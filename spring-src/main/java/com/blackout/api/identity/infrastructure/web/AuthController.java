package com.blackout.api.identity.infrastructure.web;

import com.blackout.api.identity.application.service.AuthApplicationService;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth")
public class AuthController {

    private final AuthApplicationService authService;

    public AuthController(AuthApplicationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req.username(), req.password());
    }

    @PostMapping("/select-band/{bandId}")
    @Operation(summary = "Seleccionar banda activa y obtener token con bandId")
    @SecurityRequirement(name = "bearerAuth")
    public SelectBandResponse selectBand(BlackoutAuthentication auth,
                                          @PathVariable String bandId) {
        return authService.selectBand(auth.getUserId(), bandId);
    }

    @GetMapping("/me")
    @Operation(summary = "Usuario actual + banda activa")
    @SecurityRequirement(name = "bearerAuth")
    public MeResponse me(BlackoutAuthentication auth) {
        return authService.getMe(auth.getUserId(), auth.getBandId());
    }
}
