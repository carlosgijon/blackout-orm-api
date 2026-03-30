package com.blackout.api.settings.infrastructure.web.dto;

public record UpdateSettingsRequest(
        String theme,
        String bpmApiKey,
        String fontSize,
        String fontFamily
) {}
