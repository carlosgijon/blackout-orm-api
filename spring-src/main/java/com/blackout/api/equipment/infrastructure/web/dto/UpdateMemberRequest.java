package com.blackout.api.equipment.infrastructure.web.dto;

import java.util.List;

public record UpdateMemberRequest(
        String name,
        List<String> roles,
        String stagePosition,
        String vocalMicId,
        String notes,
        Integer sortOrder) {
}
