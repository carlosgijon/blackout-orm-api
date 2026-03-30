package com.blackout.api.equipment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateInstrumentRequest(
        @NotBlank String name,
        @NotBlank String type,
        String brand,
        String model,
        @NotBlank String routing,
        String memberId,
        String ampId,
        int channelOrder,
        String monoStereo,
        String notes) {
}
