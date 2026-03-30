package com.blackout.api.rehearsal.infrastructure.persistence;

import com.blackout.api.rehearsal.application.port.out.LoadRehearsalPort;
import com.blackout.api.rehearsal.application.port.out.SaveRehearsalPort;
import com.blackout.api.rehearsal.domain.Rehearsal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class RehearsalPersistenceAdapter implements LoadRehearsalPort, SaveRehearsalPort {

    private final JpaRehearsalRepository repo;

    RehearsalPersistenceAdapter(JpaRehearsalRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Rehearsal> findAllByBandIdOrderByDateDesc(String bandId) {
        return repo.findAllByBandIdOrderByDateDesc(bandId);
    }

    @Override
    public Optional<Rehearsal> findByIdAndBandId(String id, String bandId) {
        return repo.findById(id).filter(r -> r.getBandId().equals(bandId));
    }

    @Override
    public Rehearsal save(Rehearsal r) {
        return repo.save(r);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
