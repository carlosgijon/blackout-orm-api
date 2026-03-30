package com.blackout.api.merch.infrastructure.web.dto;

import java.time.Instant;

public record WaitingEntryResponse(
        String id,
        String itemId,
        String itemName,
        String itemType,
        String name,
        int quantity,
        String size,
        String contact,
        String notes,
        String status,
        Instant createdAt
) {}
