package com.blackout.api.equipment.infrastructure.web.dto;

public record UpdateMicrophoneRequest(
        String name,
        String brand,
        String model,
        String type,
        String polarPattern,
        String monoStereo,
        Boolean phantomPower,
        String assignedToType,
        String assignedToId,
        String usage,
        String notes) {
}
