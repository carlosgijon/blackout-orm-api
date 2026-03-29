package com.blackout.api.merch.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "merch_waiting_list")
public class MerchWaitingEntry {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "item_id", nullable = false) private String itemId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private int quantity = 1;
    private String size;
    private String contact;
    private String notes;
    @Column(nullable = false) private String status = "waiting";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected MerchWaitingEntry() {}
    public MerchWaitingEntry(String bandId, String itemId, String name) {
        this.bandId = bandId; this.itemId = itemId; this.name = name;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { quantity = q; }
    public String getSize() { return size; }
    public void setSize(String s) { size = s; }
    public String getContact() { return contact; }
    public void setContact(String c) { contact = c; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
    public Instant getCreatedAt() { return createdAt; }
}
