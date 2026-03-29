package com.blackout.api.identity.application.port.out;

import com.blackout.api.identity.domain.User;
import java.util.List;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    List<User> findAllByBandId(String bandId);
    boolean existsByUsername(String username);
}
