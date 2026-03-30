package com.blackout.api.equipment.infrastructure.web.dto;

public record UpdateAmplifierRequest(
        String name,
        String type,
        String brand,
        String model,
        Integer wattage,
        String routing,
        String monoStereo,
        String stagePosition,
        String memberId,
        String cabinetBrand,
        String speakerBrand,
        String speakerModel,
        String speakerConfig,
        String notes) {
}
