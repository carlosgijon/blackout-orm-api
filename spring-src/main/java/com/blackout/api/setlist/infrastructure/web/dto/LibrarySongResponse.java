package com.blackout.api.setlist.infrastructure.web.dto;

import com.blackout.api.setlist.domain.LibrarySong;

public record LibrarySongResponse(
    String id, String bandId, String title, String artist,
    String album, Integer duration, Integer tempo,
    String style, String notes, String startNote, String endNote
) {
    public static LibrarySongResponse from(LibrarySong s) {
        return new LibrarySongResponse(s.getId(), s.getBandId(), s.getTitle(), s.getArtist(),
            s.getAlbum(), s.getDuration(), s.getTempo(), s.getStyle(), s.getNotes(),
            s.getStartNote(), s.getEndNote());
    }
}
