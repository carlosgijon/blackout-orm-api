package com.blackout.api.rehearsal.application.port.out;

import com.blackout.api.rehearsal.domain.Rehearsal;
import com.blackout.api.rehearsal.domain.RehearsalSong;

public interface SaveRehearsalPort {
    Rehearsal save(Rehearsal r);
    void deleteById(String id);
    RehearsalSong saveSong(RehearsalSong song);
    void deleteSongById(String id);
}
