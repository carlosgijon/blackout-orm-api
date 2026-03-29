package com.blackout.api.equipment.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "microphones")
public class Microphone {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String name;
    private String brand;
    @Column(name = "model") private String model;
    @Column(nullable = false) private String type;
    @Column(name = "polar_pattern") private String polarPattern;
    @Column(name = "phantom_power", nullable = false) private boolean phantomPower = false;
    @Column(name = "mono_stereo", nullable = false) private String monoStereo = "mono";
    private String notes;
    private String usage;
    @Column(name = "assigned_to_type") private String assignedToType;
    @Column(name = "assigned_to_id") private String assignedToId;

    protected Microphone() {}
    public Microphone(String bandId, String name, String type) {
        this.bandId = bandId; this.name = name; this.type = type;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getBrand() { return brand; }
    public void setBrand(String b) { this.brand = b; }
    public String getModel() { return model; }
    public void setModel(String m) { this.model = m; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getPolarPattern() { return polarPattern; }
    public void setPolarPattern(String p) { this.polarPattern = p; }
    public boolean isPhantomPower() { return phantomPower; }
    public void setPhantomPower(boolean p) { this.phantomPower = p; }
    public String getMonoStereo() { return monoStereo; }
    public void setMonoStereo(String ms) { this.monoStereo = ms; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
    public String getUsage() { return usage; }
    public void setUsage(String u) { this.usage = u; }
    public String getAssignedToType() { return assignedToType; }
    public void setAssignedToType(String t) { this.assignedToType = t; }
    public String getAssignedToId() { return assignedToId; }
    public void setAssignedToId(String id) { this.assignedToId = id; }
}
