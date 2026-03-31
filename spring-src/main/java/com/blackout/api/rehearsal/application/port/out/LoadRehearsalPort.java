package com.blackout.api.rehearsal.application.port.out;

import com.blackout.api.rehearsal.domain.Rehearsal;

import java.util.List;
import java.util.Optional;

public interface LoadRehearsalPort {
    List<Rehearsal> findAllByBandIdOrderByDateDesc(String bandId);
    Optional<Rehearsal> findByIdAndBandId(String id, String bandId);
    String findLibrarySongTitle(String songId);
}
