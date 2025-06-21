package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PurchaseRequest {
    private LocalDate date;
    private String farmerId;
    private String flowerType;
    private String quality;
    private BigDecimal quantity;
    private BigDecimal ratePaid;
    private BigDecimal cogs;
    private String paymentMode;
    private String receiptNumber;
    private String notes;

    // Getters only - removing setters for immutability
    public LocalDate getDate() { return date; }
    public String getFarmerId() { return farmerId; }
    public String getFlowerType() { return flowerType; }
    public String getQuality() { return quality; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getRatePaid() { return ratePaid; }
    public BigDecimal getCogs() { return cogs; }
    public String getPaymentMode() { return paymentMode; }
    public String getReceiptNumber() { return receiptNumber; }
    public String getNotes() { return notes; }

    public static Builder from(PurchaseRequest request) {
        return builder()
            .withDate(request.getDate())
            .withFarmerId(request.getFarmerId())
            .withFlowerType(request.getFlowerType())
            .withQuality(request.getQuality())
            .withQuantity(request.getQuantity())
            .withRatePaid(request.getRatePaid())
            .withCogs(request.getCogs())
            .withPaymentMode(request.getPaymentMode())
            .withReceiptNumber(request.getReceiptNumber())
            .withNotes(request.getNotes());
    }

    private PurchaseRequest(Builder builder) {
        this.date = builder.date;
        this.farmerId = builder.farmerId;
        this.flowerType = builder.flowerType;
        this.quality = builder.quality;
        this.quantity = builder.quantity;
        this.ratePaid = builder.ratePaid;
        this.cogs = builder.cogs;
        this.paymentMode = builder.paymentMode;
        this.receiptNumber = builder.receiptNumber;
        this.notes = builder.notes;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private LocalDate date;
        private String farmerId;
        private String flowerType;
        private String quality;
        private BigDecimal quantity;
        private BigDecimal ratePaid;
        private BigDecimal cogs;
        private String paymentMode;
        private String receiptNumber;
        private String notes;

        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withFarmerId(String farmerId) { this.farmerId = farmerId; return this; }
        public Builder withFlowerType(String flowerType) { this.flowerType = flowerType; return this; }
        public Builder withQuality(String quality) { this.quality = quality; return this; }
        public Builder withQuantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public Builder withRatePaid(BigDecimal ratePaid) { this.ratePaid = ratePaid; return this; }
        public Builder withCogs(BigDecimal cogs) { this.cogs = cogs; return this; }
        public Builder withPaymentMode(String paymentMode) { this.paymentMode = paymentMode; return this; }
        public Builder withReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }

        public PurchaseRequest build() { return new PurchaseRequest(this); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseRequest)) return false;
        PurchaseRequest that = (PurchaseRequest) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(farmerId, that.farmerId) &&
                Objects.equals(flowerType, that.flowerType) &&
                Objects.equals(quality, that.quality) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(ratePaid, that.ratePaid) &&
                Objects.equals(cogs, that.cogs) &&
                Objects.equals(paymentMode, that.paymentMode) &&
                Objects.equals(receiptNumber, that.receiptNumber) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, farmerId, flowerType, quality, quantity, ratePaid, cogs, paymentMode, receiptNumber, notes);
    }
}
