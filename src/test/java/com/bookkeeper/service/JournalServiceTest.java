package com.bookkeeper.service;

import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JournalServiceTest {
    private JournalService journalService;

    @BeforeEach
    void setUp() {
        journalService = new JournalService();
    }

    @Test
    void testAddBalancedEntry() {
        JournalEntry entry = JournalEntry.builder()
            .withDate(LocalDate.now())
            .withDescription("Test")
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("A").withDebit(new BigDecimal("100")).withCredit(null)
                    .build(),
                JournalEntryLine.builder()
                    .withAccount("B").withDebit(null).withCredit(new BigDecimal("100"))
                    .build()
            ))
            .build();
        JournalEntry saved = journalService.addEntry(entry);
        assertNotNull(saved);
        assertTrue(saved.getId().startsWith("je"));
    }

    @Test
    void testAddUnbalancedEntry() {
        JournalEntry entry = JournalEntry.builder()
            .withDate(LocalDate.now())
            .withDescription("Unbalanced")
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("A").withDebit(new BigDecimal("100")).build(),
                JournalEntryLine.builder()
                    .withAccount("B").withCredit(new BigDecimal("90")).build()
            ))
            .build();
        assertNull(journalService.addEntry(entry));
    }

    @Test
    void testListGetUpdateRemove() {
        // add
        JournalEntry entry = JournalEntry.builder()
            .withDate(LocalDate.now())
            .withDescription("Balance")
            .withLines(Arrays.asList(
                JournalEntryLine.builder().withAccount("A").withDebit(new BigDecimal("50")).build(),
                JournalEntryLine.builder().withAccount("B").withCredit(new BigDecimal("50")).build()
            ))
            .build();
        JournalEntry saved = journalService.addEntry(entry);
        assertNotNull(saved);
        String id = saved.getId();

        // list
        List<JournalEntry> all = journalService.listEntries();
        assertTrue(all.stream().anyMatch(e -> e.getId().equals(id)));

        // get
        assertTrue(journalService.getEntryById(id).isPresent());

        // update balanced
        JournalEntry updated = JournalEntry.from(saved)
            .withDescription("Updated")
            .build();
        assertTrue(journalService.updateEntry(id, updated));

        // update unbalanced
        JournalEntryLine unbalancedLine = JournalEntryLine.builder()
            .withAccount("A")
            .withDebit(new BigDecimal("60"))
            .build();
        updated = JournalEntry.from(updated)
            .withLines(Arrays.asList(unbalancedLine, saved.getLines().get(1)))
            .build();
        assertFalse(journalService.updateEntry(id, updated));

        // remove
        assertTrue(journalService.removeEntry(id));
        assertFalse(journalService.getEntryById(id).isPresent());
    }
}
