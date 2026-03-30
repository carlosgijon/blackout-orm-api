package com.blackout.api.gig.infrastructure.web;

import com.blackout.api.gig.application.service.VenueApplicationService;
import com.blackout.api.gig.infrastructure.web.dto.CreateVenueRequest;
import com.blackout.api.gig.infrastructure.web.dto.UpdateVenueRequest;
import com.blackout.api.gig.infrastructure.web.dto.VenueResponse;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenuesController {

    private final VenueApplicationService service;

    public VenuesController(VenueApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<VenueResponse> findAll(BlackoutAuthentication auth) {
        requireBand(auth);
        return service.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenueResponse create(@RequestBody CreateVenueRequest req, BlackoutAuthentication auth) {
        requireBand(auth);
        return service.create(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public VenueResponse update(@PathVariable String id,
                                 @RequestBody UpdateVenueRequest req,
                                 BlackoutAuthentication auth) {
        requireBand(auth);
        return service.update(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String id, BlackoutAuthentication auth) {
        requireBand(auth);
        service.remove(auth.getBandId(), id);
    }

    private void requireBand(BlackoutAuthentication auth) {
        if (auth.getBandId() == null) throw new ForbiddenException("Token sin banda seleccionada.");
    }
}
