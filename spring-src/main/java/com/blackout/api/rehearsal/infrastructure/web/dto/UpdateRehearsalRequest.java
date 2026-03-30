package com.blackout.api.rehearsal.infrastructure.web.dto;

public record UpdateRehearsalRequest(
        String date,
        String notes
) {}
