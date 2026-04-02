package com.blackout.api.polls.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AddOptionRequest(@NotBlank String text, @NotBlank String proposedBy) {}
