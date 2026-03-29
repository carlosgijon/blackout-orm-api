package com.blackout.api.identity.infrastructure.web.dto;

import jakarta.validation.constraints.*;

public record ChangePasswordRequest(@NotBlank @Size(min = 6) String newPassword) {}
