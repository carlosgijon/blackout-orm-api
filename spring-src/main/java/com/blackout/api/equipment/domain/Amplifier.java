package com.blackout.api.equipment.domain;

import jakarta.persistence.*;

@Entity @Table(name = "amplifiers")
public class Amplifier {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(name = "member_id") private String memberId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String type;
    private String brand;
    private String model;
    private Integer wattage;
    @Column(nullable = false) private String routing;
    @Column(name = "mono_stereo") private String monoStereo;
    @Column(name = "stage_position") private String stagePosition;
    private String notes;
    @Column(name = "cabinet_brand") private String cabinetBrand;
    @Column(name = "speaker_brand") private String speakerBrand;
    @Column(name = "speaker_model") private String speakerModel;
    @Column(name = "speaker_config") private String speakerConfig;

    protected Amplifier() {}
    public Amplifier(String bandId, String name, String type, String routing) {
        this.bandId = bandId; this.name = name; this.type = type; this.routing = routing;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String m) { memberId = m; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getType() { return type; }
    public void setType(String t) { type = t; }
    public String getBrand() { return brand; }
    public void setBrand(String b) { brand = b; }
    public String getModel() { return model; }
    public void setModel(String m) { model = m; }
    public Integer getWattage() { return wattage; }
    public void setWattage(Integer w) { wattage = w; }
    public String getRouting() { return routing; }
    public void setRouting(String r) { routing = r; }
    public String getMonoStereo() { return monoStereo; }
    public void setMonoStereo(String ms) { monoStereo = ms; }
    public String getStagePosition() { return stagePosition; }
    public void setStagePosition(String sp) { stagePosition = sp; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public String getCabinetBrand() { return cabinetBrand; }
    public void setCabinetBrand(String c) { cabinetBrand = c; }
    public String getSpeakerBrand() { return speakerBrand; }
    public void setSpeakerBrand(String s) { speakerBrand = s; }
    public String getSpeakerModel() { return speakerModel; }
    public void setSpeakerModel(String s) { speakerModel = s; }
    public String getSpeakerConfig() { return speakerConfig; }
    public void setSpeakerConfig(String s) { speakerConfig = s; }
}
