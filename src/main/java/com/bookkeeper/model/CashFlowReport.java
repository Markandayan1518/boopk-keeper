package com.bookkeeper.model;

import java.math.BigDecimal;

public class CashFlowReport {
    private BigDecimal totalInflows;
    private BigDecimal totalOutflows;
    private BigDecimal netFlow;

    public BigDecimal getTotalInflows() { return totalInflows; }
    public void setTotalInflows(BigDecimal totalInflows) { this.totalInflows = totalInflows; }

    public BigDecimal getTotalOutflows() { return totalOutflows; }
    public void setTotalOutflows(BigDecimal totalOutflows) { this.totalOutflows = totalOutflows; }

    public BigDecimal getNetFlow() { return netFlow; }
    public void setNetFlow(BigDecimal netFlow) { this.netFlow = netFlow; }
}
