package com.blackout.api.gig.application.service;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.gig.application.port.out.LoadGigPort;
import com.blackout.api.gig.application.port.out.SaveGigPort;
import com.blackout.api.gig.domain.ChecklistItem;
import com.blackout.api.gig.domain.Gig;
import com.blackout.api.gig.domain.GigChecklist;
import com.blackout.api.gig.domain.GigContact;
import com.blackout.api.gig.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GigApplicationService {

    private final LoadGigPort loadGig;
    private final SaveGigPort saveGig;
    private final ApplicationEventPublisher events;

    public GigApplicationService(LoadGigPort loadGig,
                                  SaveGigPort saveGig,
                                  ApplicationEventPublisher events) {
        this.loadGig = loadGig;
        this.saveGig = saveGig;
        this.events = events;
    }

    // ── Gigs ─────────────────────────────────────────────────────────────────

    public List<GigResponse> findAll(String bandId) {
        return loadGig.findAllByBandId(bandId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GigResponse create(String bandId, CreateGigRequest req) {
        Gig gig = new Gig(bandId, req.title());
        gig.setVenueId(req.venueId());
        gig.setSetlistId(req.setlistId());
        gig.setDate(req.date());
        gig.setTime(req.time());
        gig.setPay(req.pay());
        gig.setLoadInTime(req.loadInTime());
        gig.setSoundcheckTime(req.soundcheckTime());
        gig.setSetTime(req.setTime());
        gig.setNotes(req.notes());
        return toResponse(saveGig.save(gig));
    }

    @Transactional
    public GigResponse update(String bandId, String id, UpdateGigRequest req) {
        Gig gig = findOwned(bandId, id);
        if (req.title() != null)         gig.setTitle(req.title());
        if (req.venueId() != null)        gig.setVenueId(req.venueId());
        if (req.setlistId() != null)      gig.setSetlistId(req.setlistId());
        if (req.date() != null)           gig.setDate(req.date());
        if (req.time() != null)           gig.setTime(req.time());
        if (req.pay() != null)            gig.setPay(req.pay());
        if (req.loadInTime() != null)     gig.setLoadInTime(req.loadInTime());
        if (req.soundcheckTime() != null) gig.setSoundcheckTime(req.soundcheckTime());
        if (req.setTime() != null)        gig.setSetTime(req.setTime());
        if (req.notes() != null)          gig.setNotes(req.notes());
        if (req.attendance() != null)     gig.setAttendance(req.attendance());
        if (req.followUpDate() != null)   gig.setFollowUpDate(req.followUpDate());
        if (req.followUpNote() != null)   gig.setFollowUpNote(req.followUpNote());
        return toResponse(saveGig.save(gig));
    }

    @Transactional
    public GigResponse updateStatus(String bandId, String id, String status) {
        Gig gig = findOwned(bandId, id);
        boolean wasCobrado = "cobrado".equals(gig.getStatus());

        gig.changeStatus(status, events);
        Gig saved = saveGig.save(gig);

        // Inline: when cobrado for the first time, create income transaction
        if ("cobrado".equals(status) && !wasCobrado) {
            String rawPay = gig.getPay() != null ? gig.getPay() : "";
            String cleaned = rawPay.replaceAll("[^0-9.,]", "").replace(',', '.');
            if (!cleaned.isEmpty()) {
                try {
                    double amount = Double.parseDouble(cleaned);
                    if (amount > 0) {
                        String today = LocalDate.now().toString();
                        Transaction tx = new Transaction(bandId, "income", "gig", amount, today);
                        tx.setGigId(id);
                        tx.setDescription("Cobro: " + gig.getTitle());
                        saveGig.saveTransaction(tx);
                    }
                } catch (NumberFormatException ignored) {
                    // non-parseable pay field — skip transaction creation
                }
            }
        }

        return toResponse(saved);
    }

    @Transactional
    public void updateFollowUp(String bandId, String id, UpdateFollowUpRequest req) {
        Gig gig = findOwned(bandId, id);
        gig.setFollowUpDate(req.followUpDate());
        gig.setFollowUpNote(req.followUpNote());
        saveGig.save(gig);
    }

    @Transactional
    public void remove(String bandId, String id) {
        findOwned(bandId, id);
        saveGig.deleteById(id);
    }

    // ── Contacts ─────────────────────────────────────────────────────────────

    public List<GigContactResponse> getContacts(String bandId, String gigId) {
        findOwned(bandId, gigId);
        return loadGig.findContactsByGigId(gigId).stream()
                .map(GigContactResponse::from)
                .toList();
    }

    @Transactional
    public GigContactResponse createContact(String bandId, String gigId, CreateGigContactRequest req) {
        findOwned(bandId, gigId);
        GigContact contact = new GigContact(gigId,
                req.date() != null ? req.date() : LocalDate.now().toString(),
                req.contactType() != null ? req.contactType() : "other");
        contact.setNotes(req.notes());
        return GigContactResponse.from(saveGig.saveContact(contact));
    }

    @Transactional
    public void removeContact(String bandId, String gigId, String contactId) {
        findOwned(bandId, gigId);
        saveGig.deleteContact(contactId);
    }

    // ── Checklists ───────────────────────────────────────────────────────────

    public List<GigChecklistResponse> getChecklists(String bandId, String gigId) {
        findOwned(bandId, gigId);
        return loadGig.findChecklistsByGigId(gigId).stream()
                .map(GigChecklistResponse::from)
                .toList();
    }

    @Transactional
    public GigChecklistResponse createChecklist(String bandId, String gigId, CreateChecklistRequest req) {
        findOwned(bandId, gigId);
        GigChecklist checklist = new GigChecklist(gigId, req.name());
        return GigChecklistResponse.from(saveGig.saveChecklist(checklist));
    }

    @Transactional
    public void removeChecklist(String bandId, String gigId, String checklistId) {
        findOwned(bandId, gigId);
        saveGig.deleteChecklist(checklistId);
    }

    // ── Checklist Items ──────────────────────────────────────────────────────

    public List<ChecklistItemResponse> getItems(String bandId, String checklistId) {
        findOwnedChecklist(bandId, checklistId);
        return loadGig.findItemsByChecklistId(checklistId).stream()
                .map(ChecklistItemResponse::from)
                .toList();
    }

    @Transactional
    public ChecklistItemResponse createItem(String bandId, String checklistId, CreateChecklistItemRequest req) {
        findOwnedChecklist(bandId, checklistId);
        ChecklistItem item = new ChecklistItem(checklistId, req.text());
        item.setDone(req.done());
        item.setSortOrder(req.sortOrder());
        return ChecklistItemResponse.from(saveGig.saveItem(item));
    }

    @Transactional
    public ChecklistItemResponse updateItem(String bandId, String checklistId, String itemId, UpdateChecklistItemRequest req) {
        findOwnedChecklist(bandId, checklistId);
        ChecklistItem item = loadGig.findItemById(itemId);
        if (req.text() != null) item.setText(req.text());
        item.setDone(req.done());
        item.setSortOrder(req.sortOrder());
        return ChecklistItemResponse.from(saveGig.saveItem(item));
    }

    @Transactional
    public void removeItem(String bandId, String checklistId, String itemId) {
        findOwnedChecklist(bandId, checklistId);
        saveGig.deleteItem(itemId);
    }

    @Transactional
    public void resetItems(String bandId, String checklistId) {
        findOwnedChecklist(bandId, checklistId);
        saveGig.resetChecklistItems(checklistId);
    }

    // ── Summary ──────────────────────────────────────────────────────────────

    public GigSummaryResponse getSummary(String bandId, String gigId) {
        Gig gig = findOwned(bandId, gigId);
        String venueName = gig.getVenueId() != null
                ? loadGig.findVenueById(gig.getVenueId()).map(v -> v.getName()).orElse(null)
                : null;
        GigResponse gigResponse = GigResponse.from(gig, venueName);

        List<Transaction> allTx = loadGig.findTransactionsByGigIdAndBandId(gigId, bandId);
        List<TransactionSummary> transactions = allTx.stream()
                .map(TransactionSummary::from)
                .toList();
        List<TransactionSummary> merchSales = allTx.stream()
                .filter(t -> "merch_sales".equals(t.getCategory()))
                .map(TransactionSummary::from)
                .toList();

        return new GigSummaryResponse(gigResponse, transactions, merchSales);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Gig findOwned(String bandId, String id) {
        return loadGig.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Gig not found"));
    }

    private GigChecklist findOwnedChecklist(String bandId, String checklistId) {
        return loadGig.findChecklistById(checklistId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist not found"));
    }

    private GigResponse toResponse(Gig gig) {
        String venueName = gig.getVenueId() != null
                ? loadGig.findVenueById(gig.getVenueId()).map(v -> v.getName()).orElse(null)
                : null;
        return GigResponse.from(gig, venueName);
    }
}
