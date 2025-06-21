package com.bookkeeper.controller;

import com.bookkeeper.model.Repayment;
import com.bookkeeper.service.RepaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {
    private final RepaymentService repaymentService;

    public RepaymentController(RepaymentService repaymentService) {
        this.repaymentService = repaymentService;
    }

    @GetMapping
    public List<Repayment> listRepayments(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "farmerId", required = false) String farmerId) {
        return repaymentService.listRepayments().stream()
                .filter(r -> date == null || r.getDate().equals(LocalDate.parse(date)))
                .filter(r -> farmerId == null || r.getFarmerId().equals(farmerId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repayment> getRepayment(@PathVariable String id) {
        Optional<Repayment> rep = repaymentService.getRepaymentById(id);
        return rep.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Repayment> createRepayment(@RequestBody Repayment repayment) {
        Repayment saved = repaymentService.addRepayment(repayment);
        return saved != null ? ResponseEntity.ok(saved)
                             : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepayment(@PathVariable String id) {
        boolean removed = repaymentService.removeRepayment(id);
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
