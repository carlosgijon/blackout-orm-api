package com.blackout.api.rehearsal.infrastructure.web;

import com.blackout.api.rehearsal.application.service.RehearsalApplicationService;
import com.blackout.api.rehearsal.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rehearsals")
public class RehearsalController {

    private final RehearsalApplicationService service;

    public RehearsalController(RehearsalApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<RehearsalResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @GetMapping("/{id}")
    public RehearsalResponse findOne(BlackoutAuthentication auth, @PathVariable String id) {
        return service.findOne(auth.getBandId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RehearsalResponse create(
            BlackoutAuthentication auth,
            @Valid @RequestBody CreateRehearsalRequest dto) {
        return service.create(auth.getBandId(), dto);
    }

    @PutMapping("/{id}")
    public RehearsalResponse update(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody UpdateRehearsalRequest dto) {
        return service.update(auth.getBandId(), id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.remove(auth.getBandId(), id);
    }

    @PostMapping("/{rehearsalId}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public RehearsalSongResponse addSong(
            BlackoutAuthentication auth,
            @PathVariable String rehearsalId,
            @RequestBody AddRehearsalSongRequest dto) {
        return service.addSong(auth.getBandId(), rehearsalId, dto);
    }

    @PutMapping("/{rehearsalId}/songs/{songId}")
    public RehearsalSongResponse updateSong(
            BlackoutAuthentication auth,
            @PathVariable String rehearsalId,
            @PathVariable String songId,
            @RequestBody UpdateRehearsalSongRequest dto) {
        return service.updateSong(auth.getBandId(), rehearsalId, songId, dto);
    }

    @DeleteMapping("/{rehearsalId}/songs/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSong(
            BlackoutAuthentication auth,
            @PathVariable String rehearsalId,
            @PathVariable String songId) {
        service.removeSong(auth.getBandId(), rehearsalId, songId);
    }
}
