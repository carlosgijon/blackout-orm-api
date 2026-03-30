package com.blackout.api.votes.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record VoteSessionResponse(
        String id,
        String bandId,
        String playlistId,
        String title,
        String status,
        Instant createdAt,
        List<VoteResponse> votes
) {}
