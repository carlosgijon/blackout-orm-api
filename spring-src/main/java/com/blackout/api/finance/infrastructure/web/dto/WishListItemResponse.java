package com.blackout.api.finance.infrastructure.web.dto;

import java.time.Instant;

public record WishListItemResponse(
        String id,
        String name,
        String category,
        Double estimatedPrice,
        String priority,
        String notes,
        boolean purchased,
        Instant createdAt
) {}
