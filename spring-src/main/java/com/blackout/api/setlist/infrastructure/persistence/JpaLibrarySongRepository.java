package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.setlist.domain.LibrarySong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface JpaLibrarySongRepository extends JpaRepository<LibrarySong, String> {
    List<LibrarySong> findAllByBandIdOrderByTitleAsc(String bandId);
    Optional<LibrarySong> findByIdAndBandId(String id, String bandId);
}
