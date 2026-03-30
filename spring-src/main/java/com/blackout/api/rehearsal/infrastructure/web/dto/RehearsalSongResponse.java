package com.blackout.api.rehearsal.infrastructure.web.dto;

public record RehearsalSongResponse(
        String id,
        String rehearsalId,
        String songId,
        String title,
        Integer duration,
        String notes
) {}
