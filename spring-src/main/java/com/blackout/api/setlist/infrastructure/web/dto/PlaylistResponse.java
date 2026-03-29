package com.blackout.api.setlist.infrastructure.web.dto;

import com.blackout.api.setlist.domain.Playlist;
import java.time.Instant;

public record PlaylistResponse(String id, String name, String description, Instant createdAt) {
    public static PlaylistResponse from(Playlist p) {
        return new PlaylistResponse(p.getId(), p.getName(), p.getDescription(), p.getCreatedAt());
    }
}
