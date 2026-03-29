package com.blackout.api.identity.infrastructure.web.dto;

import com.blackout.api.identity.domain.Band;
import java.time.Instant;

public record BandResponse(String id, String name, String slug, String logo, Instant createdAt) {
    public static BandResponse from(Band b) {
        return new BandResponse(b.getId(), b.getName(), b.getSlug(), b.getLogo(), b.getCreatedAt());
    }
}
