package com.blackout.api.ai.infrastructure.web.dto;

import java.util.List;

public record SetlistResult(
        List<String> orderedIds,
        List<String> joinAfter,
        String bisAfterSongId,
        String explanation
) {}
