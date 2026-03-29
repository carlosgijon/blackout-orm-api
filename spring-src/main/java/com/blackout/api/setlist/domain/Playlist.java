package com.blackout.api.setlist.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "playlistId", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PlaylistSong> songs = new ArrayList<>();

    protected Playlist() {}

    public Playlist(String bandId, String name) {
        this.bandId = bandId;
        this.name = name;
    }

    /** Domain method: remove an entry and resequence positions in memory */
    public void removeEntry(String entryId) {
        songs.removeIf(s -> s.getId().equals(entryId));
        resequence();
    }

    /** Domain method: apply a new order given an ordered list of ids */
    public void applyOrder(List<String> orderedIds) {
        Map<String, PlaylistSong> index = new HashMap<>();
        for (PlaylistSong s : songs) index.put(s.getId(), s);
        for (int i = 0; i < orderedIds.size(); i++) {
            PlaylistSong s = index.get(orderedIds.get(i));
            if (s != null) s.setPosition(i + 1);
        }
    }

    private void resequence() {
        songs.sort(Comparator.comparingInt(PlaylistSong::getPosition));
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setPosition(i + 1);
        }
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public List<PlaylistSong> getSongs() { return songs; }
}
