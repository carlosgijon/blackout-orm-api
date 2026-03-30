package com.blackout.api.calendar.infrastructure.web;

import com.blackout.api.calendar.application.service.CalendarApplicationService;
import com.blackout.api.calendar.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarApplicationService service;

    public CalendarController(CalendarApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CalendarEventResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @GetMapping("/month")
    public List<CalendarEventResponse> getByMonth(
            BlackoutAuthentication auth,
            @RequestParam int year,
            @RequestParam int month) {
        return service.getByMonth(auth.getBandId(), year, month);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarEventResponse create(
            BlackoutAuthentication auth,
            @Valid @RequestBody CreateCalendarEventRequest dto) {
        return service.create(auth.getBandId(), dto);
    }

    @PutMapping("/{id}")
    public CalendarEventResponse update(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody UpdateCalendarEventRequest dto) {
        return service.update(auth.getBandId(), id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.remove(auth.getBandId(), id);
    }
}
