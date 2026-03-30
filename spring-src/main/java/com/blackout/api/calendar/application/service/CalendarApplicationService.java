package com.blackout.api.calendar.application.service;

import com.blackout.api.calendar.application.port.out.LoadCalendarPort;
import com.blackout.api.calendar.application.port.out.SaveCalendarPort;
import com.blackout.api.calendar.domain.CalendarEvent;
import com.blackout.api.calendar.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CalendarApplicationService {

    private final LoadCalendarPort loadCalendar;
    private final SaveCalendarPort saveCalendar;

    public CalendarApplicationService(LoadCalendarPort loadCalendar, SaveCalendarPort saveCalendar) {
        this.loadCalendar = loadCalendar;
        this.saveCalendar = saveCalendar;
    }

    public List<CalendarEventResponse> findAll(String bandId) {
        return loadCalendar.findAllByBandId(bandId)
                .stream().map(this::toResponse).toList();
    }

    public List<CalendarEventResponse> getByMonth(String bandId, int year, int month) {
        String yearMonth = String.format("%d-%02d", year, month);
        return loadCalendar.findByBandIdAndMonth(bandId, yearMonth)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CalendarEventResponse create(String bandId, CreateCalendarEventRequest dto) {
        CalendarEvent event = new CalendarEvent(bandId, dto.type(), dto.title(), dto.date());
        event.setEndDate(dto.endDate());
        event.setNotes(dto.notes());
        // color field not present in entity — ignored
        return toResponse(saveCalendar.save(event));
    }

    @Transactional
    public CalendarEventResponse update(String bandId, String id, UpdateCalendarEventRequest dto) {
        CalendarEvent event = loadCalendar.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent not found: " + id));

        if (dto.title() != null) event.setTitle(dto.title());
        if (dto.type() != null) event.setType(dto.type());
        if (dto.date() != null) event.setDate(dto.date());
        if (dto.endDate() != null) event.setEndDate(dto.endDate());
        if (dto.notes() != null) event.setNotes(dto.notes());
        // color field not present in entity — ignored

        return toResponse(saveCalendar.save(event));
    }

    @Transactional
    public void remove(String bandId, String id) {
        loadCalendar.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent not found: " + id));
        saveCalendar.deleteById(id);
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private CalendarEventResponse toResponse(CalendarEvent e) {
        return new CalendarEventResponse(
                e.getId(), e.getBandId(), e.getTitle(), e.getType(),
                e.getDate(), e.getEndDate(), e.getNotes(),
                null,  // color not stored in entity
                e.getCreatedAt());
    }
}
