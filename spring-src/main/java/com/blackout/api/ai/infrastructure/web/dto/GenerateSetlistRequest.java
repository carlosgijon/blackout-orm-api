package com.blackout.api.ai.infrastructure.web.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record GenerateSetlistRequest(
        @NotEmpty List<SongInput> songs,
        String preferences
) {}
