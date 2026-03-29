package com.blackout.api.identity.infrastructure.persistence;

import com.blackout.api.identity.domain.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface JpaBandRepository extends JpaRepository<Band, String> {
    Optional<Band> findBySlug(String slug);

    @Query("""
        SELECT new com.blackout.api.identity.application.port.out.LoadBandPort$BandSummary(
            b.id, b.name, b.slug, b.createdAt,
            (SELECT COUNT(ub) FROM UserBand ub WHERE ub.bandId = b.id)
        )
        FROM Band b
        ORDER BY b.createdAt ASC
        """)
    List<com.blackout.api.identity.application.port.out.LoadBandPort.BandSummary> findAllWithMemberCount();
}
