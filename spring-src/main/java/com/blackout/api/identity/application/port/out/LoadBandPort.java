package com.blackout.api.identity.application.port.out;

import com.blackout.api.identity.domain.Band;
import java.util.List;
import java.util.Optional;

public interface LoadBandPort {
    Optional<Band> findById(String id);
    Optional<Band> findBySlug(String slug);
    List<BandSummary> findAllWithMemberCount();

    record BandSummary(String id, String name, String slug, java.time.Instant createdAt, long memberCount) {}
}
