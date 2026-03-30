package com.blackout.api.gig.infrastructure.web.dto;

public record UpdateGigRequest(
        String title,
        String venueId,
        String setlistId,
        String date,
        String time,
        String status,
        String pay,
        String loadInTime,
        String soundcheckTime,
        String setTime,
        String notes,
        Integer attendance,
        String followUpDate,
        String followUpNote) {}
