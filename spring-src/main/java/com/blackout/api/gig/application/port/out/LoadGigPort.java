package com.blackout.api.gig.application.port.out;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.gig.domain.ChecklistItem;
import com.blackout.api.gig.domain.Gig;
import com.blackout.api.gig.domain.GigChecklist;
import com.blackout.api.gig.domain.GigContact;
import com.blackout.api.gig.domain.Venue;
import java.util.List;
import java.util.Optional;

public interface LoadGigPort {
    Optional<Gig> findByIdAndBandId(String id, String bandId);
    List<Gig> findAllByBandId(String bandId);
    Optional<Venue> findVenueById(String id);

    // checklist / contacts (logical grouping kept here for simplicity)
    Optional<GigChecklist> findChecklistById(String checklistId, String bandId);
    List<GigChecklist> findChecklistsByGigId(String gigId);
    List<GigContact> findContactsByGigId(String gigId);
    List<ChecklistItem> findItemsByChecklistId(String checklistId);
    ChecklistItem findItemById(String itemId);

    /** Cross-context: load finance transactions linked to a gig. */
    List<Transaction> findTransactionsByGigIdAndBandId(String gigId, String bandId);
}
