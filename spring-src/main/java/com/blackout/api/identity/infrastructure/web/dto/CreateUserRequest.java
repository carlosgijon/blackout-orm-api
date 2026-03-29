package com.blackout.api.identity.infrastructure.web.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
    @NotBlank String username,
    @NotBlank @Size(min = 6) String password,
    String displayName,
    @NotBlank String role
) {}
