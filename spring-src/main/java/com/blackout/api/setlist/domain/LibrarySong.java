package com.blackout.api.setlist.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "library_songs")
public class LibrarySong {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;
    private Integer duration;
    private Integer tempo;
    private String style;
    private String notes;

    @Column(name = "start_note")
    private String startNote;

    @Column(name = "end_note")
    private String endNote;

    private String status = "READY";

    protected LibrarySong() {}

    public LibrarySong(String bandId, String title, String artist) {
        this.bandId = bandId;
        this.title = title;
        this.artist = artist;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getTempo() { return tempo; }
    public void setTempo(Integer tempo) { this.tempo = tempo; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStartNote() { return startNote; }
    public void setStartNote(String startNote) { this.startNote = startNote; }
    public String getEndNote() { return endNote; }
    public void setEndNote(String endNote) { this.endNote = endNote; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
