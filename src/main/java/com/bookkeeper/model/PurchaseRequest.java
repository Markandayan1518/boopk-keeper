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

    // getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getFlowerType() { return flowerType; }
    public void setFlowerType(String flowerType) { this.flowerType = flowerType; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getRatePaid() { return ratePaid; }
    public void setRatePaid(BigDecimal ratePaid) { this.ratePaid = ratePaid; }

    public BigDecimal getCogs() { return cogs; }
    public void setCogs(BigDecimal cogs) { this.cogs = cogs; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

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
