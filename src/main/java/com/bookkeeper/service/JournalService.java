package com.bookkeeper.service;

import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Service for managing journal entries in double-entry format. Ensures debits equal credits.
 */
@Service
public class JournalService {
    private final Map<String, JournalEntry> entries = new ConcurrentHashMap<>();

    /**
     * Adds a new journal entry if debits equal credits. Returns saved entry or null if invalid.
     */
    public JournalEntry addEntry(JournalEntry entry) {
        BigDecimal totalDebit = entry.getLines().stream()
                .map(JournalEntryLine::getDebit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entry.getLines().stream()
                .map(JournalEntryLine::getCredit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebit.compareTo(totalCredit) != 0) {
            return null; // invalid balanced entry
        }
        String dateStr = entry.getDate() != null
                ? StringUtils.remove(entry.getDate().toString(), '-')
                : StringUtils.remove(LocalDate.now().toString(), '-');
        String baseId = "je" + dateStr;
        String id = baseId;
        int suffix = 1;
        while (entries.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }

        JournalEntry newEntry = JournalEntry.builder()
            .withId(id)
            .withDate(entry.getDate())
            .withDescription(entry.getDescription())
            .withLines(entry.getLines())
            .build();

        entries.put(id, newEntry);
        return newEntry;
    }

    public List<JournalEntry> listEntries() {
        return new ArrayList<>(entries.values());
    }

    public Optional<JournalEntry> getEntryById(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    /**
     * Updates existing entry if balanced. Returns true if updated.
     */
    public boolean updateEntry(String id, JournalEntry updated) {
        if (!entries.containsKey(id)) {
            return false;
        }
        BigDecimal totalDebit = updated.getLines().stream()
                .map(JournalEntryLine::getDebit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = updated.getLines().stream()
                .map(JournalEntryLine::getCredit)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebit.compareTo(totalCredit) != 0) {
            return false;
        }

        JournalEntry newEntry = JournalEntry.builder()
            .withId(id)
            .withDate(updated.getDate())
            .withDescription(updated.getDescription())
            .withLines(updated.getLines())
            .build();

        entries.put(id, newEntry);
        return true;
    }

    public boolean removeEntry(String id) {
        return entries.remove(id) != null;
    }
}
