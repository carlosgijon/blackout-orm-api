package com.blackout.api.equipment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateMemberRequest(
        @NotBlank String name,
        List<String> roles,
        String stagePosition,
        String vocalMicId,
        String notes,
        int sortOrder) {
}
