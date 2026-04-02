package com.blackout.api.polls.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreatePollRequest(
        @NotBlank String title,
        String description,
        @NotBlank String type,
        @NotBlank String createdBy,
        String deadline,
        String linkedGigId,
        List<String> options
) {}
