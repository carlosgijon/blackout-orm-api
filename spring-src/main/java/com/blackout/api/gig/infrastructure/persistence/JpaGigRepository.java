package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.domain.Gig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

interface JpaGigRepository extends JpaRepository<Gig, String> {
    List<Gig> findAllByBandIdOrderByCreatedAtDesc(String bandId);
    Optional<Gig> findByIdAndBandId(String id, String bandId);
}
