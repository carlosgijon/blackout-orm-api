package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.gig.domain.GigChecklist;
import java.time.Instant;

public record GigChecklistResponse(
        String id,
        String gigId,
        String name,
        Instant createdAt) {

    public static GigChecklistResponse from(GigChecklist cl) {
        return new GigChecklistResponse(cl.getId(), cl.getGigId(), cl.getName(), cl.getCreatedAt());
    }
}
