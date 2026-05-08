package com.blackout.api.equipment.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stage_plots")
public class StagePlot {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false, unique = true)
    private String bandId;

    @Column(name = "plot_data", columnDefinition = "TEXT")
    private String plotData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected StagePlot() {}

    public StagePlot(String bandId, String plotData) {
        this.bandId = bandId;
        this.plotData = plotData;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    
    public String getPlotData() { return plotData; }
    public void setPlotData(String plotData) { 
        this.plotData = plotData; 
        this.updatedAt = Instant.now();
    }
    
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
