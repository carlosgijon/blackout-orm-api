package com.blackout.api.votes.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSessionRequest(
        @NotBlank String title
) {}
