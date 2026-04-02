package com.blackout.api.polls.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "poll_options")
public class PollOption {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "poll_id", nullable = false)
    private String pollId;

    @Column(nullable = false)
    private String text;

    @Column(name = "proposed_by")
    private String proposedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected PollOption() {}

    public PollOption(String pollId, String text, String proposedBy) {
        this.pollId = pollId;
        this.text = text;
        this.proposedBy = proposedBy;
    }

    public String  getId()         { return id; }
    public String  getPollId()     { return pollId; }
    public String  getText()       { return text; }
    public String  getProposedBy() { return proposedBy; }
    public Instant getCreatedAt()  { return createdAt; }
}
