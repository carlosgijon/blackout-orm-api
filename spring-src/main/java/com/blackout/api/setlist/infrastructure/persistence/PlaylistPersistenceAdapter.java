package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.setlist.application.port.out.LoadPlaylistPort;
import com.blackout.api.setlist.application.port.out.SavePlaylistPort;
import com.blackout.api.setlist.domain.Playlist;
import com.blackout.api.setlist.domain.PlaylistSong;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class PlaylistPersistenceAdapter implements LoadPlaylistPort, SavePlaylistPort {

    private final JpaPlaylistRepository playlists;
    private final JpaPlaylistSongRepository playlistSongs;
    private final JpaGigForPlaylistRepository gigs;

    public PlaylistPersistenceAdapter(JpaPlaylistRepository playlists,
                                       JpaPlaylistSongRepository playlistSongs,
                                       JpaGigForPlaylistRepository gigs) {
        this.playlists = playlists;
        this.playlistSongs = playlistSongs;
        this.gigs = gigs;
    }

    // LoadPlaylistPort
    @Override public List<Playlist> findAllByBandIdOrderByCreatedAtDesc(String bandId) {
        return playlists.findAllByBandIdOrderByCreatedAtDesc(bandId);
    }
    @Override public Optional<Playlist> findByIdAndBandId(String id, String bandId) {
        return playlists.findByIdAndBandId(id, bandId);
    }
    @Override public List<PlaylistSong> findSongsByPlaylistId(String playlistId) {
        return playlistSongs.findByPlaylistIdOrderByPositionAsc(playlistId);
    }
    @Override public int countSongsByPlaylistId(String playlistId) {
        return playlistSongs.countByPlaylistId(playlistId);
    }
    @Override public List<Object[]> countUsagePerSongInBand(String bandId) {
        return playlistSongs.countUsagePerSongInBand(bandId);
    }
    @Override public List<String> findPlaylistNamesBySongId(String songId) {
        return playlistSongs.findPlaylistNamesBySongId(songId);
    }
    @Override public List<GigSummary> findGigsByPlaylistId(String bandId, String playlistId) {
        return gigs.findGigSummariesByPlaylist(bandId, playlistId).stream()
            .map(row -> new GigSummary(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4]
            ))
            .toList();
    }

    // SavePlaylistPort
    @Override public Playlist save(Playlist p) { return playlists.save(p); }
    @Override public void deleteById(String id) { playlists.deleteById(id); }
    @Override public PlaylistSong saveSong(PlaylistSong e) { return playlistSongs.save(e); }
    @Override public List<PlaylistSong> saveAllSongs(List<PlaylistSong> entries) {
        return playlistSongs.saveAll(entries);
    }
    @Override public void deleteSongById(String entryId) { playlistSongs.deleteById(entryId); }
}
