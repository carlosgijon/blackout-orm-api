package com.blackout.api.rehearsal.infrastructure.persistence;

import com.blackout.api.rehearsal.application.port.out.LoadRehearsalPort;
import com.blackout.api.rehearsal.application.port.out.SaveRehearsalPort;
import com.blackout.api.rehearsal.domain.Rehearsal;
import com.blackout.api.rehearsal.domain.RehearsalSong;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class RehearsalPersistenceAdapter implements LoadRehearsalPort, SaveRehearsalPort {

    private final JpaRehearsalRepository repo;
    private final JpaRehearsalSongRepository songRepo;
    private final EntityManager em;

    RehearsalPersistenceAdapter(JpaRehearsalRepository repo, JpaRehearsalSongRepository songRepo, EntityManager em) {
        this.repo = repo;
        this.songRepo = songRepo;
        this.em = em;
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
        // Delete songs first via native SQL to avoid Hibernate's SET NULL pattern
        em.createNativeQuery("DELETE FROM rehearsal_songs WHERE rehearsal_id = ?1")
                .setParameter(1, id)
                .executeUpdate();
        repo.deleteById(id);
    }

    @Override
    public RehearsalSong saveSong(RehearsalSong song) {
        return songRepo.save(song);
    }

    @Override
    public void deleteSongById(String id) {
        songRepo.deleteById(id);
    }

    @Override
    public String[] findLibrarySongInfo(String songId) {
        List<?> result = em.createNativeQuery(
                "SELECT title, artist, tempo, style FROM library_songs WHERE id = ?1")
                .setParameter(1, songId)
                .getResultList();
        if (result.isEmpty()) return new String[]{null, null, null, null};
        Object[] row = (Object[]) result.get(0);
        return new String[]{
            (String) row[0],
            (String) row[1],
            row[2] != null ? row[2].toString() : null,
            (String) row[3]
        };
    }
}
