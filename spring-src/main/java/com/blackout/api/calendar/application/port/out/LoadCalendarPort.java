package com.blackout.api.calendar.application.port.out;

import com.blackout.api.calendar.domain.CalendarEvent;

import java.util.List;
import java.util.Optional;

public interface LoadCalendarPort {
    List<CalendarEvent> findAllByBandId(String bandId);
    List<CalendarEvent> findByBandIdAndMonth(String bandId, String yearMonth);
    Optional<CalendarEvent> findByIdAndBandId(String id, String bandId);
}
