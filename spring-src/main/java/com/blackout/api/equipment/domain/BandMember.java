package com.blackout.api.equipment.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "band_members")
public class BandMember {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String roles = "[]"; // JSON array
    @Column(name = "stage_position") private String stagePosition;
    @Column(name = "vocal_mic_id") private String vocalMicId;
    private String notes;
    @Column(name = "sort_order", nullable = false) private int sortOrder = 0;

    protected BandMember() {}
    public BandMember(String bandId, String name) { this.bandId = bandId; this.name = name; }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getStagePosition() { return stagePosition; }
    public void setStagePosition(String stagePosition) { this.stagePosition = stagePosition; }
    public String getVocalMicId() { return vocalMicId; }
    public void setVocalMicId(String vocalMicId) { this.vocalMicId = vocalMicId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
