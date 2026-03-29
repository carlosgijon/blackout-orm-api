package com.blackout.api.votes.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name = "vote_sessions")
public class VoteSession {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "playlist_id", nullable = false) private String playlistId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String status = "open";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "sessionId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    protected VoteSession() {}
    public VoteSession(String bandId, String playlistId, String title) {
        this.bandId = bandId; this.playlistId = playlistId; this.title = title;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getPlaylistId() { return playlistId; }
    public String getTitle() { return title; }
    public void setTitle(String t) { title = t; }
    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
    public Instant getCreatedAt() { return createdAt; }
    public List<Vote> getVotes() { return votes; }
}
