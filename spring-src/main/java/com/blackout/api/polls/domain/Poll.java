package com.blackout.api.polls.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String title;

    private String description;

    /** "yes_no" | "approval" | "proposal" */
    @Column(nullable = false)
    private String type;

    /** "draft" | "open" | "closed" | "archived" */
    @Column(nullable = false)
    private String status = "draft";

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    private Instant deadline;

    @Column(name = "linked_gig_id")
    private String linkedGigId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    @OrderBy("createdAt ASC")
    private List<PollOption> options = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    @OrderBy("createdAt ASC")
    private List<PollVote> votes = new ArrayList<>();

    protected Poll() {}

    public Poll(String bandId, String title, String type, String createdBy) {
        this.bandId = bandId;
        this.title = title;
        this.type = type;
        this.createdBy = createdBy;
    }

    public String getId()          { return id; }
    public String getBandId()      { return bandId; }
    public String getTitle()       { return title; }
    public void   setTitle(String t) { title = t; }
    public String getDescription() { return description; }
    public void   setDescription(String d) { description = d; }
    public String getType()        { return type; }
    public String getStatus()      { return status; }
    public void   setStatus(String s) { status = s; }
    public String getCreatedBy()   { return createdBy; }
    public Instant getDeadline()    { return deadline; }
    public void   setDeadline(Instant d) { deadline = d; }
    public String getLinkedGigId() { return linkedGigId; }
    public void   setLinkedGigId(String g) { linkedGigId = g; }
    public Instant getCreatedAt()  { return createdAt; }
    public List<PollOption> getOptions() { return options; }
    public List<PollVote>   getVotes()   { return votes; }
}
