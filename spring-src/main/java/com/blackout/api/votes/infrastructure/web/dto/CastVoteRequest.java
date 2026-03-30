package com.blackout.api.votes.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CastVoteRequest(
        @NotBlank String voterName,
        List<String> orderedIds
) {}
