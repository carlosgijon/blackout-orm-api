package com.blackout.api.mixer.infrastructure.persistence;

import com.blackout.api.mixer.application.port.out.LoadScnFilePort;
import com.blackout.api.mixer.application.port.out.SaveScnFilePort;
import com.blackout.api.mixer.domain.ScnFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class ScnFilePersistenceAdapter implements LoadScnFilePort, SaveScnFilePort {

    private final JpaScnFileRepository repo;

    ScnFilePersistenceAdapter(JpaScnFileRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ScnFile> findAllByBandId(String bandId) {
        return repo.findAllByBandIdOrderByCreatedAtDesc(bandId);
    }

    @Override
    public Optional<ScnFile> findByIdAndBandId(String id, String bandId) {
        return repo.findByIdAndBandId(id, bandId);
    }

    @Override
    public ScnFile save(ScnFile file) {
        return repo.save(file);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
