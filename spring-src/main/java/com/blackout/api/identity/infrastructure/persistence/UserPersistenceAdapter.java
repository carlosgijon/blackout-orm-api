package com.blackout.api.identity.infrastructure.persistence;

import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.User;
import com.blackout.api.identity.domain.UserBand;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, LoadUserBandPort {

    private final JpaUserRepository users;
    private final JpaUserBandRepository userBands;

    public UserPersistenceAdapter(JpaUserRepository users, JpaUserBandRepository userBands) {
        this.users = users;
        this.userBands = userBands;
    }

    // LoadUserPort
    @Override public Optional<User> findById(String id) { return users.findById(id); }
    @Override public Optional<User> findByUsername(String username) { return users.findByUsername(username); }
    @Override public List<User> findAllByBandId(String bandId) { return users.findAllByBandIdOrderByCreatedAtAsc(bandId); }
    @Override public boolean existsByUsername(String username) { return users.existsByUsername(username); }

    // SaveUserPort
    @Override public User save(User user) { return users.save(user); }
    @Override public void deleteById(String id) { users.deleteById(id); }

    // LoadUserBandPort
    @Override public Optional<UserBand> findByUserIdAndBandId(String userId, String bandId) {
        return userBands.findByUserIdAndBandId(userId, bandId);
    }
    @Override public List<UserBand> findAllByUserId(String userId) {
        return userBands.findAllByUserId(userId);
    }
    @Override public UserBand save(UserBand ub) { return userBands.save(ub); }
}
