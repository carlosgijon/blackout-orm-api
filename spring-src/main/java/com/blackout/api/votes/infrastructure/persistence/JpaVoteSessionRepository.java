package com.blackout.api.votes.infrastructure.persistence;

import com.blackout.api.votes.domain.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface JpaVoteSessionRepository extends JpaRepository<VoteSession, String> {

    @Query("SELECT s FROM VoteSession s WHERE s.bandId = :bandId AND s.playlistId = :playlistId ORDER BY s.createdAt DESC")
    List<VoteSession> findByBandIdAndPlaylistIdOrderByCreatedAtDesc(
            @Param("bandId") String bandId,
            @Param("playlistId") String playlistId
    );

    Optional<VoteSession> findByIdAndBandId(String id, String bandId);

    @Query("SELECT s FROM VoteSession s LEFT JOIN FETCH s.votes WHERE s.id = :id")
    Optional<VoteSession> findByIdWithVotes(@Param("id") String id);
}
