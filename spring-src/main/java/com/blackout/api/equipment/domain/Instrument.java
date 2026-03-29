package com.blackout.api.equipment.domain;

import jakarta.persistence.*;

@Entity @Table(name = "instruments")
public class Instrument {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "member_id") private String memberId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String type;
    private String brand;
    private String model;
    @Column(nullable = false) private String routing;
    @Column(name = "amp_id") private String ampId;
    @Column(name = "mono_stereo") private String monoStereo;
    @Column(name = "channel_order", nullable = false) private int channelOrder = 0;
    private String notes;

    protected Instrument() {}
    public Instrument(String bandId, String name, String type, String routing) {
        this.bandId = bandId; this.name = name; this.type = type; this.routing = routing;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String m) { this.memberId = m; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getBrand() { return brand; }
    public void setBrand(String b) { this.brand = b; }
    public String getModel() { return model; }
    public void setModel(String m) { this.model = m; }
    public String getRouting() { return routing; }
    public void setRouting(String r) { this.routing = r; }
    public String getAmpId() { return ampId; }
    public void setAmpId(String a) { this.ampId = a; }
    public String getMonoStereo() { return monoStereo; }
    public void setMonoStereo(String ms) { this.monoStereo = ms; }
    public int getChannelOrder() { return channelOrder; }
    public void setChannelOrder(int c) { this.channelOrder = c; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
}
