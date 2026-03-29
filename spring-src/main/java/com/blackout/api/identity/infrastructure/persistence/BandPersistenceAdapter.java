package com.blackout.api.identity.infrastructure.persistence;

import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.Band;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class BandPersistenceAdapter implements LoadBandPort, SaveBandPort {

    private final JpaBandRepository bands;

    public BandPersistenceAdapter(JpaBandRepository bands) {
        this.bands = bands;
    }

    @Override public Optional<Band> findById(String id) { return bands.findById(id); }
    @Override public Optional<Band> findBySlug(String slug) { return bands.findBySlug(slug); }
    @Override public List<BandSummary> findAllWithMemberCount() { return bands.findAllWithMemberCount(); }
    @Override public Band save(Band band) { return bands.save(band); }
    @Override public void deleteById(String id) { bands.deleteById(id); }
}
