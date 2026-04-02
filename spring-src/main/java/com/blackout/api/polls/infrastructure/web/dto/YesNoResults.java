package com.blackout.api.polls.infrastructure.web.dto;

import java.util.List;

public record YesNoResults(
        String type,
        long   total,
        long   yes,     long yesPct,
        long   no,      long noPct,
        long   abstain, long abstainPct,
        List<VoterBreakdown> voters
) {}
