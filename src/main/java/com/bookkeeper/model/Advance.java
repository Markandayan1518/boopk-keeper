package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an advance payment made to a farmer.
 */
public class Advance {
    private String id;
    private LocalDate date;
    private String farmerId;
    private BigDecimal amount;
    private String paymentMode;
    private String receiptNumber;
    private String remarks;

    private Advance(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.farmerId = builder.farmerId;
        this.amount = builder.amount;
        this.paymentMode = builder.paymentMode;
        this.receiptNumber = builder.receiptNumber;
        this.remarks = builder.remarks;
    }

    public Advance() {}

    public static Builder from(Advance advance) {
        return builder()
            .withId(advance.getId())
            .withDate(advance.getDate())
            .withFarmerId(advance.getFarmerId())
            .withAmount(advance.getAmount())
            .withPaymentMode(advance.getPaymentMode())
            .withReceiptNumber(advance.getReceiptNumber())
            .withRemarks(advance.getRemarks());
    }

    // Getters only - removing setters for immutability
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getFarmerId() { return farmerId; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMode() { return paymentMode; }
    public String getReceiptNumber() { return receiptNumber; }
    public String getRemarks() { return remarks; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private LocalDate date;
        private String farmerId;
        private BigDecimal amount;
        private String paymentMode;
        private String receiptNumber;
        private String remarks;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder withFarmerId(String farmerId) {
            this.farmerId = farmerId;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
            return this;
        }

        public Builder withReceiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
            return this;
        }

        public Builder withRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Advance build() {
            return new Advance(this);
        }
    }
}
