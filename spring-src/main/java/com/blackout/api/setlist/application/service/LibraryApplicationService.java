package com.blackout.api.setlist.application.service;

import com.blackout.api.setlist.application.port.out.LoadLibrarySongPort;
import com.blackout.api.setlist.application.port.out.LoadPlaylistPort;
import com.blackout.api.setlist.application.port.out.SaveLibrarySongPort;
import com.blackout.api.setlist.domain.LibrarySong;
import com.blackout.api.setlist.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LibraryApplicationService {

    private final LoadLibrarySongPort loadSong;
    private final SaveLibrarySongPort saveSong;
    private final LoadPlaylistPort loadPlaylist;

    public LibraryApplicationService(LoadLibrarySongPort loadSong,
                                      SaveLibrarySongPort saveSong,
                                      LoadPlaylistPort loadPlaylist) {
        this.loadSong = loadSong;
        this.saveSong = saveSong;
        this.loadPlaylist = loadPlaylist;
    }

    public List<LibrarySongResponse> findAll(String bandId) {
        return loadSong.findAllByBandId(bandId).stream()
            .map(LibrarySongResponse::from)
            .toList();
    }

    @Transactional
    public LibrarySongResponse create(String bandId, LibrarySongRequest req) {
        LibrarySong song = new LibrarySong(bandId, req.title(), req.artist());
        song.setAlbum(req.album());
        song.setDuration(req.duration());
        song.setTempo(req.tempo());
        song.setStyle(req.style());
        song.setNotes(req.notes());
        song.setStartNote(req.startNote());
        song.setEndNote(req.endNote());
        return LibrarySongResponse.from(saveSong.save(song));
    }

    @Transactional
    public LibrarySongResponse update(String bandId, String id, LibrarySongRequest req) {
        LibrarySong song = findOwned(bandId, id);
        song.setTitle(req.title());
        song.setArtist(req.artist());
        song.setAlbum(req.album());
        song.setDuration(req.duration());
        song.setTempo(req.tempo());
        song.setStyle(req.style());
        song.setNotes(req.notes());
        song.setStartNote(req.startNote());
        song.setEndNote(req.endNote());
        return LibrarySongResponse.from(saveSong.save(song));
    }

    @Transactional
    public void remove(String bandId, String id) {
        findOwned(bandId, id);
        saveSong.deleteById(id);
    }

    public LibraryStatsResponse getStats(String bandId) {
        List<LibrarySong> songs = loadSong.findAllByBandId(bandId);

        // Build usage count map from a single aggregate query
        Map<String, Integer> usageMap = new HashMap<>();
        for (Object[] row : loadPlaylist.countUsagePerSongInBand(bandId)) {
            usageMap.put((String) row[0], ((Number) row[1]).intValue());
        }

        // byGenre
        Map<String, Integer> byGenre = new LinkedHashMap<>();
        for (LibrarySong s : songs) {
            String g = (s.getStyle() != null && !s.getStyle().isBlank())
                ? s.getStyle().trim() : "Sin género";
            byGenre.merge(g, 1, Integer::sum);
        }

        // songs with usage count
        List<LibraryStatsResponse.SongWithUsage> withCount = songs.stream()
            .map(s -> new LibraryStatsResponse.SongWithUsage(
                s.getId(), s.getTitle(), s.getArtist(), s.getTempo(), s.getDuration(),
                usageMap.getOrDefault(s.getId(), 0)))
            .toList();

        List<LibraryStatsResponse.SongWithUsage> mostUsed = withCount.stream()
            .filter(s -> s.usageCount() > 0)
            .sorted(Comparator.comparingInt(LibraryStatsResponse.SongWithUsage::usageCount).reversed())
            .limit(10)
            .toList();

        List<LibraryStatsResponse.SongWithUsage> neverUsed = withCount.stream()
            .filter(s -> s.usageCount() == 0)
            .toList();

        return new LibraryStatsResponse(
            songs.size(),
            (int) songs.stream().filter(s -> s.getTempo() != null).count(),
            (int) songs.stream().filter(s -> s.getDuration() != null).count(),
            byGenre, mostUsed, neverUsed
        );
    }

    public List<String> getUsage(String bandId, String id) {
        findOwned(bandId, id);
        return loadPlaylist.findPlaylistNamesBySongId(id);
    }

    private LibrarySong findOwned(String bandId, String id) {
        return loadSong.findByIdAndBandId(id, bandId)
            .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
    }
}
