package com.blackout.api.votes.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "votes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "voter_name"}))
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "session_id", nullable = false) private String sessionId;
    @Column(name = "voter_name", nullable = false) private String voterName;
    @Column(name = "ordered_ids", nullable = false) private String orderedIds; // JSON array
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected Vote() {}
    public Vote(String sessionId, String voterName, String orderedIds) {
        this.sessionId = sessionId; this.voterName = voterName; this.orderedIds = orderedIds;
    }

    public String getId() { return id; }
    public String getSessionId() { return sessionId; }
    public String getVoterName() { return voterName; }
    public String getOrderedIds() { return orderedIds; }
    public void setOrderedIds(String o) { orderedIds = o; }
    public Instant getCreatedAt() { return createdAt; }
}
