package com.blackout.api.merch.infrastructure.web.dto;

import java.util.Map;

public record UpdateMerchItemRequest(
        String name,
        String type,
        Double productionCost,
        Integer batchSize,
        Double sellingPrice,
        Double fixedCosts,
        Integer stock,
        Boolean hasSizes,
        Map<String, Integer> stockSizes,
        String notes
) {}
