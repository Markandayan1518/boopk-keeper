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

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }

    public BigDecimal getTotalAdvances() { return totalAdvances; }
    public void setTotalAdvances(BigDecimal totalAdvances) { this.totalAdvances = totalAdvances; }

    public BigDecimal getTotalRepayments() { return totalRepayments; }
    public void setTotalRepayments(BigDecimal totalRepayments) { this.totalRepayments = totalRepayments; }

    public BigDecimal getCurrentAdvance() { return currentAdvance; }
    public void setCurrentAdvance(BigDecimal currentAdvance) { this.currentAdvance = currentAdvance; }
}
