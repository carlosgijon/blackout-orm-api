package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.finance.domain.Transaction;
import java.time.Instant;

public record TransactionSummary(
        String id,
        String type,
        String category,
        double amount,
        String date,
        String description,
        String gigId,
        Instant createdAt) {

    public static TransactionSummary from(Transaction t) {
        return new TransactionSummary(
                t.getId(), t.getType(), t.getCategory(), t.getAmount(),
                t.getDate(), t.getDescription(), t.getGigId(), t.getCreatedAt());
    }
}
