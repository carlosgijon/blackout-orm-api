package com.blackout.api.bpm.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BpmLookupRequest(
        @NotBlank String title,
        @NotBlank String artist
) {}
