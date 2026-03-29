package com.blackout.api.identity.application.service;

import com.blackout.api.identity.application.factory.BandFactory;
import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.Band;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BandsApplicationService {

    private final LoadBandPort loadBand;
    private final SaveBandPort saveBand;
    private final BandFactory bandFactory;

    public BandsApplicationService(LoadBandPort loadBand, SaveBandPort saveBand, BandFactory bandFactory) {
        this.loadBand = loadBand;
        this.saveBand = saveBand;
        this.bandFactory = bandFactory;
    }

    public List<BandSummaryResponse> findAll() {
        return loadBand.findAllWithMemberCount().stream()
            .map(s -> new BandSummaryResponse(s.id(), s.name(), s.slug(), s.createdAt(), s.memberCount()))
            .toList();
    }

    @Transactional
    public CreateBandResponse create(CreateBandRequest req) {
        return bandFactory.createWithAdmin(req);
    }

    @Transactional
    public void remove(String id) {
        Band band = loadBand.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Banda no encontrada"));
        saveBand.deleteById(band.getId());
    }

    public BandResponse getMyBand(String bandId) {
        Band band = loadBand.findById(bandId)
            .orElseThrow(() -> new ResourceNotFoundException("Banda no encontrada"));
        return BandResponse.from(band);
    }

    @Transactional
    public BandResponse updateMyBand(String bandId, UpdateBandRequest req) {
        Band band = loadBand.findById(bandId)
            .orElseThrow(() -> new ResourceNotFoundException("Banda no encontrada"));
        if (req.name() != null) band.setName(req.name());
        if (req.logo() != null)  band.setLogo(req.logo());
        return BandResponse.from(saveBand.save(band));
    }
}
