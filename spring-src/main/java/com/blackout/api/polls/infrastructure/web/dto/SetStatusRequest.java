package com.blackout.api.polls.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SetStatusRequest(@NotBlank String status) {}
