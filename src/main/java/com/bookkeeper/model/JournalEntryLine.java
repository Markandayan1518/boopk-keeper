package com.bookkeeper.model;

import java.math.BigDecimal;

/**
 * A single line in a double-entry journal entry.
 */
public class JournalEntryLine {
    private String account;
    private BigDecimal debit;
    private BigDecimal credit;
    private String referenceType;   // e.g., "Purchase", "Sale", "Advance", "Repayment"
    private String referenceId;     // ID of the linked document

    private JournalEntryLine(Builder builder) {
        this.account = builder.account;
        this.debit = builder.debit;
        this.credit = builder.credit;
        this.referenceType = builder.referenceType;
        this.referenceId = builder.referenceId;
    }

    public static Builder from(JournalEntryLine line) {
        return builder()
            .withAccount(line.getAccount())
            .withDebit(line.getDebit())
            .withCredit(line.getCredit())
            .withReferenceType(line.getReferenceType())
            .withReferenceId(line.getReferenceId());
    }

    // Getters only - removing setters for immutability
    public String getAccount() { return account; }
    public BigDecimal getDebit() { return debit; }
    public BigDecimal getCredit() { return credit; }
    public String getReferenceType() { return referenceType; }
    public String getReferenceId() { return referenceId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String account;
        private BigDecimal debit;
        private BigDecimal credit;
        private String referenceType;
        private String referenceId;

        public Builder withAccount(String account) { this.account = account; return this; }
        public Builder withDebit(BigDecimal debit) { this.debit = debit; return this; }
        public Builder withCredit(BigDecimal credit) { this.credit = credit; return this; }
        public Builder withReferenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public Builder withReferenceId(String referenceId) { this.referenceId = referenceId; return this; }

        public JournalEntryLine build() { return new JournalEntryLine(this); }
    }
}
