package com.blackout.api.equipment.infrastructure.web.dto;

import com.blackout.api.equipment.domain.BandMember;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public record MemberResponse(
        String id,
        String bandId,
        String name,
        List<String> roles,
        String stagePosition,
        String vocalMicId,
        String notes,
        int sortOrder) {

    public static MemberResponse from(BandMember m, ObjectMapper objectMapper) {
        List<String> rolesList;
        try {
            rolesList = objectMapper.readValue(m.getRoles(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            rolesList = Collections.emptyList();
        }
        return new MemberResponse(
                m.getId(),
                m.getBandId(),
                m.getName(),
                rolesList,
                m.getStagePosition(),
                m.getVocalMicId(),
                m.getNotes(),
                m.getSortOrder());
    }
}
