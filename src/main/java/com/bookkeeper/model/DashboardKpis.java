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

    // getters/setters
    public long getTotalFarmers() { return totalFarmers; }
    public void setTotalFarmers(long totalFarmers) { this.totalFarmers = totalFarmers; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }
    public BigDecimal getTotalSales() { return totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
    public BigDecimal getTotalAdvances() { return totalAdvances; }
    public void setTotalAdvances(BigDecimal totalAdvances) { this.totalAdvances = totalAdvances; }
    public BigDecimal getTotalRepayments() { return totalRepayments; }
    public void setTotalRepayments(BigDecimal totalRepayments) { this.totalRepayments = totalRepayments; }
    public BigDecimal getTotalOutstanding() { return totalOutstanding; }
    public void setTotalOutstanding(BigDecimal totalOutstanding) { this.totalOutstanding = totalOutstanding; }
}
