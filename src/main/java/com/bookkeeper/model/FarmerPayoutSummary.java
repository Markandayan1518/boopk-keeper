package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Summary of farmer payouts per month.
 */
public class FarmerPayoutSummary {
    private String farmerId;
    private String month; // e.g., "2025-06"
    private BigDecimal totalPurchases;
    private BigDecimal commission;
    private BigDecimal netPayout;

    private FarmerPayoutSummary(Builder builder) {
        this.farmerId = builder.farmerId;
        this.month = builder.month;
        this.totalPurchases = builder.totalPurchases;
        this.commission = builder.commission;
        this.netPayout = builder.netPayout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(FarmerPayoutSummary summary) {
        return builder()
            .withFarmerId(summary.getFarmerId())
            .withMonth(summary.getMonth())
            .withTotalPurchases(summary.getTotalPurchases())
            .withCommission(summary.getCommission())
            .withNetPayout(summary.getNetPayout());
    }

    // Getters only - removing setters for immutability
    public String getFarmerId() { return farmerId; }
    public String getMonth() { return month; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public BigDecimal getCommission() { return commission; }
    public BigDecimal getNetPayout() { return netPayout; }

    public static class Builder {
        private String farmerId;
        private String month;
        private BigDecimal totalPurchases;
        private BigDecimal commission;
        private BigDecimal netPayout;

        public Builder withFarmerId(String farmerId) { this.farmerId = farmerId; return this; }
        public Builder withMonth(String month) { this.month = month; return this; }
        public Builder withTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; return this; }
        public Builder withCommission(BigDecimal commission) { this.commission = commission; return this; }
        public Builder withNetPayout(BigDecimal netPayout) { this.netPayout = netPayout; return this; }

        public FarmerPayoutSummary build() { return new FarmerPayoutSummary(this); }
    }
}
