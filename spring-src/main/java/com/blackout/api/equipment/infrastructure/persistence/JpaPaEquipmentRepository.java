package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.PaEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaPaEquipmentRepository extends JpaRepository<PaEquipment, String> {
    List<PaEquipment> findAllByBandId(String bandId);
    Optional<PaEquipment> findByIdAndBandId(String id, String bandId);
}
