package com.blackout.api.rehearsal.infrastructure.web.dto;

public record RehearsalSongResponse(
        String id,
        String songId,
        String title,
        String artist,
        Integer tempo,
        String style,
        String notes,
        Integer rating
) {}
