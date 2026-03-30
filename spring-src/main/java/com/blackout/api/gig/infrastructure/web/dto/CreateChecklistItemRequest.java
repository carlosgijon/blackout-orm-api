package com.blackout.api.gig.infrastructure.web.dto;

public record CreateChecklistItemRequest(String text, boolean done, int sortOrder) {}
