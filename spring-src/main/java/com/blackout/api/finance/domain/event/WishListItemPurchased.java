package com.blackout.api.finance.domain.event;

public record WishListItemPurchased(
    String itemId, String bandId, String name, String category, Double estimatedPrice) {}
