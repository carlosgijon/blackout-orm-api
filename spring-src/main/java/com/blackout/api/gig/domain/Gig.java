package com.blackout.api.gig.domain;

import com.blackout.api.gig.domain.event.GigMarkedAsCobrado;
import jakarta.persistence.*;
import org.springframework.context.ApplicationEventPublisher;
import java.time.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name = "gigs")
public class Gig {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "venue_id") private String venueId;
    @Column(name = "setlist_id") private String setlistId;
    @Column(nullable = false) private String title;
    private String date;
    private String time;
    @Column(nullable = false) private String status = "lead";
    private String pay;
    @Column(name = "load_in_time") private String loadInTime;
    @Column(name = "soundcheck_time") private String soundcheckTime;
    @Column(name = "set_time") private String setTime;
    private String notes;
    private Integer attendance;
    @Column(name = "follow_up_date") private String followUpDate;
    @Column(name = "follow_up_note") private String followUpNote;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected Gig() {}
    public Gig(String bandId, String title) { this.bandId = bandId; this.title = title; }

    /**
     * Domain method: change status and publish side-effect events.
     * Mirrors NestJS GigsService.updateStatus() business logic exactly.
     */
    public void changeStatus(String newStatus, ApplicationEventPublisher events) {
        boolean wasPlayed = "played".equals(this.status);
        boolean wasCobrado = "cobrado".equals(this.status);

        if ("played".equals(newStatus) && !wasPlayed) {
            LocalDate nextMonthFirst = LocalDate.now().plusMonths(1).withDayOfMonth(1);
            this.followUpDate = nextMonthFirst.toString();
            this.followUpNote = "Recordatorio: cobrar este concierto";
        }
        if ("cobrado".equals(newStatus) && !wasCobrado) {
            events.publishEvent(new GigMarkedAsCobrado(this.id, this.bandId, this.title, this.pay));
            this.followUpDate = null;
            this.followUpNote = null;
        }
        this.status = newStatus;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getVenueId() { return venueId; }
    public void setVenueId(String v) { venueId = v; }
    public String getSetlistId() { return setlistId; }
    public void setSetlistId(String s) { setlistId = s; }
    public String getTitle() { return title; }
    public void setTitle(String t) { title = t; }
    public String getDate() { return date; }
    public void setDate(String d) { date = d; }
    public String getTime() { return time; }
    public void setTime(String t) { time = t; }
    public String getStatus() { return status; }
    public String getPay() { return pay; }
    public void setPay(String p) { pay = p; }
    public String getLoadInTime() { return loadInTime; }
    public void setLoadInTime(String l) { loadInTime = l; }
    public String getSoundcheckTime() { return soundcheckTime; }
    public void setSoundcheckTime(String s) { soundcheckTime = s; }
    public String getSetTime() { return setTime; }
    public void setSetTime(String s) { setTime = s; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Integer getAttendance() { return attendance; }
    public void setAttendance(Integer a) { attendance = a; }
    public String getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(String f) { followUpDate = f; }
    public String getFollowUpNote() { return followUpNote; }
    public void setFollowUpNote(String f) { followUpNote = f; }
    public Instant getCreatedAt() { return createdAt; }
}
