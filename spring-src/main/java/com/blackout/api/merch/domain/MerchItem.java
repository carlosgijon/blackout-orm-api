package com.blackout.api.merch.domain;

import com.blackout.api.shared.domain.BadRequestException;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.*;

@Entity @Table(name = "merch_items")
public class MerchItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "band_id", nullable = false) private String bandId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String type;
    @Column(name = "production_cost", nullable = false) private double productionCost;
    @Column(name = "batch_size", nullable = false) private int batchSize = 50;
    @Column(name = "selling_price", nullable = false) private double sellingPrice;
    @Column(name = "fixed_costs", nullable = false) private double fixedCosts = 0;
    @Column(nullable = false) private int stock = 0;
    @Column(name = "has_sizes", nullable = false) private boolean hasSizes = false;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stock_sizes") private Map<String, Integer> stockSizes;
    private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt = Instant.now();

    protected MerchItem() {}
    public MerchItem(String bandId, String name, String type, double productionCost, double sellingPrice) {
        this.bandId = bandId; this.name = name; this.type = type;
        this.productionCost = productionCost; this.sellingPrice = sellingPrice;
    }

    /** Domain method: sell units. Atomically validates and decrements stock. */
    public void sell(int quantity, String size) {
        if (hasSizes && size != null && !size.isBlank()) {
            int current = stockSizes.getOrDefault(size, 0);
            if (current < quantity) throw new BadRequestException("Stock insuficiente para talla " + size);
            stockSizes.put(size, current - quantity);
        } else {
            if (stock < quantity) throw new BadRequestException("Stock insuficiente");
            stock -= quantity;
        }
        recomputeTotal();
    }

    /** Recomputes total stock from sizes map */
    public void recomputeTotal() {
        if (hasSizes && stockSizes != null) {
            this.stock = stockSizes.values().stream().mapToInt(Integer::intValue).sum();
        }
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getType() { return type; }
    public void setType(String t) { type = t; }
    public double getProductionCost() { return productionCost; }
    public void setProductionCost(double p) { productionCost = p; }
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int b) { batchSize = b; }
    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double s) { sellingPrice = s; }
    public double getFixedCosts() { return fixedCosts; }
    public void setFixedCosts(double f) { fixedCosts = f; }
    public int getStock() { return stock; }
    public void setStock(int s) { stock = s; }
    public boolean isHasSizes() { return hasSizes; }
    public void setHasSizes(boolean h) { hasSizes = h; }
    public Map<String, Integer> getStockSizes() { return stockSizes; }
    public void setStockSizes(Map<String, Integer> ss) { this.stockSizes = ss; recomputeTotal(); }
    public String getNotes() { return notes; }
    public void setNotes(String n) { notes = n; }
    public Instant getCreatedAt() { return createdAt; }
}
