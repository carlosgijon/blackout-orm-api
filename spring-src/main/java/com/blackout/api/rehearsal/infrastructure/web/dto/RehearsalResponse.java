package com.blackout.api.rehearsal.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record RehearsalResponse(
        String id,
        String date,
        String notes,
        String status,
        String createdAt,
        List<RehearsalSongResponse> songs
) {}
