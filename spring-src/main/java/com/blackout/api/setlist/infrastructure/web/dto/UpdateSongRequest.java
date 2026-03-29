package com.blackout.api.setlist.infrastructure.web.dto;

public record UpdateSongRequest(
    String type,
    String title,
    String setlistName,
    boolean joinWithNext
) {}
