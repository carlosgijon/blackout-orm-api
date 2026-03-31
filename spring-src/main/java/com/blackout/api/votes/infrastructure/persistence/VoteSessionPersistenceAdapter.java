package com.blackout.api.votes.infrastructure.persistence;

import com.blackout.api.votes.application.port.out.LoadVoteSessionPort;
import com.blackout.api.votes.application.port.out.SaveVoteSessionPort;
import com.blackout.api.votes.domain.VoteSession;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class VoteSessionPersistenceAdapter implements LoadVoteSessionPort, SaveVoteSessionPort {

    private final JpaVoteSessionRepository repo;
    private final JpaPlaylistForVotesRepository playlistRepo;

    VoteSessionPersistenceAdapter(JpaVoteSessionRepository repo,
                                   JpaPlaylistForVotesRepository playlistRepo) {
        this.repo = repo;
        this.playlistRepo = playlistRepo;
    }

    @Override
    public Optional<VoteSession> findLatestByBandIdAndPlaylistId(String bandId, String playlistId) {
        return repo.findByBandIdAndPlaylistIdOrderByCreatedAtDesc(bandId, playlistId)
                .stream().findFirst();
    }

    @Override
    public Optional<VoteSession> findByIdAndBandId(String id, String bandId) {
        return repo.findByIdAndBandId(id, bandId);
    }

    @Override
    public Optional<VoteSession> findByIdWithVotes(String id) {
        return repo.findByIdWithVotes(id);
    }

    @Override
    public VoteSession save(VoteSession session) {
        return repo.save(session);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existsPlaylistByIdAndBandId(String playlistId, String bandId) {
        return playlistRepo.existsByIdAndBandId(playlistId, bandId);
    }
}
