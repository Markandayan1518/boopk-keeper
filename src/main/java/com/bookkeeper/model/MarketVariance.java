package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Summary of variance between purchase cost and market value by flower type.
 */
public class MarketVariance {
    private String flowerType;
    private BigDecimal totalVariance;

    public String getFlowerType() { return flowerType; }
    public void setFlowerType(String flowerType) { this.flowerType = flowerType; }

    public BigDecimal getTotalVariance() { return totalVariance; }
    public void setTotalVariance(BigDecimal totalVariance) { this.totalVariance = totalVariance; }
}
