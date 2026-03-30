package com.blackout.api.equipment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMicrophoneRequest(
        @NotBlank String name,
        String brand,
        String model,
        String type,
        String polarPattern,
        String monoStereo,
        boolean phantomPower,
        String assignedToType,
        String assignedToId,
        String usage,
        String notes) {
}
