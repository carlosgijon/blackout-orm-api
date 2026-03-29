package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.setlist.application.port.out.LoadLibrarySongPort;
import com.blackout.api.setlist.application.port.out.SaveLibrarySongPort;
import com.blackout.api.setlist.domain.LibrarySong;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class LibrarySongPersistenceAdapter implements LoadLibrarySongPort, SaveLibrarySongPort {

    private final JpaLibrarySongRepository repo;

    public LibrarySongPersistenceAdapter(JpaLibrarySongRepository repo) {
        this.repo = repo;
    }

    @Override public List<LibrarySong> findAllByBandId(String bandId) {
        return repo.findAllByBandIdOrderByTitleAsc(bandId);
    }
    @Override public Optional<LibrarySong> findByIdAndBandId(String id, String bandId) {
        return repo.findByIdAndBandId(id, bandId);
    }
    @Override public List<LibrarySong> findAllById(Iterable<String> ids) {
        return repo.findAllById(ids);
    }
    @Override public LibrarySong save(LibrarySong song) { return repo.save(song); }
    @Override public void deleteById(String id) { repo.deleteById(id); }
}
