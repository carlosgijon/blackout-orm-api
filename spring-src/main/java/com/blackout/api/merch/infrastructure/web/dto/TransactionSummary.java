package com.blackout.api.merch.infrastructure.web.dto;

import java.time.Instant;

public record TransactionSummary(
        String id,
        String type,
        String category,
        double amount,
        String date,
        String description,
        Instant createdAt
) {}
