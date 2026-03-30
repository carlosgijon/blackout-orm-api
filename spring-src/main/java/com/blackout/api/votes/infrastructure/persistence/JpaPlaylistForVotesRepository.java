package com.blackout.api.votes.infrastructure.persistence;

import com.blackout.api.setlist.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaPlaylistForVotesRepository extends JpaRepository<Playlist, String> {
    boolean existsByIdAndBandId(String id, String bandId);
}
