package com.blackout.api.finance.infrastructure.web.dto;

public record UpdateTransactionRequest(
        String type,
        String category,
        double amount,
        String date,
        String description,
        String gigId
) {}
