package com.blackout.api.rehearsal.infrastructure.web.dto;

import java.time.Instant;

public record RehearsalSummaryResponse(
        String id,
        String bandId,
        String date,
        String notes,
        int songCount,
        Instant createdAt
) {}
