package com.blackout.api.setlist.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePlaylistRequest(@NotBlank String name, String description) {}
