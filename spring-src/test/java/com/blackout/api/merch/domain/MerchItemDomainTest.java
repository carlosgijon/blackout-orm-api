package com.blackout.api.merch.domain;

import com.blackout.api.shared.domain.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Pure unit tests for MerchItem domain methods.
 * Tests sell() stock validation and recomputeTotal().
 */
class MerchItemDomainTest {

    @Test
    @DisplayName("sell() decrements simple stock by quantity")
    void sell_simpleStock_decrementsCorrectly() {
        MerchItem item = item(false);
        item.setStock(10);

        item.sell(3, null);

        assertThat(item.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("sell() throws BadRequestException when simple stock is insufficient")
    void sell_simpleStock_insufficientThrows() {
        MerchItem item = item(false);
        item.setStock(2);

        assertThatThrownBy(() -> item.sell(5, null))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Stock insuficiente");
    }

    @Test
    @DisplayName("sell() decrements sized stock for given size")
    void sell_sizedStock_decrementsCorrectSize() {
        MerchItem item = item(true);
        Map<String, Integer> sizes = new HashMap<>();
        sizes.put("S", 5);
        sizes.put("M", 10);
        sizes.put("L", 3);
        item.setStockSizes(sizes);

        item.sell(2, "M");

        assertThat(item.getStockSizes().get("M")).isEqualTo(8);
        assertThat(item.getStockSizes().get("S")).isEqualTo(5);
        assertThat(item.getStock()).isEqualTo(5 + 8 + 3); // recomputeTotal called
    }

    @Test
    @DisplayName("sell() throws when sized stock for a size is insufficient")
    void sell_sizedStock_insufficientThrows() {
        MerchItem item = item(true);
        Map<String, Integer> sizes = new HashMap<>();
        sizes.put("S", 1);
        item.setStockSizes(sizes);

        assertThatThrownBy(() -> item.sell(3, "S"))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Stock insuficiente para talla S");
    }

    @Test
    @DisplayName("sell() with size=null on sized item falls back to simple stock check")
    void sell_sizedItem_nullSize_usesSimpleStock() {
        MerchItem item = item(true);
        item.setStock(5);
        Map<String, Integer> sizes = new HashMap<>();
        sizes.put("S", 5);
        item.setStockSizes(sizes);

        // sell without specifying a size — uses simple stock path
        item.sell(3, null);

        assertThat(item.getStock()).isEqualTo(2);
    }

    @Test
    @DisplayName("recomputeTotal sums all size values")
    void recomputeTotal_sumsSizes() {
        MerchItem item = item(true);
        Map<String, Integer> sizes = new HashMap<>();
        sizes.put("S", 4);
        sizes.put("M", 6);
        sizes.put("XL", 2);
        item.setStockSizes(sizes); // calls recomputeTotal internally

        assertThat(item.getStock()).isEqualTo(12);
    }

    @Test
    @DisplayName("sell() zero quantity on simple item leaves stock unchanged (boundary)")
    void sell_zeroQuantity_simpleStock_noChange() {
        MerchItem item = item(false);
        item.setStock(5);

        // sell 0 should not throw (no stock check violation)
        item.sell(0, null);

        assertThat(item.getStock()).isEqualTo(5);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private MerchItem item(boolean hasSizes) {
        MerchItem m = new MerchItem("band-1", "Test Shirt", "t-shirt", 8.0, 25.0);
        m.setHasSizes(hasSizes);
        return m;
    }
}
