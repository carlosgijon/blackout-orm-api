package com.blackout.api.finance.infrastructure.web.dto;

import java.time.Instant;

public record TransactionResponse(
        String id,
        String type,
        String category,
        double amount,
        String date,
        String description,
        String gigId,
        Instant createdAt
) {}
