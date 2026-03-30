package com.blackout.api.settings.infrastructure.web.dto;

public record SettingsResponse(
        String theme,
        String bpmApiKey,
        String fontSize,
        String fontFamily
) {}
