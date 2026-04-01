package com.blackout.api.votes.infrastructure.web;

import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import com.blackout.api.votes.application.service.VotesApplicationService;
import com.blackout.api.votes.infrastructure.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote-sessions")
public class VotesController {

    private final VotesApplicationService service;

    public VotesController(VotesApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<VoteSessionResponse> getSession(
            BlackoutAuthentication auth,
            @RequestParam String playlistId
    ) {
        return service.getSession(auth.getBandId(), playlistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping
    public VoteSessionResponse createSession(
            BlackoutAuthentication auth,
            @RequestBody CreateSessionRequest request
    ) {
        return service.createSession(auth.getBandId(), request.playlistId(), request.title());
    }

    @RequestMapping(value = "/{id}/close", method = {RequestMethod.PUT, RequestMethod.POST})
    public VoteSessionResponse closeSession(
            BlackoutAuthentication auth,
            @PathVariable String id
    ) {
        return service.closeSession(auth.getBandId(), id);
    }

    @RequestMapping(value = "/{id}/reopen", method = {RequestMethod.PUT, RequestMethod.POST})
    public VoteSessionResponse reopenSession(
            BlackoutAuthentication auth,
            @PathVariable String id
    ) {
        return service.reopenSession(auth.getBandId(), id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            BlackoutAuthentication auth,
            @PathVariable String id
    ) {
        service.deleteSession(auth.getBandId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/votes")
    public VoteResponse castVote(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody CastVoteRequest request
    ) {
        return service.castVote(auth.getBandId(), id, request.voterName(), request.orderedIds());
    }

    @GetMapping("/{id}/results")
    public List<VoteResultEntry> getResults(
            BlackoutAuthentication auth,
            @PathVariable String id
    ) {
        return service.getResults(auth.getBandId(), id);
    }
}
