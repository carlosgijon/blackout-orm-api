package com.blackout.api.setlist.application.port.out;

import com.blackout.api.setlist.domain.Playlist;
import com.blackout.api.setlist.domain.PlaylistSong;
import java.util.List;

public interface SavePlaylistPort {
    Playlist save(Playlist playlist);
    void deleteById(String id);
    PlaylistSong saveSong(PlaylistSong entry);
    List<PlaylistSong> saveAllSongs(List<PlaylistSong> entries);
    void deleteSongById(String entryId);
}
