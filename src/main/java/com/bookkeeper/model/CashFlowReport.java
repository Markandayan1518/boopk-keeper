package com.bookkeeper.model;

import java.math.BigDecimal;

public class CashFlowReport {
    private BigDecimal totalInflows;
    private BigDecimal totalOutflows;
    private BigDecimal netFlow;

    private CashFlowReport(Builder builder) {
        this.totalInflows = builder.totalInflows;
        this.totalOutflows = builder.totalOutflows;
        this.netFlow = builder.netFlow;
    }

    public static Builder from(CashFlowReport report) {
        return builder()
            .withTotalInflows(report.getTotalInflows())
            .withTotalOutflows(report.getTotalOutflows())
            .withNetFlow(report.getNetFlow());
    }

    // Getters only - removing setters for immutability
    public BigDecimal getTotalInflows() { return totalInflows; }
    public BigDecimal getTotalOutflows() { return totalOutflows; }
    public BigDecimal getNetFlow() { return netFlow; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal totalInflows;
        private BigDecimal totalOutflows;
        private BigDecimal netFlow;

        public Builder withTotalInflows(BigDecimal totalInflows) {
            this.totalInflows = totalInflows;
            return this;
        }

        public Builder withTotalOutflows(BigDecimal totalOutflows) {
            this.totalOutflows = totalOutflows;
            return this;
        }

        public Builder withNetFlow(BigDecimal netFlow) {
            this.netFlow = netFlow;
            return this;
        }

        public CashFlowReport build() {
            return new CashFlowReport(this);
        }
    }
}
