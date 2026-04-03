package com.blackout.api.mixer.application.port.out;

import com.blackout.api.mixer.domain.ScnFile;
import java.util.List;
import java.util.Optional;

public interface LoadScnFilePort {
    List<ScnFile> findAllByBandId(String bandId);
    Optional<ScnFile> findByIdAndBandId(String id, String bandId);
}
