package com.blackout.api.merch.infrastructure.web.dto;

public record UpdateWaitingRequest(
        String status,
        String contact,
        String notes
) {}
