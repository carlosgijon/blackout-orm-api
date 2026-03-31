package com.blackout.api.votes.application.port.out;

import com.blackout.api.votes.domain.VoteSession;
import java.util.Optional;

public interface LoadVoteSessionPort {
    Optional<VoteSession> findLatestByBandIdAndPlaylistId(String bandId, String playlistId);
    Optional<VoteSession> findByIdAndBandId(String id, String bandId);
    Optional<VoteSession> findByIdWithVotes(String id);
    boolean existsPlaylistByIdAndBandId(String playlistId, String bandId);
}
