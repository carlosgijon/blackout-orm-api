package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.gig.application.port.out.LoadGigPort;
import com.blackout.api.gig.application.port.out.SaveGigPort;
import com.blackout.api.gig.domain.ChecklistItem;
import com.blackout.api.gig.domain.Gig;
import com.blackout.api.gig.domain.GigChecklist;
import com.blackout.api.gig.domain.GigContact;
import com.blackout.api.gig.domain.Venue;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class GigPersistenceAdapter implements LoadGigPort, SaveGigPort {

    private final JpaGigRepository gigRepo;
    private final JpaVenueRepository venueRepo;
    private final JpaGigContactRepository contactRepo;
    private final JpaGigChecklistRepository checklistRepo;
    private final JpaChecklistItemRepository itemRepo;
    private final JpaTransactionForGigRepository transactionRepo;

    public GigPersistenceAdapter(JpaGigRepository gigRepo,
                                  JpaVenueRepository venueRepo,
                                  JpaGigContactRepository contactRepo,
                                  JpaGigChecklistRepository checklistRepo,
                                  JpaChecklistItemRepository itemRepo,
                                  JpaTransactionForGigRepository transactionRepo) {
        this.gigRepo = gigRepo;
        this.venueRepo = venueRepo;
        this.contactRepo = contactRepo;
        this.checklistRepo = checklistRepo;
        this.itemRepo = itemRepo;
        this.transactionRepo = transactionRepo;
    }

    // --- LoadGigPort ---

    @Override
    public Optional<Gig> findByIdAndBandId(String id, String bandId) {
        return gigRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public List<Gig> findAllByBandId(String bandId) {
        return gigRepo.findAllByBandIdOrderByCreatedAtDesc(bandId);
    }

    @Override
    public Optional<Venue> findVenueById(String id) {
        return venueRepo.findById(id);
    }

    @Override
    public Optional<GigChecklist> findChecklistById(String checklistId, String bandId) {
        return checklistRepo.findByIdAndBandId(checklistId, bandId);
    }

    @Override
    public List<GigChecklist> findChecklistsByGigId(String gigId) {
        return checklistRepo.findAllByGigIdOrderByCreatedAtAsc(gigId);
    }

    @Override
    public List<GigContact> findContactsByGigId(String gigId) {
        return contactRepo.findAllByGigIdOrderByDateDesc(gigId);
    }

    @Override
    public List<ChecklistItem> findItemsByChecklistId(String checklistId) {
        return itemRepo.findAllByChecklistIdOrderBySortOrderAsc(checklistId);
    }

    @Override
    public ChecklistItem findItemById(String itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found"));
    }

    // --- SaveGigPort ---

    @Override
    public Gig save(Gig gig) {
        return gigRepo.save(gig);
    }

    @Override
    public void deleteById(String id) {
        gigRepo.deleteById(id);
    }

    @Override
    public GigContact saveContact(GigContact contact) {
        return contactRepo.save(contact);
    }

    @Override
    public void deleteContact(String contactId) {
        contactRepo.deleteById(contactId);
    }

    @Override
    public GigChecklist saveChecklist(GigChecklist checklist) {
        return checklistRepo.save(checklist);
    }

    @Override
    public void deleteChecklist(String checklistId) {
        checklistRepo.deleteById(checklistId);
    }

    @Override
    public ChecklistItem saveItem(ChecklistItem item) {
        return itemRepo.save(item);
    }

    @Override
    public void deleteItem(String itemId) {
        itemRepo.deleteById(itemId);
    }

    @Override
    @Transactional
    public void resetChecklistItems(String checklistId) {
        itemRepo.resetDoneByChecklistId(checklistId);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    @Override
    public List<Transaction> findTransactionsByGigIdAndBandId(String gigId, String bandId) {
        return transactionRepo.findByGigIdAndBandId(gigId, bandId);
    }
}
