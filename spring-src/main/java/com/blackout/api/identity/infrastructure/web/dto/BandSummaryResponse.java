package com.blackout.api.identity.infrastructure.web.dto;

import java.time.Instant;

public record BandSummaryResponse(String id, String name, String slug, Instant createdAt, long memberCount) {}
