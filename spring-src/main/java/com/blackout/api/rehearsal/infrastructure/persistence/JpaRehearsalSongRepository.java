package com.blackout.api.rehearsal.infrastructure.persistence;

import com.blackout.api.rehearsal.domain.RehearsalSong;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaRehearsalSongRepository extends JpaRepository<RehearsalSong, String> {
}
