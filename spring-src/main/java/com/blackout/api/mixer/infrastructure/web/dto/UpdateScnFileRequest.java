package com.blackout.api.mixer.infrastructure.web.dto;

public record UpdateScnFileRequest(
        String name,
        String notes,
        String gigId,
        String venueId,
        String type
) {}
