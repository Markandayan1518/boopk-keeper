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

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }
    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }
    public BigDecimal getNetPayout() { return netPayout; }
    public void setNetPayout(BigDecimal netPayout) { this.netPayout = netPayout; }
}
