package com.blackout.api.identity.application.port.out;

import com.blackout.api.identity.domain.Band;

public interface SaveBandPort {
    Band save(Band band);
    void deleteById(String id);
}
