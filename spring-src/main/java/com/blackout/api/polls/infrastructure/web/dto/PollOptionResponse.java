package com.blackout.api.polls.infrastructure.web.dto;

import java.time.Instant;

public record PollOptionResponse(
        String id,
        String pollId,
        String text,
        String proposedBy,
        Instant createdAt
) {}
