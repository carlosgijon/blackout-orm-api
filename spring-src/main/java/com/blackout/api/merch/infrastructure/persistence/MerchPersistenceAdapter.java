package com.blackout.api.merch.infrastructure.persistence;

import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.merch.application.port.out.LoadMerchPort;
import com.blackout.api.merch.application.port.out.SaveMerchPort;
import com.blackout.api.merch.domain.MerchItem;
import com.blackout.api.merch.domain.MerchWaitingEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class MerchPersistenceAdapter implements LoadMerchPort, SaveMerchPort {

    private final JpaMerchItemRepository itemRepo;
    private final JpaMerchWaitingRepository waitingRepo;
    final JpaTransactionForMerchRepository transactionRepo;

    MerchPersistenceAdapter(
            JpaMerchItemRepository itemRepo,
            JpaMerchWaitingRepository waitingRepo,
            JpaTransactionForMerchRepository transactionRepo) {
        this.itemRepo = itemRepo;
        this.waitingRepo = waitingRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public List<MerchItem> findAllByBandIdOrderByCreatedAtAsc(String bandId) {
        return itemRepo.findAllByBandIdOrderByCreatedAtAsc(bandId);
    }

    @Override
    public Optional<MerchItem> findByIdAndBandId(String id, String bandId) {
        return itemRepo.findById(id).filter(i -> i.getBandId().equals(bandId));
    }

    @Override
    public Optional<MerchWaitingEntry> findWaitingEntryById(String id) {
        return waitingRepo.findById(id);
    }

    @Override
    public List<MerchWaitingEntry> findAllWaitingByBandId(String bandId) {
        return waitingRepo.findAllByBandIdOrderByCreatedAtAsc(bandId);
    }

    @Override
    public MerchItem save(MerchItem item) {
        return itemRepo.save(item);
    }

    @Override
    public void deleteById(String id) {
        itemRepo.deleteById(id);
    }

    @Override
    public MerchWaitingEntry saveWaiting(MerchWaitingEntry entry) {
        return waitingRepo.save(entry);
    }

    @Override
    public void deleteWaitingById(String id) {
        waitingRepo.deleteById(id);
    }

    public Transaction saveTransaction(Transaction t) {
        return transactionRepo.save(t);
    }
}
