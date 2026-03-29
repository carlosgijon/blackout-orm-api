package com.blackout.api.setlist.application.service;

import com.blackout.api.setlist.application.port.out.LoadLibrarySongPort;
import com.blackout.api.setlist.application.port.out.LoadPlaylistPort;
import com.blackout.api.setlist.application.port.out.SavePlaylistPort;
import com.blackout.api.setlist.domain.LibrarySong;
import com.blackout.api.setlist.domain.Playlist;
import com.blackout.api.setlist.domain.PlaylistSong;
import com.blackout.api.setlist.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaylistApplicationService {

    private final LoadPlaylistPort loadPlaylist;
    private final SavePlaylistPort savePlaylist;
    private final LoadLibrarySongPort loadSong;

    public PlaylistApplicationService(LoadPlaylistPort loadPlaylist,
                                       SavePlaylistPort savePlaylist,
                                       LoadLibrarySongPort loadSong) {
        this.loadPlaylist = loadPlaylist;
        this.savePlaylist = savePlaylist;
        this.loadSong = loadSong;
    }

    public List<PlaylistSummaryResponse> findAll(String bandId) {
        List<Playlist> playlists = loadPlaylist.findAllByBandIdOrderByCreatedAtDesc(bandId);

        // Single query: usage data for ALL playlists in the band at once
        Map<String, Integer> durationMap = new HashMap<>();
        Map<String, Integer> songCountMap = new HashMap<>();

        for (Playlist p : playlists) {
            List<PlaylistSong> songs = loadPlaylist.findSongsByPlaylistId(p.getId());
            int songCount = (int) songs.stream().filter(ps -> "song".equals(ps.getType())).count();
            songCountMap.put(p.getId(), songCount);

            // Collect all songIds, load library songs in batch
            List<String> songIds = songs.stream()
                .map(PlaylistSong::getSongId)
                .filter(Objects::nonNull)
                .toList();
            if (!songIds.isEmpty()) {
                int dur = loadSong.findAllById(songIds).stream()
                    .mapToInt(ls -> ls.getDuration() != null ? ls.getDuration() : 0)
                    .sum();
                durationMap.put(p.getId(), dur);
            }
        }

        return playlists.stream()
            .map(p -> new PlaylistSummaryResponse(
                p.getId(), p.getName(), p.getDescription(), p.getCreatedAt(),
                songCountMap.getOrDefault(p.getId(), 0),
                durationMap.getOrDefault(p.getId(), 0)
            ))
            .toList();
    }

    @Transactional
    public PlaylistResponse create(String bandId, CreatePlaylistRequest req) {
        Playlist p = new Playlist(bandId, req.name());
        p.setDescription(req.description());
        return PlaylistResponse.from(savePlaylist.save(p));
    }

    @Transactional
    public PlaylistResponse update(String bandId, String id, UpdatePlaylistRequest req) {
        Playlist p = findOwned(bandId, id);
        p.setName(req.name());
        p.setDescription(req.description());
        return PlaylistResponse.from(savePlaylist.save(p));
    }

    @Transactional
    public void remove(String bandId, String id) {
        findOwned(bandId, id);
        savePlaylist.deleteById(id);
    }

    public List<PlaylistSongView> getSongs(String bandId, String playlistId) {
        findOwned(bandId, playlistId);
        List<PlaylistSong> entries = loadPlaylist.findSongsByPlaylistId(playlistId);

        // Batch-load library songs in 1 query (no N+1)
        Set<String> songIds = entries.stream()
            .map(PlaylistSong::getSongId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        Map<String, LibrarySong> songMap = loadSong.findAllById(songIds).stream()
            .collect(Collectors.toMap(LibrarySong::getId, s -> s));

        return entries.stream()
            .map(ps -> PlaylistSongView.from(ps, songMap.get(ps.getSongId())))
            .toList();
    }

    @Transactional
    public PlaylistSongView addSong(String bandId, String playlistId, AddSongRequest req) {
        findOwned(bandId, playlistId);
        int lastPos = loadPlaylist.countSongsByPlaylistId(playlistId);

        PlaylistSong entry = new PlaylistSong();
        entry.setPlaylistId(playlistId);
        entry.setSongId(req.songId());
        entry.setPosition(lastPos);
        entry.setType(req.type() != null ? req.type() : "song");
        entry.setTitle(req.title());
        entry.setSetlistName(req.setlistName());
        entry.setJoinWithNext(req.joinWithNext());

        PlaylistSong saved = savePlaylist.saveSong(entry);
        LibrarySong ls = saved.getSongId() != null
            ? loadSong.findAllById(List.of(saved.getSongId())).stream().findFirst().orElse(null)
            : null;
        return PlaylistSongView.from(saved, ls);
    }

    @Transactional
    public PlaylistSongView updateSong(String bandId, String playlistId,
                                        String entryId, UpdateSongRequest req) {
        findOwned(bandId, playlistId);
        List<PlaylistSong> songs = loadPlaylist.findSongsByPlaylistId(playlistId);
        PlaylistSong entry = songs.stream()
            .filter(s -> s.getId().equals(entryId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        entry.setType(req.type());
        entry.setTitle(req.title());
        entry.setSetlistName(req.setlistName());
        entry.setJoinWithNext(req.joinWithNext());

        PlaylistSong saved = savePlaylist.saveSong(entry);
        LibrarySong ls = saved.getSongId() != null
            ? loadSong.findAllById(List.of(saved.getSongId())).stream().findFirst().orElse(null)
            : null;
        return PlaylistSongView.from(saved, ls);
    }

    @Transactional
    public void removeSong(String bandId, String playlistId, String entryId) {
        // Load aggregate, apply domain method (removes + resequences in memory), cascade save
        Playlist playlist = loadPlaylistWithSongs(bandId, playlistId);
        playlist.removeEntry(entryId);
        savePlaylist.save(playlist);
    }

    @Transactional
    public List<PlaylistSongView> reorder(String bandId, String playlistId, List<String> ids) {
        // Load aggregate, apply domain method (reorders in memory), cascade save
        Playlist playlist = loadPlaylistWithSongs(bandId, playlistId);
        playlist.applyOrder(ids);
        savePlaylist.save(playlist);
        return getSongs(bandId, playlistId);
    }

    public List<PlaylistGigSummary> getGigs(String bandId, String playlistId) {
        findOwned(bandId, playlistId);
        return loadPlaylist.findGigsByPlaylistId(bandId, playlistId).stream()
            .map(g -> new PlaylistGigSummary(g.id(), g.title(), g.date(), g.status(), g.venueName()))
            .toList();
    }

    private Playlist loadPlaylistWithSongs(String bandId, String playlistId) {
        Playlist playlist = findOwned(bandId, playlistId);
        // Trigger lazy load of songs inside the @Transactional boundary
        playlist.getSongs().size();
        return playlist;
    }

    private Playlist findOwned(String bandId, String id) {
        return loadPlaylist.findByIdAndBandId(id, bandId)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));
    }
}
