package com.blackout.api.setlist.infrastructure.web.dto;

public record AddSongRequest(
    String songId,
    String type,
    String title,
    String artist,
    String album,
    Integer duration,
    Integer tempo,
    String style,
    String notes,
    String setlistName,
    boolean joinWithNext
) {}
