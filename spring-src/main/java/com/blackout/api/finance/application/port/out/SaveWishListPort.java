package com.blackout.api.finance.application.port.out;

import com.blackout.api.finance.domain.WishListItem;

public interface SaveWishListPort {
    WishListItem save(WishListItem item);
    void deleteById(String id);
}
