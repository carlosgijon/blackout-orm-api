package com.blackout.api.setlist.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlaylistRequest(@NotBlank String name, String description) {}
