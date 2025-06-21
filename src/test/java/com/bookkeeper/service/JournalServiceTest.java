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
        JournalEntry entry = new JournalEntry();
        entry.setDate(LocalDate.now());
        entry.setDescription("Test");
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("A"); dr.setDebit(new BigDecimal("100")); dr.setCredit(null);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("B"); cr.setDebit(null); cr.setCredit(new BigDecimal("100"));
        entry.setLines(Arrays.asList(dr, cr));
        JournalEntry saved = journalService.addEntry(entry);
        assertNotNull(saved);
        assertTrue(saved.getId().startsWith("je"));
    }

    @Test
    void testAddUnbalancedEntry() {
        JournalEntry entry = new JournalEntry();
        entry.setDate(LocalDate.now());
        entry.setDescription("Unbalanced");
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("A"); dr.setDebit(new BigDecimal("100")); dr.setCredit(null);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("B"); cr.setDebit(null); cr.setCredit(new BigDecimal("90"));
        entry.setLines(Arrays.asList(dr, cr));
        assertNull(journalService.addEntry(entry));
    }

    @Test
    void testListGetUpdateRemove() {
        // add
        JournalEntry entry = new JournalEntry();
        entry.setDate(LocalDate.now());
        entry.setDescription("Balance");
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("A"); dr.setDebit(new BigDecimal("50")); dr.setCredit(null);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("B"); cr.setDebit(null); cr.setCredit(new BigDecimal("50"));
        entry.setLines(Arrays.asList(dr, cr));
        JournalEntry saved = journalService.addEntry(entry);
        assertNotNull(saved);
        String id = saved.getId();
        // list
        List<JournalEntry> all = journalService.listEntries();
        assertTrue(all.stream().anyMatch(e -> e.getId().equals(id)));
        // get
        assertTrue(journalService.getEntryById(id).isPresent());
        // update balanced
        saved.setDescription("Updated");
        assertTrue(journalService.updateEntry(id, saved));
        // update unbalanced
        saved.getLines().get(0).setDebit(new BigDecimal("60"));
        assertFalse(journalService.updateEntry(id, saved));
        // remove
        assertTrue(journalService.removeEntry(id));
        assertFalse(journalService.getEntryById(id).isPresent());
    }
}
