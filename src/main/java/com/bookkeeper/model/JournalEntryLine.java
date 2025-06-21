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

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public BigDecimal getDebit() { return debit; }
    public void setDebit(BigDecimal debit) { this.debit = debit; }

    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
}
