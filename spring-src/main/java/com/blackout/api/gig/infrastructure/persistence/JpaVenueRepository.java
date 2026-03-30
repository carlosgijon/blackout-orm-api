package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

interface JpaVenueRepository extends JpaRepository<Venue, String> {
    List<Venue> findAllByBandIdOrderByNameAsc(String bandId);
    Optional<Venue> findByIdAndBandId(String id, String bandId);
}
