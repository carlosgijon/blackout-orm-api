package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/microphones")
public class MicrophonesController {

    private final EquipmentApplicationService service;

    public MicrophonesController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<MicrophoneResponse> findAll(BlackoutAuthentication auth) {
        return service.findAllMicrophones(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MicrophoneResponse create(BlackoutAuthentication auth,
                                     @Valid @RequestBody CreateMicrophoneRequest req) {
        return service.createMicrophone(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public MicrophoneResponse update(BlackoutAuthentication auth,
                                     @PathVariable String id,
                                     @RequestBody UpdateMicrophoneRequest req) {
        return service.updateMicrophone(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeMicrophone(auth.getBandId(), id);
    }
}
