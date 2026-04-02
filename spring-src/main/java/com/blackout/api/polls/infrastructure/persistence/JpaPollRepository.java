package com.blackout.api.polls.infrastructure.persistence;

import com.blackout.api.polls.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface JpaPollRepository extends JpaRepository<Poll, String> {

    @Query("SELECT p FROM Poll p LEFT JOIN FETCH p.options LEFT JOIN FETCH p.votes WHERE p.bandId = :bandId ORDER BY p.createdAt DESC")
    List<Poll> findAllByBandId(@Param("bandId") String bandId);

    @Query("SELECT p FROM Poll p LEFT JOIN FETCH p.options LEFT JOIN FETCH p.votes WHERE p.id = :id AND p.bandId = :bandId")
    Optional<Poll> findByIdAndBandId(@Param("id") String id, @Param("bandId") String bandId);
}
