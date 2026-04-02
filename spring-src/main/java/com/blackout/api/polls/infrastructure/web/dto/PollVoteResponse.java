package com.blackout.api.polls.infrastructure.web.dto;

import java.time.Instant;

public record PollVoteResponse(
        String id,
        String pollId,
        String optionId,
        String voterName,
        String value,
        String comment,
        Instant createdAt
) {}
