package com.blackout.api.merch.infrastructure.persistence;

import com.blackout.api.merch.domain.MerchItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface JpaMerchItemRepository extends JpaRepository<MerchItem, String> {

    List<MerchItem> findAllByBandIdOrderByCreatedAtAsc(String bandId);
}
