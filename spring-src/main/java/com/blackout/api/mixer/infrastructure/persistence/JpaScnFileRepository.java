package com.blackout.api.mixer.infrastructure.persistence;

import com.blackout.api.mixer.domain.ScnFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaScnFileRepository extends JpaRepository<ScnFile, String> {
    List<ScnFile> findAllByBandIdOrderByCreatedAtDesc(String bandId);
    Optional<ScnFile> findByIdAndBandId(String id, String bandId);
}
