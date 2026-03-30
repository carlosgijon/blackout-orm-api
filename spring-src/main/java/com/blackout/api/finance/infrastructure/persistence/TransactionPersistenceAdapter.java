package com.blackout.api.finance.infrastructure.persistence;

import com.blackout.api.finance.application.port.out.LoadTransactionPort;
import com.blackout.api.finance.application.port.out.SaveTransactionPort;
import com.blackout.api.finance.domain.Transaction;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class TransactionPersistenceAdapter implements LoadTransactionPort, SaveTransactionPort {

    private final JpaTransactionRepository repo;

    TransactionPersistenceAdapter(JpaTransactionRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Transaction> findAllByBandIdOrderByDateDesc(String bandId) {
        return repo.findAllByBandIdOrderByDateDesc(bandId);
    }

    @Override
    public Optional<Transaction> findByIdAndBandId(String id, String bandId) {
        return repo.findById(id).filter(t -> t.getBandId().equals(bandId));
    }

    @Override
    public List<Transaction> findAllByGigIdAndBandId(String gigId, String bandId) {
        return repo.findByGigIdAndBandId(gigId, bandId);
    }

    @Override
    public Transaction save(Transaction t) {
        return repo.save(t);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
