package com.blackout.api.rehearsal.infrastructure.web.dto;

public record UpdateRehearsalSongRequest(
        String title,
        Integer duration,
        String notes
) {}
