package com.blackout.api.rehearsal.application.service;

import com.blackout.api.rehearsal.application.port.out.LoadRehearsalPort;
import com.blackout.api.rehearsal.application.port.out.SaveRehearsalPort;
import com.blackout.api.rehearsal.domain.Rehearsal;
import com.blackout.api.rehearsal.domain.RehearsalSong;
import com.blackout.api.rehearsal.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RehearsalApplicationService {

    private final LoadRehearsalPort loadRehearsal;
    private final SaveRehearsalPort saveRehearsal;

    public RehearsalApplicationService(LoadRehearsalPort loadRehearsal, SaveRehearsalPort saveRehearsal) {
        this.loadRehearsal = loadRehearsal;
        this.saveRehearsal = saveRehearsal;
    }

    public List<RehearsalResponse> findAll(String bandId) {
        return loadRehearsal.findAllByBandIdOrderByDateDesc(bandId)
                .stream().map(this::toResponse).toList();
    }

    public RehearsalResponse findOne(String bandId, String id) {
        Rehearsal r = loadRehearsal.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + id));
        return toResponse(r);
    }

    @Transactional
    public RehearsalResponse create(String bandId, CreateRehearsalRequest dto) {
        Rehearsal r = new Rehearsal(bandId, dto.date());
        r.setNotes(dto.notes());
        if (dto.status() != null) r.setStatus(dto.status());
        return toResponse(saveRehearsal.save(r));
    }

    @Transactional
    public RehearsalResponse update(String bandId, String id, UpdateRehearsalRequest dto) {
        Rehearsal r = loadRehearsal.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + id));
        if (dto.date() != null) r.setDate(dto.date());
        if (dto.notes() != null) r.setNotes(dto.notes());
        if (dto.status() != null) r.setStatus(dto.status());
        return toResponse(saveRehearsal.save(r));
    }

    @Transactional
    public void remove(String bandId, String id) {
        loadRehearsal.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + id));
        saveRehearsal.deleteById(id);
    }

    @Transactional
    public RehearsalSongResponse addSong(String bandId, String rehearsalId, AddRehearsalSongRequest dto) {
        loadRehearsal.findByIdAndBandId(rehearsalId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + rehearsalId));

        String songId = dto.songId() != null ? dto.songId() : "";
        RehearsalSong song = new RehearsalSong(rehearsalId, songId);
        song.setNotes(dto.notes());
        song.setRating(dto.rating());

        return toSongResponse(saveRehearsal.saveSong(song));
    }

    @Transactional
    public RehearsalSongResponse updateSong(String bandId, String rehearsalId, String songId, UpdateRehearsalSongRequest dto) {
        Rehearsal r = loadRehearsal.findByIdAndBandId(rehearsalId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + rehearsalId));

        RehearsalSong song = r.getSongs().stream()
                .filter(s -> s.getId().equals(songId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("RehearsalSong not found: " + songId));

        if (dto.notes() != null) song.setNotes(dto.notes());
        if (dto.rating() != null) song.setRating(dto.rating());

        return toSongResponse(saveRehearsal.saveSong(song));
    }

    @Transactional
    public void removeSong(String bandId, String rehearsalId, String songId) {
        Rehearsal r = loadRehearsal.findByIdAndBandId(rehearsalId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal not found: " + rehearsalId));

        boolean exists = r.getSongs().stream().anyMatch(s -> s.getId().equals(songId));
        if (!exists) throw new ResourceNotFoundException("RehearsalSong not found: " + songId);
        saveRehearsal.deleteSongById(songId);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private RehearsalResponse toResponse(Rehearsal r) {
        List<RehearsalSongResponse> songs = r.getSongs().stream()
                .map(this::toSongResponse).toList();
        return new RehearsalResponse(
                r.getId(), r.getDate(), r.getNotes(), r.getStatus(),
                r.getCreatedAt().toString(), songs);
    }

    private RehearsalSongResponse toSongResponse(RehearsalSong s) {
        String[] info = loadRehearsal.findLibrarySongInfo(s.getSongId());
        Integer tempo = info[2] != null ? Integer.parseInt(info[2]) : null;
        return new RehearsalSongResponse(
                s.getId(), s.getSongId(),
                info[0], info[1], tempo, info[3],
                s.getNotes(), s.getRating());
    }
}
