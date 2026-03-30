package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.domain.GigContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

interface JpaGigContactRepository extends JpaRepository<GigContact, String> {
    List<GigContact> findAllByGigIdOrderByDateDesc(String gigId);
}
