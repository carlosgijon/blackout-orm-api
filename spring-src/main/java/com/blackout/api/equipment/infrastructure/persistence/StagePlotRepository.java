package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.StagePlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StagePlotRepository extends JpaRepository<StagePlot, String> {
    Optional<StagePlot> findByBandId(String bandId);
}
