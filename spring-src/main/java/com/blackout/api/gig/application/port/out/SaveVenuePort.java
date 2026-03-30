package com.blackout.api.gig.application.port.out;

import com.blackout.api.gig.domain.Venue;

public interface SaveVenuePort {
    Venue save(Venue venue);
    void deleteById(String id);
}
