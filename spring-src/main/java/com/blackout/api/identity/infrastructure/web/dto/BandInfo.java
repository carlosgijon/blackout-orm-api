package com.blackout.api.identity.infrastructure.web.dto;

import com.blackout.api.identity.domain.Band;

public record BandInfo(String id, String name, String slug, String logo, String role) {
    public static BandInfo from(Band b, String role) {
        return new BandInfo(b.getId(), b.getName(), b.getSlug(), b.getLogo(), role);
    }
}
