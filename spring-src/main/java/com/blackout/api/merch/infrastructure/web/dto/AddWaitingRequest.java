package com.blackout.api.merch.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AddWaitingRequest(
        @NotBlank String name,
        Integer quantity,
        String size,
        String contact,
        String notes
) {}
