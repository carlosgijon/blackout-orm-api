package com.blackout.api.finance.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String type;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private double amount;
    @Column(nullable = false) private String date;
    private String description;
    @Column(name = "gig_id") private String gigId;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected Transaction() {}

    public Transaction(String bandId, String type, String category, double amount, String date) {
        this.bandId = bandId; this.type = type; this.category = category;
        this.amount = amount; this.date = date;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getType() { return type; }
    public void setType(String t) { type = t; }
    public String getCategory() { return category; }
    public void setCategory(String c) { category = c; }
    public double getAmount() { return amount; }
    public void setAmount(double a) { amount = a; }
    public String getDate() { return date; }
    public void setDate(String d) { date = d; }
    public String getDescription() { return description; }
    public void setDescription(String d) { description = d; }
    public String getGigId() { return gigId; }
    public void setGigId(String g) { gigId = g; }
    public Instant getCreatedAt() { return createdAt; }
}
