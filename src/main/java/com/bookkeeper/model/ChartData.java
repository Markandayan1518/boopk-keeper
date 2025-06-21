package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Data point for charts and CSV exports.
 */
public class ChartData {
    private String label;
    private BigDecimal value;

    public ChartData() {}
    public ChartData(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}
