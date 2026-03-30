package com.blackout.api.equipment.infrastructure.web.dto;

import com.blackout.api.equipment.domain.Amplifier;

public record AmplifierResponse(
        String id,
        String bandId,
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

    public static AmplifierResponse from(Amplifier a) {
        return new AmplifierResponse(
                a.getId(),
                a.getBandId(),
                a.getName(),
                a.getType(),
                a.getBrand(),
                a.getModel(),
                a.getWattage(),
                a.getRouting(),
                a.getMonoStereo(),
                a.getStagePosition(),
                a.getMemberId(),
                a.getCabinetBrand(),
                a.getSpeakerBrand(),
                a.getSpeakerModel(),
                a.getSpeakerConfig(),
                a.getNotes());
    }
}
