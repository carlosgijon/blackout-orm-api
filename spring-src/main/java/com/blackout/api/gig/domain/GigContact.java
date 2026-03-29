package com.blackout.api.gig.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "gig_contacts")
public class GigContact {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "gig_id", nullable = false) private String gigId;
    @Column(nullable = false) private String date;
    @Column(name = "contact_type", nullable = false) private String contactType;
    private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected GigContact() {}
    public GigContact(String gigId, String date, String contactType) {
        this.gigId = gigId; this.date = date; this.contactType = contactType;
    }

    public String getId() { return id; }
    public String getGigId() { return gigId; }
    public String getDate() { return date; }
    public void setDate(String d) { date = d; }
    public String getContactType() { return contactType; }
    public void setContactType(String ct) { contactType = ct; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Instant getCreatedAt() { return createdAt; }
}
