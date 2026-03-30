package com.blackout.api.calendar.infrastructure.persistence;

import com.blackout.api.calendar.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface JpaCalendarRepository extends JpaRepository<CalendarEvent, String> {

    List<CalendarEvent> findAllByBandId(String bandId);

    @Query("SELECT e FROM CalendarEvent e WHERE e.bandId = :bandId AND e.date LIKE :prefix")
    List<CalendarEvent> findByBandIdAndDateStartsWith(
            @Param("bandId") String bandId,
            @Param("prefix") String prefix);
}
