package com.blackout.api.equipment.domain;

import jakarta.persistence.*;

@Entity @Table(name = "pa_equipment")
public class PaEquipment {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private String name;
    private String brand;
    private String model;
    @Column(nullable = false) private int quantity = 1;
    private Integer channels;
    @Column(name = "aux_sends") private Integer auxSends;
    private Integer wattage;
    private String notes;
    @Column(name = "monitor_type") private String monitorType;
    @Column(name = "iem_wireless", nullable = false) private boolean iemWireless = false;

    protected PaEquipment() {}
    public PaEquipment(String bandId, String category, String name) {
        this.bandId = bandId; this.category = category; this.name = name;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getCategory() { return category; }
    public void setCategory(String c) { category = c; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getBrand() { return brand; }
    public void setBrand(String b) { brand = b; }
    public String getModel() { return model; }
    public void setModel(String m) { model = m; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { quantity = q; }
    public Integer getChannels() { return channels; }
    public void setChannels(Integer c) { channels = c; }
    public Integer getAuxSends() { return auxSends; }
    public void setAuxSends(Integer a) { auxSends = a; }
    public Integer getWattage() { return wattage; }
    public void setWattage(Integer w) { wattage = w; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public String getMonitorType() { return monitorType; }
    public void setMonitorType(String mt) { monitorType = mt; }
    public boolean isIemWireless() { return iemWireless; }
    public void setIemWireless(boolean iem) { iemWireless = iem; }
}
