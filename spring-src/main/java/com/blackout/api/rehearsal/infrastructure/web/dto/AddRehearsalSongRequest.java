package com.blackout.api.rehearsal.infrastructure.web.dto;

public record AddRehearsalSongRequest(
        String songId,
        String notes,
        Integer rating
) {}
