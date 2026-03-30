package com.blackout.api.calendar.infrastructure.web.dto;

import java.time.Instant;

public record CalendarEventResponse(
        String id,
        String bandId,
        String title,
        String type,
        String date,
        String endDate,
        String notes,
        String color,
        Instant createdAt
) {}
