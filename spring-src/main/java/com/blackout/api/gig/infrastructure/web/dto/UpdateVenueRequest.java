package com.blackout.api.gig.infrastructure.web.dto;

public record UpdateVenueRequest(
        String name,
        String city,
        String address,
        String website,
        Integer capacity,
        String bookingName,
        String bookingEmail,
        String bookingPhone,
        String notes) {}
