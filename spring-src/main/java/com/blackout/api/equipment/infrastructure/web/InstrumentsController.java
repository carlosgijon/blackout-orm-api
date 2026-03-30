package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentsController {

    private final EquipmentApplicationService service;

    public InstrumentsController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<InstrumentResponse> findAll(BlackoutAuthentication auth) {
        return service.findAllInstruments(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstrumentResponse create(BlackoutAuthentication auth,
                                     @Valid @RequestBody CreateInstrumentRequest req) {
        return service.createInstrument(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public InstrumentResponse update(BlackoutAuthentication auth,
                                     @PathVariable String id,
                                     @RequestBody UpdateInstrumentRequest req) {
        return service.updateInstrument(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeInstrument(auth.getBandId(), id);
    }

    @PostMapping("/{id}/mics")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setMics(BlackoutAuthentication auth,
                        @PathVariable String id,
                        @RequestBody SetMicsRequest req) {
        service.setInstrumentMics(auth.getBandId(), id, req.micIds());
    }
}
