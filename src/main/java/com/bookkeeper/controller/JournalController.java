package com.bookkeeper.controller;

import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.service.JournalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journal-entries")
public class JournalController {
    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping
    public List<JournalEntry> listEntries(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "refType", required = false) String refType,
            @RequestParam(value = "refId", required = false) String refId,
            @RequestParam(value = "account", required = false) String account) {
        return journalService.listEntries().stream()
                .filter(e -> date == null || e.getDate().equals(LocalDate.parse(date)))
                .filter(e -> refType == null || e.getLines().stream().anyMatch(l -> l.getReferenceType().equalsIgnoreCase(refType)))
                .filter(e -> refId == null || e.getLines().stream().anyMatch(l -> l.getReferenceId().equalsIgnoreCase(refId)))
                .filter(e -> account == null || e.getLines().stream().anyMatch(l -> l.getAccount().equalsIgnoreCase(account)))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getEntry(@PathVariable String id) {
        Optional<JournalEntry> entry = journalService.getEntryById(id);
        return entry.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        JournalEntry saved = journalService.addEntry(entry);
        return saved != null ? ResponseEntity.ok(saved)
                             : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEntry(@PathVariable String id, @RequestBody JournalEntry entry) {
        boolean updated = journalService.updateEntry(id, entry);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        boolean removed = journalService.removeEntry(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
