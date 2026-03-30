package com.blackout.api.calendar.infrastructure.web.dto;

public record UpdateCalendarEventRequest(
        String title,
        String type,
        String date,
        String endDate,
        String notes,
        String color
) {}
