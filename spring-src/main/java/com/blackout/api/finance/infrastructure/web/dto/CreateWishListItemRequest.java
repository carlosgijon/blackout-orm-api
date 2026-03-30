package com.blackout.api.finance.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWishListItemRequest(
        @NotBlank String name,
        @NotBlank String category,
        Double estimatedPrice,
        String priority,
        String notes
) {}
