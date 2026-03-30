package com.blackout.api.merch.application.port.out;

import com.blackout.api.merch.domain.MerchItem;
import com.blackout.api.merch.domain.MerchWaitingEntry;

public interface SaveMerchPort {
    MerchItem save(MerchItem item);
    void deleteById(String id);
    MerchWaitingEntry saveWaiting(MerchWaitingEntry entry);
    void deleteWaitingById(String id);
}
