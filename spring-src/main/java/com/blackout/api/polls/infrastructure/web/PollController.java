package com.blackout.api.polls.infrastructure.web;

import com.blackout.api.polls.application.service.PollApplicationService;
import com.blackout.api.polls.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {

    private final PollApplicationService service;

    public PollController(PollApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<PollResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @GetMapping("/{id}")
    public PollResponse get(BlackoutAuthentication auth, @PathVariable String id) {
        return service.get(auth.getBandId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponse create(BlackoutAuthentication auth,
                               @Valid @RequestBody CreatePollRequest req) {
        return service.create(auth.getBandId(), req);
    }

    @PatchMapping("/{id}/status")
    public PollResponse setStatus(BlackoutAuthentication auth,
                                  @PathVariable String id,
                                  @Valid @RequestBody SetStatusRequest req) {
        return service.setStatus(auth.getBandId(), id, req.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(BlackoutAuthentication auth, @PathVariable String id) {
        service.delete(auth.getBandId(), id);
    }

    @PostMapping("/{id}/options")
    public PollResponse addOption(BlackoutAuthentication auth,
                                  @PathVariable String id,
                                  @Valid @RequestBody AddOptionRequest req) {
        return service.addOption(auth.getBandId(), id, req.text(), req.proposedBy());
    }

    @DeleteMapping("/{id}/options/{optionId}")
    public PollResponse deleteOption(BlackoutAuthentication auth,
                                     @PathVariable String id,
                                     @PathVariable String optionId) {
        return service.deleteOption(auth.getBandId(), id, optionId);
    }

    @PostMapping("/{id}/votes")
    public PollResponse castVote(BlackoutAuthentication auth,
                                 @PathVariable String id,
                                 @Valid @RequestBody CastPollVoteRequest req) {
        return service.castVote(auth.getBandId(), id, req);
    }

    @GetMapping("/{id}/results")
    public Object getResults(BlackoutAuthentication auth, @PathVariable String id) {
        return service.getResults(auth.getBandId(), id);
    }
}
