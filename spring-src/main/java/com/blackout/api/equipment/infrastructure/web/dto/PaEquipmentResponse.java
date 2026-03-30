package com.blackout.api.equipment.infrastructure.web.dto;

import com.blackout.api.equipment.domain.PaEquipment;

public record PaEquipmentResponse(
        String id,
        String bandId,
        String name,
        String category,
        String brand,
        String model,
        int quantity,
        Integer channels,
        Integer auxSends,
        Integer wattage,
        String monitorType,
        boolean iemWireless,
        String notes) {

    public static PaEquipmentResponse from(PaEquipment p) {
        return new PaEquipmentResponse(
                p.getId(),
                p.getBandId(),
                p.getName(),
                p.getCategory(),
                p.getBrand(),
                p.getModel(),
                p.getQuantity(),
                p.getChannels(),
                p.getAuxSends(),
                p.getWattage(),
                p.getMonitorType(),
                p.isIemWireless(),
                p.getNotes());
    }
}
