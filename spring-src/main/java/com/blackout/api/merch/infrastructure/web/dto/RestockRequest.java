package com.blackout.api.merch.infrastructure.web.dto;

import java.util.Map;

public record RestockRequest(
        Integer stock,
        Map<String, Integer> stockSizes
) {}
