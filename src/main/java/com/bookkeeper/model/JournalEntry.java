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

    private JournalEntry(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.description = builder.description;
        this.lines = builder.lines;
    }

    public static Builder from(JournalEntry entry) {
        return builder()
            .withId(entry.getId())
            .withDate(entry.getDate())
            .withDescription(entry.getDescription())
            .withLines(entry.getLines());
    }

    // Getters only - removing setters for immutability
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public List<JournalEntryLine> getLines() { return lines; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private LocalDate date;
        private String description;
        private List<JournalEntryLine> lines;

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withDate(LocalDate date) { this.date = date; return this; }
        public Builder withDescription(String description) { this.description = description; return this; }
        public Builder withLines(List<JournalEntryLine> lines) { this.lines = lines; return this; }

        public JournalEntry build() { return new JournalEntry(this); }
    }
}
