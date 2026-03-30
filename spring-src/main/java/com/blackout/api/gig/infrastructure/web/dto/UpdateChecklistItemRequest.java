package com.blackout.api.gig.infrastructure.web.dto;

public record UpdateChecklistItemRequest(String text, boolean done, int sortOrder) {}
