package com.blackout.api.rehearsal.application.port.out;

import com.blackout.api.rehearsal.domain.Rehearsal;

public interface SaveRehearsalPort {
    Rehearsal save(Rehearsal r);
    void deleteById(String id);
}
