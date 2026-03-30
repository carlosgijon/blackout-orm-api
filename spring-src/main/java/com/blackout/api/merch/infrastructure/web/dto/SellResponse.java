package com.blackout.api.merch.infrastructure.web.dto;

public record SellResponse(
        MerchItemResponse item,
        TransactionSummary transaction
) {}
