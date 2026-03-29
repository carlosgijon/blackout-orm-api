package com.blackout.api.gig.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name = "gig_checklists")
public class GigChecklist {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "gig_id", nullable = false) private String gigId;
    @Column(nullable = false) private String name;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "checklistId", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sort_order ASC")
    private List<ChecklistItem> items = new ArrayList<>();

    protected GigChecklist() {}
    public GigChecklist(String gigId, String name) { this.gigId = gigId; this.name = name; }

    public String getId() { return id; }
    public String getGigId() { return gigId; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public Instant getCreatedAt() { return createdAt; }
    public List<ChecklistItem> getItems() { return items; }
}
