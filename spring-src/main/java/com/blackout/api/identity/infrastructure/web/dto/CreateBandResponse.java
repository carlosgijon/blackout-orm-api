package com.blackout.api.identity.infrastructure.web.dto;

import java.time.Instant;

public record CreateBandResponse(
    String id, String name, String slug, Instant createdAt,
    AdminUserSummary adminUser
) {
    public record AdminUserSummary(String id, String username) {}
}
