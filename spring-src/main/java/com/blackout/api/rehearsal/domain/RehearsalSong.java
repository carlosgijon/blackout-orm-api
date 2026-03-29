package com.blackout.api.rehearsal.domain;

import jakarta.persistence.*;

@Entity @Table(name = "rehearsal_songs")
public class RehearsalSong {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "rehearsal_id", nullable = false) private String rehearsalId;
    @Column(name = "song_id", nullable = false) private String songId;
    private String notes;
    private Integer rating;

    protected RehearsalSong() {}
    public RehearsalSong(String rehearsalId, String songId) {
        this.rehearsalId = rehearsalId; this.songId = songId;
    }

    public String getId() { return id; }
    public String getRehearsalId() { return rehearsalId; }
    public String getSongId() { return songId; }
    public void setSongId(String s) { songId = s; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Integer getRating() { return rating; }
    public void setRating(Integer r) { rating = r; }
}
