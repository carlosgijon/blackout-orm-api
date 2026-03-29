package com.blackout.api.finance.domain;

import com.blackout.api.finance.domain.event.WishListItemPurchased;
import jakarta.persistence.*;
import org.springframework.context.ApplicationEventPublisher;
import java.time.Instant;

@Entity @Table(name = "wish_list_items")
public class WishListItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String category;
    @Column(name = "estimated_price") private Double estimatedPrice;
    @Column(nullable = false) private String priority = "medium";
    private String notes;
    @Column(nullable = false) private boolean purchased = false;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected WishListItem() {}
    public WishListItem(String bandId, String name, String category) {
        this.bandId = bandId; this.name = name; this.category = category;
    }

    /** Domain method: mark as purchased and publish side-effect event */
    public void markPurchased(ApplicationEventPublisher events) {
        if (!this.purchased) {
            this.purchased = true;
            events.publishEvent(new WishListItemPurchased(id, bandId, name, category, estimatedPrice));
        }
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { category = c; }
    public Double getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(Double e) { estimatedPrice = e; }
    public String getPriority() { return priority; }
    public void setPriority(String p) { priority = p; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public boolean isPurchased() { return purchased; }
    public Instant getCreatedAt() { return createdAt; }
}
