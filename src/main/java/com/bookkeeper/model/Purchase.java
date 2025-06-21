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

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public BigDecimal getMarketRate() { return marketRate; }
    public void setMarketRate(BigDecimal marketRate) { this.marketRate = marketRate; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public BigDecimal getMarketValue() { return marketValue; }
    public void setMarketValue(BigDecimal marketValue) { this.marketValue = marketValue; }

    public BigDecimal getVariance() { return variance; }
    public void setVariance(BigDecimal variance) { this.variance = variance; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
