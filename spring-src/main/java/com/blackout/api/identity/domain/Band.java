package com.blackout.api.identity.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bands")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Band() {}

    public Band(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    // Getters & setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public Instant getCreatedAt() { return createdAt; }
}
