package com.blackout.api.settings.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "settings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"band_id", "key"}))
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    protected Setting() {}

    public Setting(String bandId, String key, String value) {
        this.bandId = bandId;
        this.key = key;
        this.value = value;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
