package com.blackout.api.setlist.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pure unit tests for the Playlist aggregate root.
 * No Spring context — tests domain logic only.
 */
class PlaylistDomainTest {

    private Playlist playlist;
    private PlaylistSong song1;
    private PlaylistSong song2;
    private PlaylistSong song3;

    @BeforeEach
    void setUp() {
        playlist = new Playlist("band-1", "Test Playlist");

        song1 = song("id-1", 1);
        song2 = song("id-2", 2);
        song3 = song("id-3", 3);

        playlist.getSongs().add(song1);
        playlist.getSongs().add(song2);
        playlist.getSongs().add(song3);
    }

    @Test
    @DisplayName("removeEntry removes the song and resequences positions")
    void removeEntry_resequencesPositions() {
        playlist.removeEntry("id-2");

        List<PlaylistSong> songs = playlist.getSongs();
        assertThat(songs).hasSize(2);
        assertThat(songs.get(0).getId()).isEqualTo("id-1");
        assertThat(songs.get(0).getPosition()).isEqualTo(1);
        assertThat(songs.get(1).getId()).isEqualTo("id-3");
        assertThat(songs.get(1).getPosition()).isEqualTo(2);
    }

    @Test
    @DisplayName("removeEntry with non-existent id does nothing")
    void removeEntry_nonExistentId_noOp() {
        playlist.removeEntry("no-such-id");
        assertThat(playlist.getSongs()).hasSize(3);
    }

    @Test
    @DisplayName("applyOrder reorders songs by given id sequence")
    void applyOrder_changesPositions() {
        playlist.applyOrder(List.of("id-3", "id-1", "id-2"));

        assertThat(song3.getPosition()).isEqualTo(1);
        assertThat(song1.getPosition()).isEqualTo(2);
        assertThat(song2.getPosition()).isEqualTo(3);
    }

    @Test
    @DisplayName("applyOrder ignores unknown ids gracefully")
    void applyOrder_unknownIds_ignoredGracefully() {
        playlist.applyOrder(List.of("id-1", "unknown", "id-3"));

        assertThat(song1.getPosition()).isEqualTo(1);
        assertThat(song3.getPosition()).isEqualTo(2);
        // song2 untouched — position stays at 2 (original)
    }

    @Test
    @DisplayName("removeEntry on last remaining song leaves empty list with no NPE")
    void removeEntry_allSongs_emptyList() {
        playlist.removeEntry("id-1");
        playlist.removeEntry("id-2");
        playlist.removeEntry("id-3");
        assertThat(playlist.getSongs()).isEmpty();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private PlaylistSong song(String id, int position) {
        PlaylistSong ps = new PlaylistSong();
        // Reflectively set id because it's private with no public setter
        try {
            var f = PlaylistSong.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(ps, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ps.setPosition(position);
        ps.setType("song");
        return ps;
    }
}
