package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.Microphone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface JpaMicrophoneRepository extends JpaRepository<Microphone, String> {
    List<Microphone> findAllByBandId(String bandId);
    Optional<Microphone> findByIdAndBandId(String id, String bandId);

    @Modifying
    @Query("UPDATE Microphone m SET m.assignedToType = null, m.assignedToId = null " +
           "WHERE m.bandId = :bandId AND m.assignedToType = :atype AND m.assignedToId = :aid")
    void clearAssignment(@Param("bandId") String bandId,
                         @Param("atype") String assignedToType,
                         @Param("aid") String assignedToId);

    @Modifying
    @Query("UPDATE Microphone m SET m.assignedToType = :atype, m.assignedToId = :aid " +
           "WHERE m.bandId = :bandId AND m.id IN :ids")
    void bulkAssign(@Param("bandId") String bandId,
                    @Param("atype") String assignedToType,
                    @Param("aid") String assignedToId,
                    @Param("ids") List<String> ids);
}
