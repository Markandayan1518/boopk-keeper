package com.bookkeeper.model;

import java.math.BigDecimal;
import java.util.List;

public class Farmer {
    private String id;
    private String name;
    private String city;
    private String contact;
    private String address;
    private BigDecimal commissionRate;
    private BigDecimal creditLimit;
    private BigDecimal currentAdvance;
    private List<String> flowerTypes;
    private String bankDetails;
    private String remarks;

    public Farmer() {}

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public BigDecimal getCurrentAdvance() { return currentAdvance; }
    public void setCurrentAdvance(BigDecimal currentAdvance) { this.currentAdvance = currentAdvance; }

    public List<String> getFlowerTypes() { return flowerTypes; }
    public void setFlowerTypes(List<String> flowerTypes) { this.flowerTypes = flowerTypes; }

    public String getBankDetails() { return bankDetails; }
    public void setBankDetails(String bankDetails) { this.bankDetails = bankDetails; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
