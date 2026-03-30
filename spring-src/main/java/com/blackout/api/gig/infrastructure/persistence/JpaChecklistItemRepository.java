package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

interface JpaChecklistItemRepository extends JpaRepository<ChecklistItem, String> {
    List<ChecklistItem> findAllByChecklistIdOrderBySortOrderAsc(String checklistId);

    @Modifying
    @Query("UPDATE ChecklistItem ci SET ci.done = false WHERE ci.checklistId = :checklistId")
    void resetDoneByChecklistId(@Param("checklistId") String checklistId);
}
