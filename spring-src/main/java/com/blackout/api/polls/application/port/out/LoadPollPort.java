package com.blackout.api.polls.application.port.out;

import com.blackout.api.polls.domain.Poll;
import java.util.List;
import java.util.Optional;

public interface LoadPollPort {
    List<Poll> findAllByBandId(String bandId);
    Optional<Poll> findByIdAndBandId(String id, String bandId);
    boolean existsGigByIdAndBandId(String gigId, String bandId);
}
