package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.gig.application.port.out.LoadVenuePort;
import com.blackout.api.gig.application.port.out.SaveVenuePort;
import com.blackout.api.gig.domain.Venue;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class VenuePersistenceAdapter implements LoadVenuePort, SaveVenuePort {

    private final JpaVenueRepository repo;

    public VenuePersistenceAdapter(JpaVenueRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Venue> findByIdAndBandId(String id, String bandId) {
        return repo.findByIdAndBandId(id, bandId);
    }

    @Override
    public List<Venue> findAllByBandId(String bandId) {
        return repo.findAllByBandIdOrderByNameAsc(bandId);
    }

    @Override
    public Venue save(Venue venue) {
        return repo.save(venue);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
