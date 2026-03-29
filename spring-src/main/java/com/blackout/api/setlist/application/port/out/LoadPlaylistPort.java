package com.blackout.api.setlist.application.port.out;

import com.blackout.api.setlist.domain.Playlist;
import com.blackout.api.setlist.domain.PlaylistSong;
import java.util.List;
import java.util.Optional;

public interface LoadPlaylistPort {
    List<Playlist> findAllByBandIdOrderByCreatedAtDesc(String bandId);
    Optional<Playlist> findByIdAndBandId(String id, String bandId);
    List<PlaylistSong> findSongsByPlaylistId(String playlistId);
    int countSongsByPlaylistId(String playlistId);

    /** For stats: returns [songId, count] pairs for all playlist songs in band */
    List<Object[]> countUsagePerSongInBand(String bandId);
    /** For usage: returns playlist names that contain a given songId */
    List<String> findPlaylistNamesBySongId(String songId);

    /** For getGigs: gig summaries linked to a playlist */
    List<GigSummary> findGigsByPlaylistId(String bandId, String playlistId);

    record GigSummary(String id, String title, String date, String status, String venueName) {}
}
