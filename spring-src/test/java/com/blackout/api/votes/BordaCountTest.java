package com.blackout.api.votes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pure unit tests for the Borda-count algorithm.
 * Tests the ranking logic in isolation without Spring context.
 */
class BordaCountTest {

    /**
     * Replicates VotesApplicationService#computeResults() logic for isolated testing.
     */
    record VoteResultEntry(String songId, double avgRank, int voteCount) {}

    private List<VoteResultEntry> computeResults(List<List<String>> votes) {
        Map<String, Integer> rankSum = new HashMap<>();
        Map<String, Integer> rankCount = new HashMap<>();

        for (List<String> ballot : votes) {
            for (int i = 0; i < ballot.size(); i++) {
                String id = ballot.get(i);
                rankSum.merge(id, i + 1, Integer::sum);
                rankCount.merge(id, 1, Integer::sum);
            }
        }

        return rankSum.entrySet().stream()
            .map(e -> new VoteResultEntry(
                e.getKey(),
                (double) e.getValue() / rankCount.getOrDefault(e.getKey(), 1),
                rankCount.getOrDefault(e.getKey(), 0)))
            .sorted(Comparator.comparingDouble(VoteResultEntry::avgRank))
            .toList();
    }

    @Test
    @DisplayName("empty votes returns empty result")
    void emptyVotes_returnsEmpty() {
        assertThat(computeResults(List.of())).isEmpty();
    }

    @Test
    @DisplayName("single voter: order matches the ballot exactly")
    void singleVoter_orderMatchesBallot() {
        List<VoteResultEntry> results = computeResults(
            List.of(List.of("S3", "S1", "S2"))
        );

        assertThat(results.get(0).songId()).isEqualTo("S3"); // rank 1 → avgRank 1.0
        assertThat(results.get(1).songId()).isEqualTo("S1"); // rank 2 → avgRank 2.0
        assertThat(results.get(2).songId()).isEqualTo("S2"); // rank 3 → avgRank 3.0
    }

    @Test
    @DisplayName("two voters agreeing produce same order")
    void twoVotersAgreeing_sameResult() {
        List<List<String>> votes = List.of(
            List.of("S1", "S2", "S3"),
            List.of("S1", "S2", "S3")
        );

        List<VoteResultEntry> results = computeResults(votes);

        assertThat(results.get(0).songId()).isEqualTo("S1");
        assertThat(results.get(0).avgRank()).isEqualTo(1.0);
        assertThat(results.get(0).voteCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("two voters disagreeing: average rank determines winner")
    void twoVotersDisagreeing_averageRankDecides() {
        // Voter A: S1(1), S2(2), S3(3) → S1 avg=1.5, S2 avg=2.0, S3 avg=2.5
        // Voter B: S2(1), S1(2), S3(3)
        List<List<String>> votes = List.of(
            List.of("S1", "S2", "S3"),
            List.of("S2", "S1", "S3")
        );

        List<VoteResultEntry> results = computeResults(votes);

        // S1: (1+2)/2=1.5, S2: (2+1)/2=1.5, S3: (3+3)/2=3.0
        // S1 and S2 are tied at 1.5; S3 is last
        assertThat(results.get(2).songId()).isEqualTo("S3");
        assertThat(results.get(2).avgRank()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("song not voted by everyone still appears in results")
    void songNotVotedByAll_appearsWithPartialVoteCount() {
        // S4 only voted by voter B
        List<List<String>> votes = List.of(
            List.of("S1", "S2"),
            List.of("S4", "S1", "S2")
        );

        List<VoteResultEntry> results = computeResults(votes);
        Optional<VoteResultEntry> s4 = results.stream().filter(r -> r.songId().equals("S4")).findFirst();

        assertThat(s4).isPresent();
        assertThat(s4.get().voteCount()).isEqualTo(1);
        assertThat(s4.get().avgRank()).isEqualTo(1.0); // ranked 1st by 1 voter
    }

    @Test
    @DisplayName("lower avgRank means higher preference (Borda invariant)")
    void lowerAvgRankMeansHigherPreference() {
        List<VoteResultEntry> results = computeResults(List.of(
            List.of("A", "B", "C", "D"),
            List.of("A", "B", "C", "D"),
            List.of("A", "B", "C", "D")
        ));

        // All voters agree: A=1st, B=2nd, C=3rd, D=4th
        assertThat(results).extracting(VoteResultEntry::songId)
            .containsExactly("A", "B", "C", "D");
        assertThat(results).extracting(VoteResultEntry::avgRank)
            .containsExactly(1.0, 2.0, 3.0, 4.0);
    }
}
