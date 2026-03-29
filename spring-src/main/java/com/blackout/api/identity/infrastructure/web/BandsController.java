package com.blackout.api.identity.infrastructure.web;

import com.blackout.api.identity.application.service.BandsApplicationService;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bands")
@Tag(name = "bands")
@SecurityRequirement(name = "bearerAuth")
public class BandsController {

    private final BandsApplicationService bandsService;

    public BandsController(BandsApplicationService bandsService) {
        this.bandsService = bandsService;
    }

    /** Superadmin: list all bands */
    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public List<BandSummaryResponse> findAll() {
        return bandsService.findAll();
    }

    /** Superadmin: create band with initial admin user */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPERADMIN')")
    public CreateBandResponse create(@Valid @RequestBody CreateBandRequest req) {
        return bandsService.create(req);
    }

    /** Superadmin: delete band */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('SUPERADMIN')")
    public void remove(@PathVariable String id) {
        bandsService.remove(id);
    }

    /** Any authenticated user with a bandId: get own band */
    @GetMapping("/mine")
    public BandResponse getMyBand(BlackoutAuthentication auth) {
        requireBandId(auth);
        return bandsService.getMyBand(auth.getBandId());
    }

    /** Admin+: update own band */
    @PutMapping("/mine")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public BandResponse updateMyBand(BlackoutAuthentication auth,
                                      @RequestBody UpdateBandRequest req) {
        requireBandId(auth);
        return bandsService.updateMyBand(auth.getBandId(), req);
    }

    private void requireBandId(BlackoutAuthentication auth) {
        if (auth.getBandId() == null)
            throw new ForbiddenException("Token sin banda seleccionada. Usa /auth/select-band primero.");
    }
}
