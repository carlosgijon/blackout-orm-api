package com.blackout.api.identity.infrastructure.persistence;

import com.blackout.api.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findAllByBandIdOrderByCreatedAtAsc(String bandId);
    boolean existsByUsername(String username);
}
