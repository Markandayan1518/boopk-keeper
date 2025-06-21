package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Summary of variance between purchase cost and market value by flower type.
 */
public class MarketVariance {
    private String flowerType;
    private BigDecimal totalVariance;

    private MarketVariance(Builder builder) {
        this.flowerType = builder.flowerType;
        this.totalVariance = builder.totalVariance;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(MarketVariance variance) {
        return builder()
            .withFlowerType(variance.getFlowerType())
            .withTotalVariance(variance.getTotalVariance());
    }

    // Getters only - removing setters for immutability
    public String getFlowerType() { return flowerType; }
    public BigDecimal getTotalVariance() { return totalVariance; }

    public static class Builder {
        private String flowerType;
        private BigDecimal totalVariance;

        public Builder withFlowerType(String flowerType) { this.flowerType = flowerType; return this; }
        public Builder withTotalVariance(BigDecimal totalVariance) { this.totalVariance = totalVariance; return this; }

        public MarketVariance build() { return new MarketVariance(this); }
    }
}
