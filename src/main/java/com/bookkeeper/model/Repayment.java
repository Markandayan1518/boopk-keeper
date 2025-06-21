package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a repayment against a farmer's advance.
 */
public class Repayment {
    private String id;
    private LocalDate date;
    private String farmerId;
    private BigDecimal amount;
    private String paymentMode;
    private String receiptNumber;
    private String remarks;

    private Repayment(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.farmerId = builder.farmerId;
        this.amount = builder.amount;
        this.paymentMode = builder.paymentMode;
        this.receiptNumber = builder.receiptNumber;
        this.remarks = builder.remarks;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(Repayment repayment) {
        return builder()
            .withId(repayment.getId())
            .withDate(repayment.getDate())
            .withFarmerId(repayment.getFarmerId())
            .withAmount(repayment.getAmount())
            .withPaymentMode(repayment.getPaymentMode())
            .withReceiptNumber(repayment.getReceiptNumber())
            .withRemarks(repayment.getRemarks());
    }

    // Getters
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getFarmerId() { return farmerId; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMode() { return paymentMode; }
    public String getReceiptNumber() { return receiptNumber; }
    public String getRemarks() { return remarks; }

    public static class Builder {
        private String id;
        private LocalDate date;
        private String farmerId;
        private BigDecimal amount;
        private String paymentMode;
        private String receiptNumber;
        private String remarks;

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withFarmerId(String farmerId) { this.farmerId = farmerId; return this; }
        public Builder withAmount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder withPaymentMode(String paymentMode) { this.paymentMode = paymentMode; return this; }
        public Builder withReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
        public Builder withRemarks(String remarks) { this.remarks = remarks; return this; }

        public Repayment build() { return new Repayment(this); }
    }
}
