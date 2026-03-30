package com.blackout.api.merch.application.service;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.merch.application.port.out.LoadMerchPort;
import com.blackout.api.merch.application.port.out.SaveMerchPort;
import com.blackout.api.merch.domain.MerchItem;
import com.blackout.api.merch.domain.MerchWaitingEntry;
import com.blackout.api.merch.infrastructure.persistence.MerchPersistenceAdapter;
import com.blackout.api.merch.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.BadRequestException;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MerchApplicationService {

    private final LoadMerchPort loadMerch;
    private final SaveMerchPort saveMerch;
    private final MerchPersistenceAdapter persistenceAdapter;

    public MerchApplicationService(
            LoadMerchPort loadMerch,
            SaveMerchPort saveMerch,
            MerchPersistenceAdapter persistenceAdapter) {
        this.loadMerch = loadMerch;
        this.saveMerch = saveMerch;
        this.persistenceAdapter = persistenceAdapter;
    }

    // ── MerchItem CRUD ────────────────────────────────────────────────────────

    public List<MerchItemResponse> findAll(String bandId) {
        return loadMerch.findAllByBandIdOrderByCreatedAtAsc(bandId)
                .stream().map(this::toItemResponse).toList();
    }

    @Transactional
    public MerchItemResponse create(String bandId, CreateMerchItemRequest dto) {
        MerchItem item = new MerchItem(
                bandId, dto.name(), dto.type(), dto.productionCost(), dto.sellingPrice());
        item.setBatchSize(dto.batchSize());
        item.setFixedCosts(dto.fixedCosts());
        item.setHasSizes(dto.hasSizes());
        item.setNotes(dto.notes());

        if (dto.hasSizes() && dto.stockSizes() != null) {
            item.setStockSizes(dto.stockSizes());
            item.recomputeTotal();
        } else if (dto.stock() != null) {
            item.setStock(dto.stock());
        }

        return toItemResponse(saveMerch.save(item));
    }

    @Transactional
    public MerchItemResponse update(String bandId, String id, UpdateMerchItemRequest dto) {
        MerchItem item = loadMerch.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("MerchItem not found: " + id));

        if (dto.name() != null) item.setName(dto.name());
        if (dto.type() != null) item.setType(dto.type());
        if (dto.productionCost() != null) item.setProductionCost(dto.productionCost());
        if (dto.batchSize() != null) item.setBatchSize(dto.batchSize());
        if (dto.sellingPrice() != null) item.setSellingPrice(dto.sellingPrice());
        if (dto.fixedCosts() != null) item.setFixedCosts(dto.fixedCosts());
        if (dto.hasSizes() != null) item.setHasSizes(dto.hasSizes());
        if (dto.notes() != null) item.setNotes(dto.notes());

        if (dto.hasSizes() != null && dto.hasSizes() && dto.stockSizes() != null) {
            item.setStockSizes(dto.stockSizes());
            item.recomputeTotal();
        } else if (dto.stock() != null) {
            item.setStock(dto.stock());
        }

        return toItemResponse(saveMerch.save(item));
    }

    @Transactional
    public void remove(String bandId, String id) {
        loadMerch.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("MerchItem not found: " + id));
        saveMerch.deleteById(id);
    }

    // ── Restock ───────────────────────────────────────────────────────────────

    @Transactional
    public MerchItemResponse restock(String bandId, String id, RestockRequest dto) {
        MerchItem item = loadMerch.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("MerchItem not found: " + id));

        if (dto.stockSizes() != null) {
            item.setStockSizes(dto.stockSizes());
            item.recomputeTotal();
        } else if (dto.stock() != null) {
            item.setStock(dto.stock());
        }

        return toItemResponse(saveMerch.save(item));
    }

    // ── Sell ──────────────────────────────────────────────────────────────────

    @Transactional
    public SellResponse sell(String bandId, String id, SellRequest dto) {
        if (dto.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        MerchItem item = loadMerch.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("MerchItem not found: " + id));

        // Domain method handles stock validation and decrement
        item.sell(dto.quantity(), dto.size());
        MerchItem saved = saveMerch.save(item);

        // Build description
        String sizePart = (dto.size() != null && !dto.size().isBlank()) ? "[" + dto.size() + "]" : "";
        String notesPart = (dto.notes() != null && !dto.notes().isBlank()) ? " - " + dto.notes() : "";
        String description = String.format(
                "Venta merch: %d× %s%s @ %.2f€%s",
                dto.quantity(), item.getName(), sizePart, dto.unitPrice(), notesPart);

        double amount = dto.quantity() * dto.unitPrice();

        Transaction t = new Transaction(bandId, "income", "merch_sales", amount, dto.date());
        t.setDescription(description);
        t.setGigId(dto.gigId());
        Transaction savedTransaction = persistenceAdapter.saveTransaction(t);

        TransactionSummary summary = new TransactionSummary(
                savedTransaction.getId(), savedTransaction.getType(), savedTransaction.getCategory(),
                savedTransaction.getAmount(), savedTransaction.getDate(),
                savedTransaction.getDescription(), savedTransaction.getCreatedAt());

        return new SellResponse(toItemResponse(saved), summary);
    }

    // ── Waiting List ──────────────────────────────────────────────────────────

    public List<WaitingEntryResponse> getAllWaiting(String bandId) {
        List<MerchWaitingEntry> entries = loadMerch.findAllWaitingByBandId(bandId);
        return entries.stream().map(entry -> {
            MerchItem item = loadMerch.findByIdAndBandId(entry.getItemId(), bandId).orElse(null);
            String itemName = item != null ? item.getName() : null;
            String itemType = item != null ? item.getType() : null;
            return toWaitingResponse(entry, itemName, itemType);
        }).toList();
    }

    @Transactional
    public WaitingEntryResponse addToWaitingList(String bandId, String itemId, AddWaitingRequest dto) {
        MerchItem item = loadMerch.findByIdAndBandId(itemId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("MerchItem not found: " + itemId));

        MerchWaitingEntry entry = new MerchWaitingEntry(bandId, itemId, dto.name());
        if (dto.quantity() != null) entry.setQuantity(dto.quantity());
        entry.setSize(dto.size());
        entry.setContact(dto.contact());
        entry.setNotes(dto.notes());

        MerchWaitingEntry saved = saveMerch.saveWaiting(entry);
        return toWaitingResponse(saved, item.getName(), item.getType());
    }

    @Transactional
    public WaitingEntryResponse updateWaitingEntry(String bandId, String entryId, UpdateWaitingRequest dto) {
        MerchWaitingEntry entry = loadMerch.findWaitingEntryById(entryId)
                .filter(e -> e.getBandId().equals(bandId))
                .orElseThrow(() -> new ResourceNotFoundException("WaitingEntry not found: " + entryId));

        if (dto.status() != null) entry.setStatus(dto.status());
        if (dto.contact() != null) entry.setContact(dto.contact());
        if (dto.notes() != null) entry.setNotes(dto.notes());

        MerchWaitingEntry saved = saveMerch.saveWaiting(entry);

        MerchItem item = loadMerch.findByIdAndBandId(saved.getItemId(), bandId).orElse(null);
        String itemName = item != null ? item.getName() : null;
        String itemType = item != null ? item.getType() : null;
        return toWaitingResponse(saved, itemName, itemType);
    }

    @Transactional
    public void removeWaitingEntry(String bandId, String entryId) {
        loadMerch.findWaitingEntryById(entryId)
                .filter(e -> e.getBandId().equals(bandId))
                .orElseThrow(() -> new ResourceNotFoundException("WaitingEntry not found: " + entryId));
        saveMerch.deleteWaitingById(entryId);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private MerchItemResponse toItemResponse(MerchItem item) {
        return new MerchItemResponse(
                item.getId(), item.getName(), item.getType(),
                item.getProductionCost(), item.getBatchSize(), item.getSellingPrice(),
                item.getFixedCosts(), item.getStock(), item.isHasSizes(),
                item.getStockSizes(), item.getNotes(), item.getCreatedAt());
    }

    private WaitingEntryResponse toWaitingResponse(MerchWaitingEntry entry, String itemName, String itemType) {
        return new WaitingEntryResponse(
                entry.getId(), entry.getItemId(), itemName, itemType,
                entry.getName(), entry.getQuantity(), entry.getSize(),
                entry.getContact(), entry.getNotes(), entry.getStatus(), entry.getCreatedAt());
    }
}
