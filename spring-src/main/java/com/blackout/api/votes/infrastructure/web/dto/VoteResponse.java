package com.blackout.api.votes.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record VoteResponse(
        String id,
        String sessionId,
        String voterName,
        List<String> orderedIds,
        Instant createdAt
) {}
