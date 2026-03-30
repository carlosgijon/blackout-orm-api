package com.blackout.api.calendar.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCalendarEventRequest(
        @NotBlank String title,
        @NotBlank String type,
        @NotBlank String date,
        String endDate,
        String notes,
        String color
) {}
