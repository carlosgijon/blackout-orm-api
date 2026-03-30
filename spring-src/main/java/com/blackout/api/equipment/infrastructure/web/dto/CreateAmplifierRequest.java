package com.blackout.api.equipment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAmplifierRequest(
        @NotBlank String name,
        @NotBlank String type,
        String brand,
        String model,
        Integer wattage,
        @NotBlank String routing,
        String monoStereo,
        String stagePosition,
        String memberId,
        String cabinetBrand,
        String speakerBrand,
        String speakerModel,
        String speakerConfig,
        String notes) {
}
