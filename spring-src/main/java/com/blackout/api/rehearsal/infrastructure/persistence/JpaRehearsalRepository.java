package com.blackout.api.rehearsal.infrastructure.persistence;

import com.blackout.api.rehearsal.domain.Rehearsal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface JpaRehearsalRepository extends JpaRepository<Rehearsal, String> {

    List<Rehearsal> findAllByBandIdOrderByDateDesc(String bandId);
}
