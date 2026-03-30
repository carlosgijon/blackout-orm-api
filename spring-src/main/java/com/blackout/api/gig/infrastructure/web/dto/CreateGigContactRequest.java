package com.blackout.api.gig.infrastructure.web.dto;

public record CreateGigContactRequest(
        String date,
        String contactType,
        String notes) {}
