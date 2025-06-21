package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Summary metrics for a farmer: totals and current advance.
 */
public class FarmerSummary {
    private String farmerId;
    private String name;
    private BigDecimal totalPurchases;
    private BigDecimal totalAdvances;
    private BigDecimal totalRepayments;
    private BigDecimal currentAdvance;

    private FarmerSummary(Builder builder) {
        this.farmerId = builder.farmerId;
        this.name = builder.name;
        this.totalPurchases = builder.totalPurchases;
        this.totalAdvances = builder.totalAdvances;
        this.totalRepayments = builder.totalRepayments;
        this.currentAdvance = builder.currentAdvance;
    }

    public static Builder from(FarmerSummary summary) {
        return builder()
            .withFarmerId(summary.getFarmerId())
            .withName(summary.getName())
            .withTotalPurchases(summary.getTotalPurchases())
            .withTotalAdvances(summary.getTotalAdvances())
            .withTotalRepayments(summary.getTotalRepayments())
            .withCurrentAdvance(summary.getCurrentAdvance());
    }

    // Getters only - removing setters for immutability
    public String getFarmerId() { return farmerId; }
    public String getName() { return name; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public BigDecimal getTotalAdvances() { return totalAdvances; }
    public BigDecimal getTotalRepayments() { return totalRepayments; }
    public BigDecimal getCurrentAdvance() { return currentAdvance; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String farmerId;
        private String name;
        private BigDecimal totalPurchases;
        private BigDecimal totalAdvances;
        private BigDecimal totalRepayments;
        private BigDecimal currentAdvance;

        public Builder withFarmerId(String farmerId) { this.farmerId = farmerId; return this; }
        public Builder withName(String name) { this.name = name; return this; }
        public Builder withTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; return this; }
        public Builder withTotalAdvances(BigDecimal totalAdvances) { this.totalAdvances = totalAdvances; return this; }
        public Builder withTotalRepayments(BigDecimal totalRepayments) { this.totalRepayments = totalRepayments; return this; }
        public Builder withCurrentAdvance(BigDecimal currentAdvance) { this.currentAdvance = currentAdvance; return this; }

        public FarmerSummary build() { return new FarmerSummary(this); }
    }
}
