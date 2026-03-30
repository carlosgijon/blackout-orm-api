package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.domain.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface JpaInstrumentRepository extends JpaRepository<Instrument, String> {
    List<Instrument> findAllByBandIdOrderByChannelOrderAsc(String bandId);
    Optional<Instrument> findByIdAndBandId(String id, String bandId);

    @Modifying
    @Query("UPDATE Instrument i SET i.ampId = null WHERE i.ampId = :ampId AND i.bandId = :bandId")
    void clearAmpIdByAmpId(@Param("ampId") String ampId, @Param("bandId") String bandId);

    @Modifying
    @Query("UPDATE Instrument i SET i.ampId = :ampId WHERE i.id = :instrumentId AND i.bandId = :bandId")
    void setAmpId(@Param("ampId") String ampId, @Param("instrumentId") String instrumentId, @Param("bandId") String bandId);
}
