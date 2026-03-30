package com.blackout.api.rehearsal.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record RehearsalResponse(
        String id,
        String bandId,
        String date,
        String notes,
        Instant createdAt,
        List<RehearsalSongResponse> songs
) {}
