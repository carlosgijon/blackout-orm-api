package com.blackout.api.merch.application.port.out;

import com.blackout.api.merch.domain.MerchItem;
import com.blackout.api.merch.domain.MerchWaitingEntry;

import java.util.List;
import java.util.Optional;

public interface LoadMerchPort {
    List<MerchItem> findAllByBandIdOrderByCreatedAtAsc(String bandId);
    Optional<MerchItem> findByIdAndBandId(String id, String bandId);
    Optional<MerchWaitingEntry> findWaitingEntryById(String id);
    List<MerchWaitingEntry> findAllWaitingByBandId(String bandId);
}
