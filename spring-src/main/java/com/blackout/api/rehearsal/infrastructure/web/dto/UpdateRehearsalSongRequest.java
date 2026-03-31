package com.blackout.api.rehearsal.infrastructure.web.dto;

public record UpdateRehearsalSongRequest(
        String notes,
        Integer rating
) {}
