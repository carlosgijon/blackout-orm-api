package com.blackout.api.polls.infrastructure.persistence;

import com.blackout.api.polls.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface JpaPollRepository extends JpaRepository<Poll, String> {

    @Query("SELECT p FROM Poll p WHERE p.bandId = :bandId ORDER BY p.createdAt DESC")
    List<Poll> findAllByBandId(@Param("bandId") String bandId);

    @Query("SELECT p FROM Poll p WHERE p.id = :id AND p.bandId = :bandId")
    Optional<Poll> findByIdAndBandId(@Param("id") String id, @Param("bandId") String bandId);

    // Override default deleteById to bypass Hibernate orphan-removal logic.
    // The DB schema has ON DELETE CASCADE on poll_options and poll_votes,
    // so a direct DELETE lets PostgreSQL handle child rows in correct order.
    @Override
    @Modifying
    @Query("DELETE FROM Poll p WHERE p.id = :id")
    void deleteById(@Param("id") String id);
}
