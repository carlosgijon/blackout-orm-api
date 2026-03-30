package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.BandMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaBandMemberRepository extends JpaRepository<BandMember, String> {
    List<BandMember> findAllByBandIdOrderBySortOrderAsc(String bandId);
    Optional<BandMember> findByIdAndBandId(String id, String bandId);
}
