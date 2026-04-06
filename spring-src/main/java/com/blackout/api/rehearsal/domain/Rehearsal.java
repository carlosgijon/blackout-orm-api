package com.blackout.api.rehearsal.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;
import java.util.*;

@Entity @Table(name = "rehearsals")
public class Rehearsal {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String date;
    private String notes;
    private String status = "COMPLETED";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rehearsal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RehearsalSong> songs = new ArrayList<>();

    protected Rehearsal() {}
    public Rehearsal(String bandId, String date) { this.bandId = bandId; this.date = date; }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getDate() { return date; }
    public void setDate(String d) { date = d; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
    public Instant getCreatedAt() { return createdAt; }
    public List<RehearsalSong> getSongs() { return songs; }
}
