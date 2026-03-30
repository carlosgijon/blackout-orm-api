package com.blackout.api.finance.application.port.out;

import com.blackout.api.finance.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface LoadTransactionPort {
    List<Transaction> findAllByBandIdOrderByDateDesc(String bandId);
    Optional<Transaction> findByIdAndBandId(String id, String bandId);
    List<Transaction> findAllByGigIdAndBandId(String gigId, String bandId);
}
