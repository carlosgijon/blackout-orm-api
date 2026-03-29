package com.blackout.api.setlist.infrastructure.web.dto;

public record PlaylistGigSummary(
    String id, String title, String date, String status, String venueName
) {}
