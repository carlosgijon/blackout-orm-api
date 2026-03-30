package com.blackout.api.gig.infrastructure.web.dto;

import java.util.List;

public record GigSummaryResponse(
        GigResponse gig,
        List<TransactionSummary> transactions,
        List<TransactionSummary> merchSales) {}
