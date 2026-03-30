package com.blackout.api.merch.infrastructure.web.dto;

import java.time.Instant;
import java.util.Map;

public record MerchItemResponse(
        String id,
        String name,
        String type,
        double productionCost,
        int batchSize,
        double sellingPrice,
        double fixedCosts,
        int stock,
        boolean hasSizes,
        Map<String, Integer> stockSizes,
        String notes,
        Instant createdAt
) {}
