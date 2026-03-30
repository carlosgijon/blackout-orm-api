package com.blackout.api.gig.infrastructure.web.dto;

public record CreateGigRequest(
        String title,
        String venueId,
        String setlistId,
        String date,
        String time,
        String pay,
        String loadInTime,
        String soundcheckTime,
        String setTime,
        String notes) {}
