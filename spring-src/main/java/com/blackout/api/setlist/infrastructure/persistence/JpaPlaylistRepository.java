package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.setlist.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface JpaPlaylistRepository extends JpaRepository<Playlist, String> {
    List<Playlist> findAllByBandIdOrderByCreatedAtDesc(String bandId);
    Optional<Playlist> findByIdAndBandId(String id, String bandId);
}
