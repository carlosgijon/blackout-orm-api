package com.blackout.api.setlist.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_songs")
public class PlaylistSong {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "playlist_id", nullable = false)
    private String playlistId;

    @Column(name = "song_id")
    private String songId;

    @Column(nullable = false)
    private int position;

    private String type = "song";
    private String title;

    @Column(name = "setlist_name")
    private String setlistName;

    @Column(name = "join_with_next", nullable = false)
    private boolean joinWithNext = false;

    protected PlaylistSong() {}

    public String getId() { return id; }
    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }
    public String getSongId() { return songId; }
    public void setSongId(String songId) { this.songId = songId; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSetlistName() { return setlistName; }
    public void setSetlistName(String setlistName) { this.setlistName = setlistName; }
    public boolean isJoinWithNext() { return joinWithNext; }
    public void setJoinWithNext(boolean joinWithNext) { this.joinWithNext = joinWithNext; }
}
