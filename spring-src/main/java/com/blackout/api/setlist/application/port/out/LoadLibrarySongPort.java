package com.blackout.api.setlist.application.port.out;

import com.blackout.api.setlist.domain.LibrarySong;
import java.util.List;
import java.util.Optional;

public interface LoadLibrarySongPort {
    List<LibrarySong> findAllByBandId(String bandId);
    Optional<LibrarySong> findByIdAndBandId(String id, String bandId);
    List<LibrarySong> findAllById(Iterable<String> ids);
}
