package com.blackout.api.polls.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CastPollVoteRequest(
        @NotBlank String voterName,
        String value,                    // yes_no: "yes"|"no"|"abstain"
        List<String> approvedOptionIds,  // approval/proposal
        String comment
) {}
