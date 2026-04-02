package com.blackout.api.polls.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record PollResponse(
        String id,
        String bandId,
        String title,
        String description,
        String type,
        String status,
        String createdBy,
        String deadline,
        String linkedGigId,
        Instant createdAt,
        List<PollOptionResponse> options,
        List<PollVoteResponse> votes,
        long voteCount
) {}
