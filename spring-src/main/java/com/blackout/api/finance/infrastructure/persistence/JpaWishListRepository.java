package com.blackout.api.finance.infrastructure.persistence;

import com.blackout.api.finance.domain.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaWishListRepository extends JpaRepository<WishListItem, String> {

    List<WishListItem> findAllByBandIdOrderByCreatedAtAsc(String bandId);

    Optional<WishListItem> findByIdAndBandId(String id, String bandId);
}
