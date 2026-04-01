package com.blackout.api.settings.infrastructure.web;

import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import com.blackout.api.settings.application.service.SettingsApplicationService;
import com.blackout.api.settings.infrastructure.web.dto.SettingsResponse;
import com.blackout.api.settings.infrastructure.web.dto.UpdateSettingsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsApplicationService service;

    public SettingsController(SettingsApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public SettingsResponse getSettings(BlackoutAuthentication auth) {
        return service.getSettings(auth.getBandId());
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Void> updateSettings(
            BlackoutAuthentication auth,
            @RequestBody UpdateSettingsRequest request
    ) {
        service.updateSettings(auth.getBandId(), request);
        return ResponseEntity.noContent().build();
    }
}
