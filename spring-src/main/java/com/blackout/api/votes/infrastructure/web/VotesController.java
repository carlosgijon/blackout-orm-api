package com.blackout.api.votes.infrastructure.web;

import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import com.blackout.api.votes.application.service.VotesApplicationService;
import com.blackout.api.votes.infrastructure.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VotesController {

    private final VotesApplicationService service;

    public VotesController(VotesApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{playlistId}/session")
    public ResponseEntity<VoteSessionResponse> getSession(
            BlackoutAuthentication auth,
            @PathVariable String playlistId
    ) {
        return service.getSession(auth.getBandId(), playlistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping("/{playlistId}/session")
    public VoteSessionResponse createSession(
            BlackoutAuthentication auth,
            @PathVariable String playlistId,
            @Valid @RequestBody CreateSessionRequest request
    ) {
        return service.createSession(auth.getBandId(), playlistId, request.title());
    }

    @PostMapping("/sessions/{sessionId}/close")
    public VoteSessionResponse closeSession(
            BlackoutAuthentication auth,
            @PathVariable String sessionId
    ) {
        return service.closeSession(auth.getBandId(), sessionId);
    }

    @PostMapping("/sessions/{sessionId}/reopen")
    public VoteSessionResponse reopenSession(
            BlackoutAuthentication auth,
            @PathVariable String sessionId
    ) {
        return service.reopenSession(auth.getBandId(), sessionId);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            BlackoutAuthentication auth,
            @PathVariable String sessionId
    ) {
        service.deleteSession(auth.getBandId(), sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sessions/{sessionId}/cast")
    public VoteResponse castVote(
            BlackoutAuthentication auth,
            @PathVariable String sessionId,
            @Valid @RequestBody CastVoteRequest request
    ) {
        return service.castVote(auth.getBandId(), sessionId, request.voterName(), request.orderedIds());
    }

    @GetMapping("/sessions/{sessionId}/results")
    public List<VoteResultEntry> getResults(
            BlackoutAuthentication auth,
            @PathVariable String sessionId
    ) {
        return service.getResults(auth.getBandId(), sessionId);
    }
}
