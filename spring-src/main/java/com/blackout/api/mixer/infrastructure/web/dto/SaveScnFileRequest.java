package com.blackout.api.mixer.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveScnFileRequest(
        @NotBlank String name,
        @NotBlank String content,
        String notes,
        String gigId,
        String venueId
) {}
