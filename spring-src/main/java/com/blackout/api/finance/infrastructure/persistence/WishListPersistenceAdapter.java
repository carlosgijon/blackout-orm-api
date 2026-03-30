package com.blackout.api.finance.infrastructure.persistence;

import com.blackout.api.finance.application.port.out.LoadWishListPort;
import com.blackout.api.finance.application.port.out.SaveWishListPort;
import com.blackout.api.finance.domain.WishListItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class WishListPersistenceAdapter implements LoadWishListPort, SaveWishListPort {

    private final JpaWishListRepository repo;

    WishListPersistenceAdapter(JpaWishListRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<WishListItem> findAllByBandIdOrderByCreatedAtAsc(String bandId) {
        return repo.findAllByBandIdOrderByCreatedAtAsc(bandId);
    }

    @Override
    public Optional<WishListItem> findByIdAndBandId(String id, String bandId) {
        return repo.findByIdAndBandId(id, bandId);
    }

    @Override
    public WishListItem save(WishListItem item) {
        return repo.save(item);
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
