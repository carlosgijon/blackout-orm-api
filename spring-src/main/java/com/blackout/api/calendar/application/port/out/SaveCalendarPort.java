package com.blackout.api.calendar.application.port.out;

import com.blackout.api.calendar.domain.CalendarEvent;

public interface SaveCalendarPort {
    CalendarEvent save(CalendarEvent e);
    void deleteById(String id);
}
