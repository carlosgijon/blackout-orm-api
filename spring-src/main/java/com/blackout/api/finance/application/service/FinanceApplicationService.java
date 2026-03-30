package com.blackout.api.finance.application.service;

import com.blackout.api.finance.application.port.out.*;
import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.finance.domain.WishListItem;
import com.blackout.api.finance.infrastructure.web.dto.*;
import com.blackout.api.settings.domain.Setting;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class FinanceApplicationService {

    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "instrument", "equipment",
            "equipment",  "equipment",
            "merch",      "merch_production",
            "studio",     "recording",
            "other",      "other"
    );

    private final LoadTransactionPort loadTransaction;
    private final SaveTransactionPort saveTransaction;
    private final LoadWishListPort loadWishList;
    private final SaveWishListPort saveWishList;
    private final LoadSettingPort loadSetting;
    private final SaveSettingPort saveSetting;
    private final ApplicationEventPublisher eventPublisher;

    public FinanceApplicationService(
            LoadTransactionPort loadTransaction,
            SaveTransactionPort saveTransaction,
            LoadWishListPort loadWishList,
            SaveWishListPort saveWishList,
            LoadSettingPort loadSetting,
            SaveSettingPort saveSetting,
            ApplicationEventPublisher eventPublisher) {
        this.loadTransaction = loadTransaction;
        this.saveTransaction = saveTransaction;
        this.loadWishList = loadWishList;
        this.saveWishList = saveWishList;
        this.loadSetting = loadSetting;
        this.saveSetting = saveSetting;
        this.eventPublisher = eventPublisher;
    }

    // ── Transactions ──────────────────────────────────────────────────────────

    public List<TransactionResponse> findAllTransactions(String bandId) {
        return loadTransaction.findAllByBandIdOrderByDateDesc(bandId)
                .stream().map(this::toTransactionResponse).toList();
    }

    @Transactional
    public TransactionResponse createTransaction(String bandId, CreateTransactionRequest dto) {
        Transaction t = new Transaction(bandId, dto.type(), dto.category(), dto.amount(), dto.date());
        t.setDescription(dto.description());
        t.setGigId(dto.gigId());
        return toTransactionResponse(saveTransaction.save(t));
    }

    @Transactional
    public TransactionResponse updateTransaction(String bandId, String id, UpdateTransactionRequest dto) {
        Transaction t = loadTransaction.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        if (dto.type() != null) t.setType(dto.type());
        if (dto.category() != null) t.setCategory(dto.category());
        if (dto.date() != null) t.setDate(dto.date());
        t.setAmount(dto.amount());
        t.setDescription(dto.description());
        t.setGigId(dto.gigId());
        return toTransactionResponse(saveTransaction.save(t));
    }

    @Transactional
    public void removeTransaction(String bandId, String id) {
        loadTransaction.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        saveTransaction.deleteById(id);
    }

    // ── Wish List ─────────────────────────────────────────────────────────────

    public List<WishListItemResponse> findAllWishList(String bandId) {
        return loadWishList.findAllByBandIdOrderByCreatedAtAsc(bandId)
                .stream().map(this::toWishListResponse).toList();
    }

    @Transactional
    public WishListItemResponse createWishListItem(String bandId, CreateWishListItemRequest dto) {
        WishListItem item = new WishListItem(bandId, dto.name(), dto.category());
        item.setEstimatedPrice(dto.estimatedPrice());
        if (dto.priority() != null) item.setPriority(dto.priority());
        item.setNotes(dto.notes());
        return toWishListResponse(saveWishList.save(item));
    }

    @Transactional
    public WishListItemResponse updateWishListItem(String bandId, String id, UpdateWishListItemRequest dto) {
        WishListItem item = loadWishList.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("WishListItem not found: " + id));

        boolean wasPurchased = item.isPurchased();

        if (dto.name() != null) item.setName(dto.name());
        if (dto.category() != null) item.setCategory(dto.category());
        if (dto.estimatedPrice() != null) item.setEstimatedPrice(dto.estimatedPrice());
        if (dto.priority() != null) item.setPriority(dto.priority());
        if (dto.notes() != null) item.setNotes(dto.notes());

        // Business logic: auto-create expense transaction when item is marked purchased
        if (!wasPurchased && Boolean.TRUE.equals(dto.purchased())) {
            item.markPurchased(eventPublisher);

            Double amount = dto.finalPrice() != null ? dto.finalPrice() : item.getEstimatedPrice();
            if (amount != null && amount > 0) {
                String mappedCategory = CATEGORY_MAP.getOrDefault(item.getCategory(), "other");
                String today = LocalDate.now().toString();
                Transaction expense = new Transaction(
                        bandId, "expense", mappedCategory, amount, today);
                expense.setDescription("Lista de deseos: " + item.getName());
                saveTransaction.save(expense);
            }
        }

        return toWishListResponse(saveWishList.save(item));
    }

    @Transactional
    public void removeWishListItem(String bandId, String id) {
        loadWishList.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("WishListItem not found: " + id));
        saveWishList.deleteById(id);
    }

    // ── Initial Balance ───────────────────────────────────────────────────────

    public InitialBalanceResponse getInitialBalance(String bandId) {
        double balance = loadSetting.findByBandIdAndKey(bandId, "initial_balance")
                .map(s -> Double.parseDouble(s.getValue()))
                .orElse(0.0);
        return new InitialBalanceResponse(balance);
    }

    @Transactional
    public InitialBalanceResponse setInitialBalance(String bandId, double amount) {
        Setting setting = loadSetting.findByBandIdAndKey(bandId, "initial_balance")
                .orElse(new Setting(bandId, "initial_balance", "0"));
        setting.setValue(String.valueOf(amount));
        saveSetting.save(setting);
        return new InitialBalanceResponse(amount);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private TransactionResponse toTransactionResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(), t.getType(), t.getCategory(), t.getAmount(),
                t.getDate(), t.getDescription(), t.getGigId(), t.getCreatedAt());
    }

    private WishListItemResponse toWishListResponse(WishListItem item) {
        return new WishListItemResponse(
                item.getId(), item.getName(), item.getCategory(),
                item.getEstimatedPrice(), item.getPriority(), item.getNotes(),
                item.isPurchased(), item.getCreatedAt());
    }
}
