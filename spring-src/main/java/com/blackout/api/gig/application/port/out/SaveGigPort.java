package com.blackout.api.gig.application.port.out;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.gig.domain.ChecklistItem;
import com.blackout.api.gig.domain.Gig;
import com.blackout.api.gig.domain.GigChecklist;
import com.blackout.api.gig.domain.GigContact;

public interface SaveGigPort {
    Gig save(Gig gig);
    void deleteById(String id);

    GigContact saveContact(GigContact contact);
    void deleteContact(String contactId);

    GigChecklist saveChecklist(GigChecklist checklist);
    void deleteChecklist(String checklistId);

    ChecklistItem saveItem(ChecklistItem item);
    void deleteItem(String itemId);

    /** Set done=false on every item belonging to the checklist. */
    void resetChecklistItems(String checklistId);

    /** Cross-context: persist a finance Transaction linked to a gig. */
    Transaction saveTransaction(Transaction transaction);
}
