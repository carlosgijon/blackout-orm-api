package com.blackout.api.setlist.infrastructure.web;

import com.blackout.api.setlist.application.service.PlaylistApplicationService;
import com.blackout.api.setlist.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@Tag(name = "playlists")
@SecurityRequirement(name = "bearerAuth")
public class PlaylistsController {

    private final PlaylistApplicationService playlistService;

    public PlaylistsController(PlaylistApplicationService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlaylistSummaryResponse> findAll(BlackoutAuthentication auth) {
        requireBand(auth);
        return playlistService.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistResponse create(BlackoutAuthentication auth,
                                    @Valid @RequestBody CreatePlaylistRequest req) {
        requireBand(auth);
        return playlistService.create(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public PlaylistResponse update(BlackoutAuthentication auth, @PathVariable String id,
                                    @Valid @RequestBody UpdatePlaylistRequest req) {
        requireBand(auth);
        return playlistService.update(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        requireBand(auth);
        playlistService.remove(auth.getBandId(), id);
    }

    @GetMapping("/{id}/songs")
    public List<PlaylistSongView> getSongs(BlackoutAuthentication auth, @PathVariable String id) {
        requireBand(auth);
        return playlistService.getSongs(auth.getBandId(), id);
    }

    @PostMapping("/{id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistSongView addSong(BlackoutAuthentication auth, @PathVariable String id,
                                     @RequestBody AddSongRequest req) {
        requireBand(auth);
        return playlistService.addSong(auth.getBandId(), id, req);
    }

    @PutMapping("/{id}/songs/{entryId}")
    public PlaylistSongView updateSong(BlackoutAuthentication auth,
                                        @PathVariable String id,
                                        @PathVariable String entryId,
                                        @RequestBody UpdateSongRequest req) {
        requireBand(auth);
        return playlistService.updateSong(auth.getBandId(), id, entryId, req);
    }

    @DeleteMapping("/{id}/songs/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSong(BlackoutAuthentication auth,
                            @PathVariable String id,
                            @PathVariable String entryId) {
        requireBand(auth);
        playlistService.removeSong(auth.getBandId(), id, entryId);
    }

    @PostMapping("/{id}/reorder")
    public List<PlaylistSongView> reorder(BlackoutAuthentication auth,
                                           @PathVariable String id,
                                           @RequestBody ReorderRequest req) {
        requireBand(auth);
        return playlistService.reorder(auth.getBandId(), id, req.ids());
    }

    @GetMapping("/{id}/gigs")
    public List<PlaylistGigSummary> getGigs(BlackoutAuthentication auth, @PathVariable String id) {
        requireBand(auth);
        return playlistService.getGigs(auth.getBandId(), id);
    }

    private void requireBand(BlackoutAuthentication auth) {
        if (auth.getBandId() == null)
            throw new ForbiddenException("Token sin banda seleccionada.");
    }
}
