package com.blackout.api.gig.infrastructure.persistence;

import com.blackout.api.finance.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
interface JpaTransactionForGigRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t WHERE t.gigId = :gigId AND t.bandId = :bandId ORDER BY t.date DESC")
    List<Transaction> findByGigIdAndBandId(@Param("gigId") String gigId, @Param("bandId") String bandId);
}
