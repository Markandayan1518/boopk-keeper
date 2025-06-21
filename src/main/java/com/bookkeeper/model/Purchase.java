package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Purchase {
    private String id;
    private LocalDate date;
    private String farmerId;
    private String flowerType;
    private String quality;
    private BigDecimal quantity;
    private BigDecimal ratePaid;
    private BigDecimal cogs;
    private BigDecimal marketRate;
    private BigDecimal totalValue;
    private BigDecimal marketValue;
    private BigDecimal variance;
    private String paymentMode;
    private String receiptNumber;
    private String notes;

    // Getters
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getFarmerId() { return farmerId; }
    public String getFlowerType() { return flowerType; }
    public String getQuality() { return quality; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getRatePaid() { return ratePaid; }
    public BigDecimal getCogs() { return cogs; }
    public BigDecimal getMarketRate() { return marketRate; }
    public BigDecimal getTotalValue() { return totalValue; }
    public BigDecimal getMarketValue() { return marketValue; }
    public BigDecimal getVariance() { return variance; }
    public String getPaymentMode() { return paymentMode; }
    public String getReceiptNumber() { return receiptNumber; }
    public String getNotes() { return notes; }

    private Purchase(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.farmerId = builder.farmerId;
        this.flowerType = builder.flowerType;
        this.quality = builder.quality;
        this.quantity = builder.quantity;
        this.ratePaid = builder.ratePaid;
        this.cogs = builder.cogs;
        this.marketRate = builder.marketRate;
        this.totalValue = builder.totalValue;
        this.marketValue = builder.marketValue;
        this.variance = builder.variance;
        this.paymentMode = builder.paymentMode;
        this.receiptNumber = builder.receiptNumber;
        this.notes = builder.notes;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(Purchase purchase) {
        return builder()
            .withId(purchase.getId())
            .withDate(purchase.getDate())
            .withFarmerId(purchase.getFarmerId())
            .withFlowerType(purchase.getFlowerType())
            .withQuality(purchase.getQuality())
            .withQuantity(purchase.getQuantity())
            .withRatePaid(purchase.getRatePaid())
            .withCogs(purchase.getCogs())
            .withMarketRate(purchase.getMarketRate())
            .withTotalValue(purchase.getTotalValue())
            .withMarketValue(purchase.getMarketValue())
            .withVariance(purchase.getVariance())
            .withPaymentMode(purchase.getPaymentMode())
            .withReceiptNumber(purchase.getReceiptNumber())
            .withNotes(purchase.getNotes());
    }

    public static class Builder {
        private String id;
        private LocalDate date;
        private String farmerId;
        private String flowerType;
        private String quality;
        private BigDecimal quantity;
        private BigDecimal ratePaid;
        private BigDecimal cogs;
        private BigDecimal marketRate;
        private BigDecimal totalValue;
        private BigDecimal marketValue;
        private BigDecimal variance;
        private String paymentMode;
        private String receiptNumber;
        private String notes;

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withFarmerId(String farmerId) { this.farmerId = farmerId; return this; }
        public Builder withFlowerType(String flowerType) { this.flowerType = flowerType; return this; }
        public Builder withQuality(String quality) { this.quality = quality; return this; }
        public Builder withQuantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public Builder withRatePaid(BigDecimal ratePaid) { this.ratePaid = ratePaid; return this; }
        public Builder withCogs(BigDecimal cogs) { this.cogs = cogs; return this; }
        public Builder withMarketRate(BigDecimal marketRate) { this.marketRate = marketRate; return this; }
        public Builder withTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; return this; }
        public Builder withMarketValue(BigDecimal marketValue) { this.marketValue = marketValue; return this; }
        public Builder withVariance(BigDecimal variance) { this.variance = variance; return this; }
        public Builder withPaymentMode(String paymentMode) { this.paymentMode = paymentMode; return this; }
        public Builder withReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }

        public Purchase build() { return new Purchase(this); }
    }
}
