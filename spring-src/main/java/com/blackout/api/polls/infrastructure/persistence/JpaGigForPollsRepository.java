package com.blackout.api.polls.infrastructure.persistence;

import com.blackout.api.gig.domain.Gig;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaGigForPollsRepository extends JpaRepository<Gig, String> {
    boolean existsByIdAndBandId(String id, String bandId);
}
