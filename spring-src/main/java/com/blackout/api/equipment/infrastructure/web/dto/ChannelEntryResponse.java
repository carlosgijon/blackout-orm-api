package com.blackout.api.equipment.infrastructure.web.dto;

public record ChannelEntryResponse(
        int channelNumber,
        String name,
        String monoStereo,
        boolean phantomPower,
        String micModel,
        String micType,
        String polarPattern,
        String notes,
        String memberId) {
}
