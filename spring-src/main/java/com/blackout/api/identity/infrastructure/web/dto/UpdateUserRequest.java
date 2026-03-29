package com.blackout.api.identity.infrastructure.web.dto;

public record UpdateUserRequest(
    String username,
    String displayName,
    String role,
    Boolean isActive
) {}
