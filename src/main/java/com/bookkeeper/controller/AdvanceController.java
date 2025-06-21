package com.bookkeeper.controller;

import com.bookkeeper.model.Advance;
import com.bookkeeper.service.AdvanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advances")
public class AdvanceController {
    private final AdvanceService advanceService;

    public AdvanceController(AdvanceService advanceService) {
        this.advanceService = advanceService;
    }

    @GetMapping
    public List<Advance> listAdvances(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "farmerId", required = false) String farmerId) {
        return advanceService.listAdvances().stream()
                .filter(a -> date == null || a.getDate().equals(LocalDate.parse(date)))
                .filter(a -> farmerId == null || a.getFarmerId().equals(farmerId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advance> getAdvance(@PathVariable String id) {
        Optional<Advance> adv = advanceService.getAdvanceById(id);
        return adv.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Advance> createAdvance(@RequestBody Advance advance) {
        Advance saved = advanceService.addAdvance(advance);
        return saved != null ? ResponseEntity.ok(saved)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvance(@PathVariable String id) {
        boolean removed = advanceService.removeAdvance(id);
        return removed ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
