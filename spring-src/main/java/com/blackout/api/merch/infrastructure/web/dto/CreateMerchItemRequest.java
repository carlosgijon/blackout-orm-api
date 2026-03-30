package com.blackout.api.merch.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record CreateMerchItemRequest(
        @NotBlank String name,
        @NotBlank String type,
        double productionCost,
        int batchSize,
        double sellingPrice,
        double fixedCosts,
        Integer stock,
        boolean hasSizes,
        Map<String, Integer> stockSizes,
        String notes
) {}
