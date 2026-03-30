package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pa")
public class PaEquipmentController {

    private final EquipmentApplicationService service;

    public PaEquipmentController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<PaEquipmentResponse> findAll(BlackoutAuthentication auth) {
        return service.findAllPa(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaEquipmentResponse create(BlackoutAuthentication auth,
                                      @Valid @RequestBody CreatePaRequest req) {
        return service.createPa(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public PaEquipmentResponse update(BlackoutAuthentication auth,
                                      @PathVariable String id,
                                      @RequestBody UpdatePaRequest req) {
        return service.updatePa(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.removePa(auth.getBandId(), id);
    }
}
