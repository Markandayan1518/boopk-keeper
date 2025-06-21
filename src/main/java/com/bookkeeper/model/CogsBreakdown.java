package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Breakdown of COGS by flower type.
 */
public class CogsBreakdown {
    private String flowerType;
    private BigDecimal totalCogs;

    public String getFlowerType() { return flowerType; }
    public void setFlowerType(String flowerType) { this.flowerType = flowerType; }

    public BigDecimal getTotalCogs() { return totalCogs; }
    public void setTotalCogs(BigDecimal totalCogs) { this.totalCogs = totalCogs; }
}
