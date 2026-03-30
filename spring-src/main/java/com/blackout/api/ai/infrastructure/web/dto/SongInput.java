package com.blackout.api.ai.infrastructure.web.dto;

public record SongInput(
        String id,
        String title,
        String artist,
        Integer tempo,
        String style,
        String startNote,
        String endNote,
        Integer duration
) {}
