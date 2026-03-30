package com.blackout.api.equipment.infrastructure.web.dto;

public record UpdatePaRequest(
        String name,
        String category,
        String brand,
        String model,
        Integer quantity,
        Integer channels,
        Integer auxSends,
        Integer wattage,
        String monitorType,
        Boolean iemWireless,
        String notes) {
}
