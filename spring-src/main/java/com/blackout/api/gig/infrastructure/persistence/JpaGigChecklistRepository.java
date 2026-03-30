package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.domain.GigChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

interface JpaGigChecklistRepository extends JpaRepository<GigChecklist, String> {
    List<GigChecklist> findAllByGigIdOrderByCreatedAtAsc(String gigId);

    @Query("SELECT cl FROM GigChecklist cl JOIN Gig g ON g.id = cl.gigId WHERE cl.id = :id AND g.bandId = :bandId")
    Optional<GigChecklist> findByIdAndBandId(@Param("id") String id, @Param("bandId") String bandId);
}
