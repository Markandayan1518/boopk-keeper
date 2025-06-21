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

    private Farmer(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.city = builder.city;
        this.contact = builder.contact;
        this.address = builder.address;
        this.commissionRate = builder.commissionRate;
        this.creditLimit = builder.creditLimit;
        this.currentAdvance = builder.currentAdvance;
        this.flowerTypes = builder.flowerTypes;
        this.bankDetails = builder.bankDetails;
        this.remarks = builder.remarks;
    }

    public static Builder builder() { return new Builder(); }

    public static Builder from(Farmer farmer) {
        return builder()
            .withId(farmer.getId())
            .withName(farmer.getName())
            .withCity(farmer.getCity())
            .withContact(farmer.getContact())
            .withAddress(farmer.getAddress())
            .withCommissionRate(farmer.getCommissionRate())
            .withCreditLimit(farmer.getCreditLimit())
            .withCurrentAdvance(farmer.getCurrentAdvance())
            .withFlowerTypes(farmer.getFlowerTypes())
            .withBankDetails(farmer.getBankDetails())
            .withRemarks(farmer.getRemarks());
    }

    public static class Builder {
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

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withName(String name) { this.name = name; return this; }
        public Builder withCity(String city) { this.city = city; return this; }
        public Builder withContact(String contact) { this.contact = contact; return this; }
        public Builder withAddress(String address) { this.address = address; return this; }
        public Builder withCommissionRate(BigDecimal rate) { this.commissionRate = rate; return this; }
        public Builder withCreditLimit(BigDecimal limit) { this.creditLimit = limit; return this; }
        public Builder withCurrentAdvance(BigDecimal advance) { this.currentAdvance = advance; return this; }
        public Builder withFlowerTypes(List<String> types) { this.flowerTypes = types; return this; }
        public Builder withBankDetails(String details) { this.bankDetails = details; return this; }
        public Builder withRemarks(String remarks) { this.remarks = remarks; return this; }

        public Farmer build() { return new Farmer(this); }
    }

    // getters only - removing setters for immutability
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public BigDecimal getCurrentAdvance() { return currentAdvance; }
    public List<String> getFlowerTypes() { return flowerTypes; }
    public String getBankDetails() { return bankDetails; }
    public String getRemarks() { return remarks; }
}
