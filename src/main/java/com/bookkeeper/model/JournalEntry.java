package com.bookkeeper.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a double-entry journal entry linking to business transactions.
 */
public class JournalEntry {
    private String id;
    private LocalDate date;
    private String description;
    private List<JournalEntryLine> lines;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<JournalEntryLine> getLines() { return lines; }
    public void setLines(List<JournalEntryLine> lines) { this.lines = lines; }
}
