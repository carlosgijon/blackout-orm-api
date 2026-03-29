package com.blackout.api.setlist.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LibrarySongRequest(
    @NotBlank String title,
    @NotBlank String artist,
    String album, Integer duration, Integer tempo,
    String style, String notes, String startNote, String endNote
) {}
