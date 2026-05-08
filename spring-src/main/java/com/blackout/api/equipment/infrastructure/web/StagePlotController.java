package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.domain.StagePlot;
import com.blackout.api.equipment.infrastructure.persistence.StagePlotRepository;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stage-plot")
public class StagePlotController {

    private final StagePlotRepository repository;

    public StagePlotController(StagePlotRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public StagePlot getStagePlot(BlackoutAuthentication auth) {
        return repository.findByBandId(auth.getBandId())
                .orElseGet(() -> new StagePlot(auth.getBandId(), "[]"));
    }

    @PostMapping
    public StagePlot saveStagePlot(BlackoutAuthentication auth, @RequestBody Map<String, String> payload) {
        String data = payload.get("plotData");
        StagePlot plot = repository.findByBandId(auth.getBandId())
                .orElse(new StagePlot(auth.getBandId(), data));
        plot.setPlotData(data);
        return repository.save(plot);
    }
}
