package com.blackout.api.setlist.infrastructure.web;

import com.blackout.api.setlist.application.service.LibraryApplicationService;
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
@RequestMapping("/library")
@Tag(name = "library")
@SecurityRequirement(name = "bearerAuth")
public class LibraryController {

    private final LibraryApplicationService libraryService;

    public LibraryController(LibraryApplicationService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<LibrarySongResponse> findAll(BlackoutAuthentication auth) {
        requireBand(auth);
        return libraryService.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibrarySongResponse create(BlackoutAuthentication auth,
                                       @Valid @RequestBody LibrarySongRequest req) {
        requireBand(auth);
        return libraryService.create(auth.getBandId(), req);
    }

    // IMPORTANT: /stats declared before /{id} — Spring prefers exact segments anyway
    @GetMapping("/stats")
    public LibraryStatsResponse getStats(BlackoutAuthentication auth) {
        requireBand(auth);
        return libraryService.getStats(auth.getBandId());
    }

    @GetMapping("/{id}/usage")
    public List<String> getUsage(BlackoutAuthentication auth, @PathVariable String id) {
        requireBand(auth);
        return libraryService.getUsage(auth.getBandId(), id);
    }

    @PutMapping("/{id}")
    public LibrarySongResponse update(BlackoutAuthentication auth,
                                       @PathVariable String id,
                                       @Valid @RequestBody LibrarySongRequest req) {
        requireBand(auth);
        return libraryService.update(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        requireBand(auth);
        libraryService.remove(auth.getBandId(), id);
    }

    private void requireBand(BlackoutAuthentication auth) {
        if (auth.getBandId() == null)
            throw new ForbiddenException("Token sin banda seleccionada.");
    }
}
