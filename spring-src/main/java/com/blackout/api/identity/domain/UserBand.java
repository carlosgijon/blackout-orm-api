package com.blackout.api.identity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user_bands",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "band_id"}))
public class UserBand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String role = "member";

    protected UserBand() {}

    public UserBand(String userId, String bandId, String role) {
        this.userId = userId;
        this.bandId = bandId;
        this.role = role;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getBandId() { return bandId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
