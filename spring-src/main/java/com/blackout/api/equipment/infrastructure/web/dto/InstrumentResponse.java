package com.blackout.api.equipment.infrastructure.web.dto;

import com.blackout.api.equipment.domain.Instrument;

public record InstrumentResponse(
        String id,
        String bandId,
        String name,
        String type,
        String brand,
        String model,
        String routing,
        String memberId,
        String ampId,
        int channelOrder,
        String monoStereo,
        String notes) {

    public static InstrumentResponse from(Instrument i) {
        return new InstrumentResponse(
                i.getId(),
                i.getBandId(),
                i.getName(),
                i.getType(),
                i.getBrand(),
                i.getModel(),
                i.getRouting(),
                i.getMemberId(),
                i.getAmpId(),
                i.getChannelOrder(),
                i.getMonoStereo(),
                i.getNotes());
    }
}
