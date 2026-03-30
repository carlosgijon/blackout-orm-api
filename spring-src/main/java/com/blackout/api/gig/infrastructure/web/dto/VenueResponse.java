package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.gig.domain.Venue;
import java.time.Instant;

public record VenueResponse(
        String id,
        String bandId,
        String name,
        String city,
        String address,
        String website,
        Integer capacity,
        String bookingName,
        String bookingEmail,
        String bookingPhone,
        String notes,
        Instant createdAt) {

    public static VenueResponse from(Venue v) {
        return new VenueResponse(
                v.getId(), v.getBandId(), v.getName(),
                v.getCity(), v.getAddress(), v.getWebsite(),
                v.getCapacity(), v.getBookingName(), v.getBookingEmail(),
                v.getBookingPhone(), v.getNotes(), v.getCreatedAt());
    }
}
