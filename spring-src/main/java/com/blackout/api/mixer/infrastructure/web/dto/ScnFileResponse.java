package com.blackout.api.mixer.infrastructure.web.dto;

import java.time.Instant;

public record ScnFileResponse(
        String id,
        String bandId,
        String name,
        String content,
        String notes,
        String gigId,
        String venueId,
        String type,
        Instant createdAt,
        Instant updatedAt
) {}
