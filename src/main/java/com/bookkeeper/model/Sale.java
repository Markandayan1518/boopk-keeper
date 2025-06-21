package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a sales invoice record.
 */
public class Sale {
    private String id;
    private LocalDate date;
    private String invoiceNumber;
    private String buyer;
    private String flowerType;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String notes;

    private Sale(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.invoiceNumber = builder.invoiceNumber;
        this.buyer = builder.buyer;
        this.flowerType = builder.flowerType;
        this.quantity = builder.quantity;
        this.rate = builder.rate;
        this.totalAmount = builder.totalAmount;
        this.paymentStatus = builder.paymentStatus;
        this.dueDate = builder.dueDate;
        this.paymentDate = builder.paymentDate;
        this.notes = builder.notes;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(Sale sale) {
        return builder()
            .withId(sale.getId())
            .withDate(sale.getDate())
            .withInvoiceNumber(sale.getInvoiceNumber())
            .withBuyer(sale.getBuyer())
            .withFlowerType(sale.getFlowerType())
            .withQuantity(sale.getQuantity())
            .withRate(sale.getRate())
            .withTotalAmount(sale.getTotalAmount())
            .withPaymentStatus(sale.getPaymentStatus())
            .withDueDate(sale.getDueDate())
            .withPaymentDate(sale.getPaymentDate())
            .withNotes(sale.getNotes());
    }

    // Getters
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getBuyer() { return buyer; }
    public String getFlowerType() { return flowerType; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getRate() { return rate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String getNotes() { return notes; }

    public static class Builder {
        private String id;
        private LocalDate date;
        private String invoiceNumber;
        private String buyer;
        private String flowerType;
        private BigDecimal quantity;
        private BigDecimal rate;
        private BigDecimal totalAmount;
        private String paymentStatus;
        private LocalDate dueDate;
        private LocalDate paymentDate;
        private String notes;

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public Builder withBuyer(String buyer) { this.buyer = buyer; return this; }
        public Builder withFlowerType(String flowerType) { this.flowerType = flowerType; return this; }
        public Builder withQuantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public Builder withRate(BigDecimal rate) { this.rate = rate; return this; }
        public Builder withTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder withPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder withDueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public Builder withPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }

        public Sale build() { return new Sale(this); }
    }
}
