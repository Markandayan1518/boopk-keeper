package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * Data point for charts and CSV exports.
 */
public class ChartData {
    private String label;
    private BigDecimal value;

    private ChartData(Builder builder) {
        this.label = builder.label;
        this.value = builder.value;
    }

    public static Builder from(ChartData data) {
        return builder()
            .withLabel(data.getLabel())
            .withValue(data.getValue());
    }

    // Getters only - removing setters for immutability
    public String getLabel() { return label; }
    public BigDecimal getValue() { return value; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String label;
        private BigDecimal value;

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public ChartData build() {
            return new ChartData(this);
        }
    }
}
