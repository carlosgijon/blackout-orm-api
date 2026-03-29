package com.blackout.api.setlist.application.port.out;

import com.blackout.api.setlist.domain.LibrarySong;

public interface SaveLibrarySongPort {
    LibrarySong save(LibrarySong song);
    void deleteById(String id);
}
