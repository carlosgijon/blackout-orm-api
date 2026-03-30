package com.blackout.api.equipment.infrastructure.web.dto;

public record UpdateInstrumentRequest(
        String name,
        String type,
        String brand,
        String model,
        String routing,
        String memberId,
        String ampId,
        Integer channelOrder,
        String monoStereo,
        String notes) {
}
