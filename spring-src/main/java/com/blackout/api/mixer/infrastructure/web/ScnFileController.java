package com.blackout.api.mixer.infrastructure.web;

import com.blackout.api.mixer.application.service.ScnFileApplicationService;
import com.blackout.api.mixer.infrastructure.web.dto.ScnFileResponse;
import com.blackout.api.mixer.infrastructure.web.dto.SaveScnFileRequest;
import com.blackout.api.mixer.infrastructure.web.dto.UpdateScnFileRequest;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mixer/scn-files")
public class ScnFileController {

    private final ScnFileApplicationService service;

    public ScnFileController(ScnFileApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScnFileResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @GetMapping("/{id}")
    public ScnFileResponse get(BlackoutAuthentication auth, @PathVariable String id) {
        return service.get(auth.getBandId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScnFileResponse create(BlackoutAuthentication auth,
                                  @Valid @RequestBody SaveScnFileRequest req) {
        return service.create(auth.getBandId(), req);
    }

    @PatchMapping("/{id}")
    public ScnFileResponse update(BlackoutAuthentication auth,
                                  @PathVariable String id,
                                  @RequestBody UpdateScnFileRequest req) {
        return service.update(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(BlackoutAuthentication auth, @PathVariable String id) {
        service.delete(auth.getBandId(), id);
    }
}
