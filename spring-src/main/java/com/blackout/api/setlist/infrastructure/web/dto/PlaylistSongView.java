package com.blackout.api.setlist.infrastructure.web.dto;

import com.blackout.api.setlist.domain.LibrarySong;
import com.blackout.api.setlist.domain.PlaylistSong;

public record PlaylistSongView(
    String id,
    String playlistId,
    String songId,
    int position,
    String type,
    String title,
    String setlistName,
    boolean joinWithNext,
    String artist,
    String album,
    Integer duration,
    Integer tempo,
    String style,
    String notes
) {
    /** Mirrors NestJS toView() — merges PlaylistSong overrides with LibrarySong fields */
    public static PlaylistSongView from(PlaylistSong ps, LibrarySong ls) {
        String title = ps.getTitle() != null ? ps.getTitle()
                     : (ls != null ? ls.getTitle() : "");
        return new PlaylistSongView(
            ps.getId(),
            ps.getPlaylistId(),
            ps.getSongId(),
            ps.getPosition(),
            ps.getType() != null ? ps.getType() : "song",
            title != null ? title : "",
            ps.getSetlistName(),
            ps.isJoinWithNext(),
            ls != null ? ls.getArtist() : "",
            ls != null ? ls.getAlbum() : null,
            ls != null ? ls.getDuration() : null,
            ls != null ? ls.getTempo() : null,
            ls != null ? ls.getStyle() : null,
            ls != null ? ls.getNotes() : null
        );
    }
}
