package com.blackout.api.bpm.infrastructure.web;

import com.blackout.api.bpm.application.service.BpmApplicationService;
import com.blackout.api.bpm.infrastructure.web.dto.BpmLookupRequest;
import com.blackout.api.bpm.infrastructure.web.dto.BpmLookupResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bpm")
public class BpmController {

    private final BpmApplicationService service;

    public BpmController(BpmApplicationService service) {
        this.service = service;
    }

    @PostMapping("/lookup")
    public BpmLookupResponse lookup(@Valid @RequestBody BpmLookupRequest request) {
        Integer bpm = service.lookup(request.title(), request.artist());
        return new BpmLookupResponse(bpm);
    }
}
