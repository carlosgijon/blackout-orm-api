package com.blackout.api.gig.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "venues")
public class Venue {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String name;
    private String city;
    private String address;
    private String website;
    private Integer capacity;
    @Column(name = "booking_name") private String bookingName;
    @Column(name = "booking_email") private String bookingEmail;
    @Column(name = "booking_phone") private String bookingPhone;
    private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected Venue() {}
    public Venue(String bandId, String name) { this.bandId = bandId; this.name = name; }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getCity() { return city; }
    public void setCity(String c) { city = c; }
    public String getAddress() { return address; }
    public void setAddress(String a) { address = a; }
    public String getWebsite() { return website; }
    public void setWebsite(String w) { website = w; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer c) { capacity = c; }
    public String getBookingName() { return bookingName; }
    public void setBookingName(String b) { bookingName = b; }
    public String getBookingEmail() { return bookingEmail; }
    public void setBookingEmail(String b) { bookingEmail = b; }
    public String getBookingPhone() { return bookingPhone; }
    public void setBookingPhone(String b) { bookingPhone = b; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Instant getCreatedAt() { return createdAt; }
}
