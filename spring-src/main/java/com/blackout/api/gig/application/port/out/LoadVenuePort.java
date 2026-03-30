package com.blackout.api.gig.application.port.out;

import com.blackout.api.gig.domain.Venue;
import java.util.List;
import java.util.Optional;

public interface LoadVenuePort {
    Optional<Venue> findByIdAndBandId(String id, String bandId);
    List<Venue> findAllByBandId(String bandId);
}
