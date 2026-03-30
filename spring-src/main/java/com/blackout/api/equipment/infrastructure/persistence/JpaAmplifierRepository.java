package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.Amplifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaAmplifierRepository extends JpaRepository<Amplifier, String> {
    List<Amplifier> findAllByBandId(String bandId);
    Optional<Amplifier> findByIdAndBandId(String id, String bandId);
}
