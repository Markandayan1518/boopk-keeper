package com.bookkeeper.controller;

import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.FarmerSummary;
import com.bookkeeper.service.FarmerService;
import com.bookkeeper.service.PurchaseService;
import com.bookkeeper.service.AdvanceService;
import com.bookkeeper.service.RepaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {
    private final FarmerService farmerService;
    private final PurchaseService purchaseService;
    private final AdvanceService advanceService;
    private final RepaymentService repaymentService;

    public FarmerController(FarmerService farmerService,
                            PurchaseService purchaseService,
                            AdvanceService advanceService,
                            RepaymentService repaymentService) {
        this.farmerService = farmerService;
        this.purchaseService = purchaseService;
        this.advanceService = advanceService;
        this.repaymentService = repaymentService;
    }

    @GetMapping
    public List<Farmer> listFarmers(@RequestParam(value = "q", required = false) String q) {
        String term = q != null ? q.toLowerCase() : null;
        return farmerService.listFarmers().stream()
            .filter(f -> term == null || f.getName().toLowerCase().contains(term)
                    || f.getCity().toLowerCase().contains(term)
                    || f.getId().toLowerCase().contains(term))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmer(@PathVariable String id) {
        Optional<Farmer> farmer = farmerService.getFarmerById(id);
        return farmer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Farmer> createFarmer(@RequestBody Farmer farmer) {
        Farmer created = farmerService.addFarmer(
                farmer.getName(),
                farmer.getCity(),
                farmer.getContact(),
                farmer.getAddress(),
                farmer.getCommissionRate(),
                farmer.getCreditLimit(),
                farmer.getFlowerTypes(),
                farmer.getBankDetails(),
                farmer.getRemarks()
        );
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFarmer(@PathVariable String id, @RequestBody Farmer farmer) {
        farmer.setId(id);
        boolean updated = farmerService.updateFarmer(farmer);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable String id) {
        boolean deleted = farmerService.removeFarmer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/advance")
    public ResponseEntity<Void> recordAdvance(@PathVariable String id,
                                              @RequestParam("amount") BigDecimal amount) {
        boolean ok = farmerService.recordAdvance(id, amount);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/repayment")
    public ResponseEntity<Void> recordRepayment(@PathVariable String id,
                                                @RequestParam("amount") BigDecimal amount) {
        boolean ok = farmerService.recordRepayment(id, amount);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /** Returns summary metrics per farmer */
    @GetMapping("/summary")
    public List<FarmerSummary> getFarmerSummaries() {
        return farmerService.listFarmers().stream().map(f -> {
            FarmerSummary s = new FarmerSummary();
            s.setFarmerId(f.getId());
            s.setName(f.getName());
            s.setTotalPurchases(purchaseService.listPurchases().stream()
                    .filter(p -> p.getFarmerId().equals(f.getId()))
                    .map(p -> p.getTotalValue()).reduce(BigDecimal.ZERO, BigDecimal::add));
            s.setTotalAdvances(advanceService.listAdvances().stream()
                    .filter(a -> a.getFarmerId().equals(f.getId()))
                    .map(a -> a.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            s.setTotalRepayments(repaymentService.listRepayments().stream()
                    .filter(r -> r.getFarmerId().equals(f.getId()))
                    .map(r -> r.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            s.setCurrentAdvance(f.getCurrentAdvance());
            return s;
        }).collect(Collectors.toList());
    }
}
