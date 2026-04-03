package com.blackout.api.mixer.application.port.out;

import com.blackout.api.mixer.domain.ScnFile;

public interface SaveScnFilePort {
    ScnFile save(ScnFile file);
    void deleteById(String id);
}
