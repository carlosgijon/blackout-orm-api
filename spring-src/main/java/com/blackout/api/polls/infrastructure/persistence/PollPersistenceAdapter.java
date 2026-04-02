package com.blackout.api.polls.infrastructure.persistence;

import com.blackout.api.polls.application.port.out.LoadPollPort;
import com.blackout.api.polls.application.port.out.SavePollPort;
import com.blackout.api.polls.domain.Poll;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class PollPersistenceAdapter implements LoadPollPort, SavePollPort {

    private final JpaPollRepository pollRepo;
    private final JpaGigForPollsRepository gigRepo;

    PollPersistenceAdapter(JpaPollRepository pollRepo, JpaGigForPollsRepository gigRepo) {
        this.pollRepo = pollRepo;
        this.gigRepo  = gigRepo;
    }

    @Override
    public List<Poll> findAllByBandId(String bandId) {
        return pollRepo.findAllByBandId(bandId);
    }

    @Override
    public Optional<Poll> findByIdAndBandId(String id, String bandId) {
        return pollRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public boolean existsGigByIdAndBandId(String gigId, String bandId) {
        return gigRepo.existsByIdAndBandId(gigId, bandId);
    }

    @Override
    public Poll save(Poll poll) {
        return pollRepo.save(poll);
    }

    @Override
    public void deleteById(String id) {
        pollRepo.deleteById(id);
    }
}
