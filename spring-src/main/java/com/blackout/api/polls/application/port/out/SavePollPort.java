package com.blackout.api.polls.application.port.out;

import com.blackout.api.polls.domain.Poll;

public interface SavePollPort {
    Poll save(Poll poll);
    void deleteById(String id);
}
