package com.bookkeeper.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleRequest {
    private LocalDate date;
    private String invoiceNumber;
    private String buyer;
    private String flowerType;
    private BigDecimal quantity;
    private BigDecimal rate;
    private String paymentStatus;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String notes;

    public static Builder from(SaleRequest request) {
        return builder()
            .withDate(request.getDate())
            .withInvoiceNumber(request.getInvoiceNumber())
            .withBuyer(request.getBuyer())
            .withFlowerType(request.getFlowerType())
            .withQuantity(request.getQuantity())
            .withRate(request.getRate())
            .withPaymentStatus(request.getPaymentStatus())
            .withDueDate(request.getDueDate())
            .withPaymentDate(request.getPaymentDate())
            .withNotes(request.getNotes());
    }

    // Getters only - removing setters for immutability
    public LocalDate getDate() { return date; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getBuyer() { return buyer; }
    public String getFlowerType() { return flowerType; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getRate() { return rate; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String getNotes() { return notes; }

    private SaleRequest(Builder builder) {
        this.date = builder.date;
        this.invoiceNumber = builder.invoiceNumber;
        this.buyer = builder.buyer;
        this.flowerType = builder.flowerType;
        this.quantity = builder.quantity;
        this.rate = builder.rate;
        this.paymentStatus = builder.paymentStatus;
        this.dueDate = builder.dueDate;
        this.paymentDate = builder.paymentDate;
        this.notes = builder.notes;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private LocalDate date;
        private String invoiceNumber;
        private String buyer;
        private String flowerType;
        private BigDecimal quantity;
        private BigDecimal rate;
        private String paymentStatus;
        private LocalDate dueDate;
        private LocalDate paymentDate;
        private String notes;

        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public Builder withBuyer(String buyer) { this.buyer = buyer; return this; }
        public Builder withFlowerType(String flowerType) { this.flowerType = flowerType; return this; }
        public Builder withQuantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public Builder withRate(BigDecimal rate) { this.rate = rate; return this; }
        public Builder withPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder withDueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public Builder withPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }

        public SaleRequest build() { return new SaleRequest(this); }
    }
}
