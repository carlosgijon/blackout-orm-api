package com.blackout.api.votes.application.port.out;

import com.blackout.api.votes.domain.VoteSession;

public interface SaveVoteSessionPort {
    VoteSession save(VoteSession session);
    void deleteById(String id);
}
