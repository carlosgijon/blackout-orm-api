package com.blackout.api.identity.infrastructure.web.dto;

import com.blackout.api.identity.domain.User;
import java.time.Instant;

public record UserResponse(
    String id,
    String username,
    String displayName,
    String role,
    boolean isActive,
    Instant createdAt,
    String bandId
) {
    public static UserResponse from(User u) {
        return new UserResponse(
            u.getId(), u.getUsername(), u.getDisplayName(),
            u.getRole(), u.isActive(), u.getCreatedAt(), u.getBandId()
        );
    }
}
