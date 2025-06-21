package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Aggregated KPIs for dashboard.
 */
public class DashboardKpis {
    private long totalFarmers;
    private BigDecimal totalPurchases;
    private BigDecimal totalSales;
    private BigDecimal totalAdvances;
    private BigDecimal totalRepayments;
    private BigDecimal totalOutstanding;

    private DashboardKpis(Builder builder) {
        this.totalFarmers = builder.totalFarmers;
        this.totalPurchases = builder.totalPurchases;
        this.totalSales = builder.totalSales;
        this.totalAdvances = builder.totalAdvances;
        this.totalRepayments = builder.totalRepayments;
        this.totalOutstanding = builder.totalOutstanding;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(DashboardKpis kpis) {
        return builder()
            .withTotalFarmers(kpis.getTotalFarmers())
            .withTotalPurchases(kpis.getTotalPurchases())
            .withTotalSales(kpis.getTotalSales())
            .withTotalAdvances(kpis.getTotalAdvances())
            .withTotalRepayments(kpis.getTotalRepayments())
            .withTotalOutstanding(kpis.getTotalOutstanding());
    }

    // Getters
    public long getTotalFarmers() { return totalFarmers; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public BigDecimal getTotalSales() { return totalSales; }
    public BigDecimal getTotalAdvances() { return totalAdvances; }
    public BigDecimal getTotalRepayments() { return totalRepayments; }
    public BigDecimal getTotalOutstanding() { return totalOutstanding; }

    public static class Builder {
        private long totalFarmers;
        private BigDecimal totalPurchases;
        private BigDecimal totalSales;
        private BigDecimal totalAdvances;
        private BigDecimal totalRepayments;
        private BigDecimal totalOutstanding;

        public Builder withTotalFarmers(long totalFarmers) { this.totalFarmers = totalFarmers; return this; }
        public Builder withTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; return this; }
        public Builder withTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; return this; }
        public Builder withTotalAdvances(BigDecimal totalAdvances) { this.totalAdvances = totalAdvances; return this; }
        public Builder withTotalRepayments(BigDecimal totalRepayments) { this.totalRepayments = totalRepayments; return this; }
        public Builder withTotalOutstanding(BigDecimal totalOutstanding) { this.totalOutstanding = totalOutstanding; return this; }

        public DashboardKpis build() { return new DashboardKpis(this); }
    }
}
