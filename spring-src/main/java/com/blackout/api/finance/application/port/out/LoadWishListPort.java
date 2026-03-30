package com.blackout.api.finance.application.port.out;

import com.blackout.api.finance.domain.WishListItem;
import java.util.List;
import java.util.Optional;

public interface LoadWishListPort {
    List<WishListItem> findAllByBandIdOrderByCreatedAtAsc(String bandId);
    Optional<WishListItem> findByIdAndBandId(String id, String bandId);
}
