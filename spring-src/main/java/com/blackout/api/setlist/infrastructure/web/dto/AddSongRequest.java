package com.blackout.api.setlist.infrastructure.web.dto;

public record AddSongRequest(
    String songId,
    String type,
    String title,
    String setlistName,
    boolean joinWithNext
) {}
