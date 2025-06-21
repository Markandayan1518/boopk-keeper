package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Breakdown of COGS by flower type.
 */
public class CogsBreakdown {
    private String flowerType;
    private BigDecimal totalCogs;

    private CogsBreakdown(Builder builder) {
        this.flowerType = builder.flowerType;
        this.totalCogs = builder.totalCogs;
    }

    public static Builder from(CogsBreakdown breakdown) {
        return builder()
            .withFlowerType(breakdown.getFlowerType())
            .withTotalCogs(breakdown.getTotalCogs());
    }

    // Getters only - removing setters for immutability
    public String getFlowerType() { return flowerType; }
    public BigDecimal getTotalCogs() { return totalCogs; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String flowerType;
        private BigDecimal totalCogs;

        public Builder withFlowerType(String flowerType) {
            this.flowerType = flowerType;
            return this;
        }

        public Builder withTotalCogs(BigDecimal totalCogs) {
            this.totalCogs = totalCogs;
            return this;
        }

        public CogsBreakdown build() {
            return new CogsBreakdown(this);
        }
    }
}
