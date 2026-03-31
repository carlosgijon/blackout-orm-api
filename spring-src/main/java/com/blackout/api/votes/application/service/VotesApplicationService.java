package com.blackout.api.votes.application.service;

import com.blackout.api.shared.domain.BadRequestException;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import com.blackout.api.votes.application.port.out.LoadVoteSessionPort;
import com.blackout.api.votes.application.port.out.SaveVoteSessionPort;
import com.blackout.api.votes.domain.Vote;
import com.blackout.api.votes.domain.VoteSession;
import com.blackout.api.votes.infrastructure.web.dto.VoteResponse;
import com.blackout.api.votes.infrastructure.web.dto.VoteResultEntry;
import com.blackout.api.votes.infrastructure.web.dto.VoteSessionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VotesApplicationService {

    private final LoadVoteSessionPort loadVoteSession;
    private final SaveVoteSessionPort saveVoteSession;
    private final ObjectMapper objectMapper;

    public VotesApplicationService(
            LoadVoteSessionPort loadVoteSession,
            SaveVoteSessionPort saveVoteSession,
            ObjectMapper objectMapper
    ) {
        this.loadVoteSession = loadVoteSession;
        this.saveVoteSession = saveVoteSession;
        this.objectMapper = objectMapper;
    }

    public Optional<VoteSessionResponse> getSession(String bandId, String playlistId) {
        return loadVoteSession.findLatestByBandIdAndPlaylistId(bandId, playlistId)
                .map(this::toResponse);
    }

    @Transactional
    public VoteSessionResponse createSession(String bandId, String playlistId, String title) {
        if (!loadVoteSession.existsPlaylistByIdAndBandId(playlistId, bandId)) {
            throw new ResourceNotFoundException("Playlist not found");
        }
        VoteSession session = new VoteSession(bandId, playlistId, title);
        return toResponse(saveVoteSession.save(session));
    }

    @Transactional
    public VoteSessionResponse closeSession(String bandId, String sessionId) {
        VoteSession session = loadVoteSession.findByIdAndBandId(sessionId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote session not found"));
        session.setStatus("closed");
        return toResponse(saveVoteSession.save(session));
    }

    @Transactional
    public VoteSessionResponse reopenSession(String bandId, String sessionId) {
        VoteSession session = loadVoteSession.findByIdAndBandId(sessionId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote session not found"));
        session.setStatus("open");
        return toResponse(saveVoteSession.save(session));
    }

    @Transactional
    public void deleteSession(String bandId, String sessionId) {
        VoteSession session = loadVoteSession.findByIdAndBandId(sessionId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote session not found"));
        saveVoteSession.deleteById(session.getId());
    }

    @Transactional
    public VoteResponse castVote(String bandId, String sessionId, String voterName, List<String> orderedIds) {
        VoteSession session = loadVoteSession.findByIdWithVotes(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote session not found"));

        if (!session.getBandId().equals(bandId)) {
            throw new ResourceNotFoundException("Vote session not found");
        }
        if (!"open".equals(session.getStatus())) {
            throw new ForbiddenException("Vote session is not open");
        }

        // Trigger lazy load
        session.getVotes().size();

        try {
            String orderedIdsJson = objectMapper.writeValueAsString(orderedIds);
            Optional<Vote> existing = session.getVotes().stream()
                    .filter(v -> v.getVoterName().equals(voterName))
                    .findFirst();

            Vote vote;
            if (existing.isPresent()) {
                existing.get().setOrderedIds(orderedIdsJson);
                vote = existing.get();
            } else {
                vote = new Vote(sessionId, voterName, orderedIdsJson);
                session.getVotes().add(vote);
            }

            saveVoteSession.save(session);
            return toVoteResponse(vote, orderedIds);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid orderedIds format");
        }
    }

    @Transactional(readOnly = true)
    public List<VoteResultEntry> getResults(String bandId, String sessionId) {
        VoteSession session = loadVoteSession.findByIdWithVotes(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote session not found"));

        if (!session.getBandId().equals(bandId)) {
            throw new ResourceNotFoundException("Vote session not found");
        }

        // Borda count: for each vote, each song at index i gets rank i+1
        // Sum ranks per songId, compute avgRank = sum/count, sort by avgRank ascending
        Map<String, Integer> rankSum = new LinkedHashMap<>();
        Map<String, Integer> voteCount = new LinkedHashMap<>();

        for (Vote vote : session.getVotes()) {
            try {
                List<String> ids = objectMapper.readValue(vote.getOrderedIds(), new TypeReference<>() {});
                for (int i = 0; i < ids.size(); i++) {
                    String songId = ids.get(i);
                    int rank = i + 1;
                    rankSum.merge(songId, rank, Integer::sum);
                    voteCount.merge(songId, 1, Integer::sum);
                }
            } catch (JsonProcessingException e) {
                // skip malformed vote
            }
        }

        return rankSum.entrySet().stream()
                .map(e -> {
                    String songId = e.getKey();
                    int sum = e.getValue();
                    int count = voteCount.get(songId);
                    double avgRank = (double) sum / count;
                    return new VoteResultEntry(songId, avgRank, count);
                })
                .sorted(Comparator.comparingDouble(VoteResultEntry::avgRank))
                .collect(Collectors.toList());
    }

    // --- Mapping helpers ---

    private VoteSessionResponse toResponse(VoteSession session) {
        List<VoteResponse> voteResponses = session.getVotes().stream()
                .map(v -> {
                    List<String> ids = parseOrderedIds(v.getOrderedIds());
                    return toVoteResponse(v, ids);
                })
                .collect(Collectors.toList());

        return new VoteSessionResponse(
                session.getId(),
                session.getBandId(),
                session.getPlaylistId(),
                session.getTitle(),
                session.getStatus(),
                session.getCreatedAt(),
                voteResponses
        );
    }

    private VoteResponse toVoteResponse(Vote vote, List<String> orderedIds) {
        return new VoteResponse(
                vote.getId(),
                vote.getSessionId(),
                vote.getVoterName(),
                orderedIds,
                vote.getCreatedAt()
        );
    }

    private List<String> parseOrderedIds(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}
