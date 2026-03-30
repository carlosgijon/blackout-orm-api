package com.blackout.api.calendar.infrastructure.persistence;

import com.blackout.api.calendar.application.port.out.LoadCalendarPort;
import com.blackout.api.calendar.application.port.out.SaveCalendarPort;
import com.blackout.api.calendar.domain.CalendarEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class CalendarPersistenceAdapter implements LoadCalendarPort, SaveCalendarPort {

    private final JpaCalendarRepository repo;

    CalendarPersistenceAdapter(JpaCalendarRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CalendarEvent> findAllByBandId(String bandId) {
        return repo.findAllByBandId(bandId);
    }

    @Override
    public List<CalendarEvent> findByBandIdAndMonth(String bandId, String yearMonth) {
        return repo.findByBandIdAndDateStartsWith(bandId, yearMonth + "%");
    }

    @Override
    public Optional<CalendarEvent> findByIdAndBandId(String id, String bandId) {
        return repo.findById(id).filter(e -> e.getBandId().equals(bandId));
    }

    @Override
    public CalendarEvent save(CalendarEvent e) {
        return repo.save(e);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
