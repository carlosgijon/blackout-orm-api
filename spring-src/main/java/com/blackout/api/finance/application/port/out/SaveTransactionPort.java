package com.blackout.api.finance.application.port.out;

import com.blackout.api.finance.domain.Transaction;

public interface SaveTransactionPort {
    Transaction save(Transaction t);
    void deleteById(String id);
}
