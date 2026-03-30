package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.gig.domain.GigContact;
import java.time.Instant;

public record GigContactResponse(
        String id,
        String gigId,
        String date,
        String contactType,
        String notes,
        Instant createdAt) {

    public static GigContactResponse from(GigContact c) {
        return new GigContactResponse(
                c.getId(), c.getGigId(), c.getDate(),
                c.getContactType(), c.getNotes(), c.getCreatedAt());
    }
}
