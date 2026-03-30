package com.blackout.api.equipment.infrastructure.web.dto;

import com.blackout.api.equipment.domain.Microphone;

public record MicrophoneResponse(
        String id,
        String bandId,
        String name,
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

    public static MicrophoneResponse from(Microphone m) {
        return new MicrophoneResponse(
                m.getId(),
                m.getBandId(),
                m.getName(),
                m.getBrand(),
                m.getModel(),
                m.getType(),
                m.getPolarPattern(),
                m.getMonoStereo(),
                m.isPhantomPower(),
                m.getAssignedToType(),
                m.getAssignedToId(),
                m.getUsage(),
                m.getNotes());
    }
}
