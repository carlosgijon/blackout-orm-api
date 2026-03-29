package com.blackout.api.identity.application.port.out;

import com.blackout.api.identity.domain.UserBand;
import java.util.List;
import java.util.Optional;

public interface LoadUserBandPort {
    Optional<UserBand> findByUserIdAndBandId(String userId, String bandId);
    List<UserBand> findAllByUserId(String userId);
    UserBand save(UserBand userBand);
}
