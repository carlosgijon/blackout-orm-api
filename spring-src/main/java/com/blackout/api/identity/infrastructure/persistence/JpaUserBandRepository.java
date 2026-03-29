package com.blackout.api.identity.infrastructure.persistence;

import com.blackout.api.identity.domain.UserBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface JpaUserBandRepository extends JpaRepository<UserBand, String> {
    Optional<UserBand> findByUserIdAndBandId(String userId, String bandId);
    List<UserBand> findAllByUserId(String userId);
}
