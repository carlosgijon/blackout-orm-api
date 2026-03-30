package com.blackout.api.finance.infrastructure.web.dto;

public record UpdateWishListItemRequest(
        String name,
        String category,
        Double estimatedPrice,
        String priority,
        String notes,
        Boolean purchased,
        Double finalPrice
) {}
