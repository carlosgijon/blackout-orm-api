package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.gig.domain.Gig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
interface JpaGigForPlaylistRepository extends JpaRepository<Gig, String> {

    @Query("""
        SELECT g.id, g.title, g.date, g.status, v.name
        FROM Gig g
        LEFT JOIN Venue v ON g.venueId = v.id
        WHERE g.bandId = :bandId AND g.setlistId = :playlistId
        ORDER BY g.date DESC NULLS LAST
        """)
    List<Object[]> findGigSummariesByPlaylist(
        @Param("bandId") String bandId,
        @Param("playlistId") String playlistId
    );
}
