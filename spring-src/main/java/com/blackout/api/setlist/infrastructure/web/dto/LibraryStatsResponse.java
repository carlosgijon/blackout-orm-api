package com.blackout.api.setlist.infrastructure.web.dto;

import java.util.List;
import java.util.Map;

public record LibraryStatsResponse(
    int totalSongs,
    int totalWithTempo,
    int totalWithDuration,
    Map<String, Integer> byGenre,
    List<SongWithUsage> mostUsed,
    List<SongWithUsage> neverUsed
) {
    public record SongWithUsage(
        String id, String title, String artist,
        Integer tempo, Integer duration, int usageCount
    ) {}
}
