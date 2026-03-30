package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/amplifiers")
public class AmplifiersController {

    private final EquipmentApplicationService service;

    public AmplifiersController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<AmplifierResponse> findAll(BlackoutAuthentication auth) {
        return service.findAllAmplifiers(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AmplifierResponse create(BlackoutAuthentication auth,
                                    @Valid @RequestBody CreateAmplifierRequest req) {
        return service.createAmplifier(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public AmplifierResponse update(BlackoutAuthentication auth,
                                    @PathVariable String id,
                                    @RequestBody UpdateAmplifierRequest req) {
        return service.updateAmplifier(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeAmplifier(auth.getBandId(), id);
    }

    @PutMapping("/{id}/instrument-link")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInstrumentLink(BlackoutAuthentication auth,
                                     @PathVariable String id,
                                     @RequestBody InstrumentLinkRequest req) {
        service.updateInstrumentLink(auth.getBandId(), id, req.instrumentId());
    }

    @PostMapping("/{id}/mics")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setMics(BlackoutAuthentication auth,
                        @PathVariable String id,
                        @RequestBody SetMicsRequest req) {
        service.setAmplifierMics(auth.getBandId(), id, req.micIds());
    }
}
