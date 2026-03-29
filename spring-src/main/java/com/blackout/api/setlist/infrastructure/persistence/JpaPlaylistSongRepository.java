package com.blackout.api.setlist.infrastructure.persistence;

import com.blackout.api.setlist.domain.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
interface JpaPlaylistSongRepository extends JpaRepository<PlaylistSong, String> {

    List<PlaylistSong> findByPlaylistIdOrderByPositionAsc(String playlistId);
    int countByPlaylistId(String playlistId);

    /**
     * Returns [songId (String), count (Long)] for every song used in any playlist
     * belonging to the given band. Used by getStats.
     */
    @Query("""
        SELECT ps.songId, COUNT(ps)
        FROM PlaylistSong ps
        JOIN Playlist p ON ps.playlistId = p.id
        WHERE p.bandId = :bandId AND ps.songId IS NOT NULL
        GROUP BY ps.songId
        """)
    List<Object[]> countUsagePerSongInBand(@Param("bandId") String bandId);

    /**
     * Returns distinct playlist names that contain the given songId. Used by getUsage.
     */
    @Query("""
        SELECT DISTINCT p.name
        FROM PlaylistSong ps
        JOIN Playlist p ON ps.playlistId = p.id
        WHERE ps.songId = :songId
        """)
    List<String> findPlaylistNamesBySongId(@Param("songId") String songId);
}
