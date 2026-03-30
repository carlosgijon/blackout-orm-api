package com.blackout.api.rehearsal.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRehearsalRequest(
        @NotBlank String date,
        String notes
) {}
