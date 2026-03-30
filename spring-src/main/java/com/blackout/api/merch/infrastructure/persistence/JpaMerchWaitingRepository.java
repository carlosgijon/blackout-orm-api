package com.blackout.api.merch.infrastructure.persistence;

import com.blackout.api.merch.domain.MerchWaitingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface JpaMerchWaitingRepository extends JpaRepository<MerchWaitingEntry, String> {

    List<MerchWaitingEntry> findAllByBandIdOrderByCreatedAtAsc(String bandId);
}
