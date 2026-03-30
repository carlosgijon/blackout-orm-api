package com.blackout.api.ai.infrastructure.web;

import com.blackout.api.ai.application.service.AiApplicationService;
import com.blackout.api.ai.infrastructure.web.dto.GenerateSetlistRequest;
import com.blackout.api.ai.infrastructure.web.dto.SetlistResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiApplicationService service;

    public AiController(AiApplicationService service) {
        this.service = service;
    }

    @PostMapping("/generate-setlist")
    public SetlistResult generateSetlist(@Valid @RequestBody GenerateSetlistRequest request) {
        return service.generateSetlist(request.songs(), request.preferences());
    }
}
