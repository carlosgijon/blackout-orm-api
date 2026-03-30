package com.blackout.api.finance.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTransactionRequest(
        @NotBlank String type,
        @NotBlank String category,
        double amount,
        @NotBlank String date,
        String description,
        String gigId
) {}
