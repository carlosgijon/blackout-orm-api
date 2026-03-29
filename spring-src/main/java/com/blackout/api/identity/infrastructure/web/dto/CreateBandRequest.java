package com.blackout.api.identity.infrastructure.web.dto;

import jakarta.validation.constraints.*;

public record CreateBandRequest(
    @NotBlank String name,
    @NotBlank String slug,
    @NotBlank String adminUsername,
    @NotBlank @Size(min = 6) String adminPassword
) {}
