package com.blackout.api.mixer.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "scn_files")
public class ScnFile {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String notes;

    @Column(name = "gig_id")
    private String gigId;

    private String venue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected ScnFile() {}

    public ScnFile(String bandId, String name, String content) {
        this.bandId  = bandId;
        this.name    = name;
        this.content = content;
    }

    public String  getId()        { return id; }
    public String  getBandId()    { return bandId; }
    public String  getName()      { return name; }
    public void    setName(String n) { name = n; }
    public String  getContent()   { return content; }
    public void    setContent(String c) { content = c; }
    public String  getNotes()     { return notes; }
    public void    setNotes(String n) { notes = n; }
    public String  getGigId()     { return gigId; }
    public void    setGigId(String g) { gigId = g; }
    public String  getVenue()     { return venue; }
    public void    setVenue(String v) { venue = v; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void    setUpdatedAt(Instant t) { updatedAt = t; }
}
