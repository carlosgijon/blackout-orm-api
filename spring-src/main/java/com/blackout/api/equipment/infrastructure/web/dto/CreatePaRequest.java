package com.blackout.api.equipment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePaRequest(
        @NotBlank String name,
        @NotBlank String category,
        String brand,
        String model,
        int quantity,
        Integer channels,
        Integer auxSends,
        Integer wattage,
        String monitorType,
        boolean iemWireless,
        String notes) {
}
