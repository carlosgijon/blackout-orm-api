package com.blackout.api.setlist.infrastructure.web.dto;

import java.time.Instant;

public record PlaylistSummaryResponse(
    String id, String name, String description,
    Instant createdAt, int songCount, int totalDuration
) {}
