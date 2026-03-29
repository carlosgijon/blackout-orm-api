package com.blackout.api.calendar.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "calendar_events")
public class CalendarEvent {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "member_id") private String memberId;
    @Column(nullable = false) private String type;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String date;
    @Column(name = "end_date") private String endDate;
    @Column(name = "all_day", nullable = false) private boolean allDay = true;
    private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected CalendarEvent() {}
    public CalendarEvent(String bandId, String type, String title, String date) {
        this.bandId = bandId; this.type = type; this.title = title; this.date = date;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String m) { memberId = m; }
    public String getType() { return type; }
    public void setType(String t) { type = t; }
    public String getTitle() { return title; }
    public void setTitle(String t) { title = t; }
    public String getDate() { return date; }
    public void setDate(String d) { date = d; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String e) { endDate = e; }
    public boolean isAllDay() { return allDay; }
    public void setAllDay(boolean a) { allDay = a; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Instant getCreatedAt() { return createdAt; }
}
