package com.blackout.api.merch.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SellRequest(
        int quantity,
        double unitPrice,
        @NotBlank String date,
        String size,
        String notes,
        String gigId
) {}
