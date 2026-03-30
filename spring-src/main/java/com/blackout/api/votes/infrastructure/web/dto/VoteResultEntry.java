package com.blackout.api.votes.infrastructure.web.dto;

public record VoteResultEntry(
        String songId,
        double avgRank,
        int voteCount
) {}
