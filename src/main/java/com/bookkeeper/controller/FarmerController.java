package com.bookkeeper.controller;

import com.bookkeeper.model.Advance;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.FarmerSummary;
import com.bookkeeper.model.Purchase;
import com.bookkeeper.model.Repayment;
import com.bookkeeper.service.FarmerService;
import com.bookkeeper.service.PurchaseService;
import com.bookkeeper.service.AdvanceService;
import com.bookkeeper.service.RepaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
        Farmer updatedFarmer = Farmer.builder()
            .withId(id)
            .withName(farmer.getName())
            .withCity(farmer.getCity())
            .withContact(farmer.getContact())
            .withAddress(farmer.getAddress())
            .withCommissionRate(farmer.getCommissionRate())
            .withCreditLimit(farmer.getCreditLimit())
            .withCurrentAdvance(farmer.getCurrentAdvance())
            .withFlowerTypes(farmer.getFlowerTypes())
            .withBankDetails(farmer.getBankDetails())
            .withRemarks(farmer.getRemarks())
            .build();
        boolean updated = farmerService.updateFarmer(updatedFarmer);
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
            return FarmerSummary.builder()
                .withFarmerId(f.getId())
                .withName(f.getName())
                .withTotalPurchases(purchaseService.listPurchases().stream()
                    .filter(p -> p.getFarmerId().equals(f.getId()))
                    .map(Purchase::getTotalValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .withTotalAdvances(advanceService.listAdvances().stream()
                    .filter(a -> a.getFarmerId().equals(f.getId()))
                    .map(Advance::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .withTotalRepayments(repaymentService.listRepayments().stream()
                    .filter(r -> r.getFarmerId().equals(f.getId()))
                    .map(Repayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .withCurrentAdvance(f.getCurrentAdvance())
                .build();
        }).collect(Collectors.toList());
    }

    @GetMapping("/farmers/{id}/summary")
    public FarmerSummary getFarmerSummary(@PathVariable String id) {
         // Calculate total purchases, advances, repayments for farmer
         Map<String, BigDecimal> purchasesByFarmer = purchaseService.listPurchases().stream()
             .collect(Collectors.groupingBy(Purchase::getFarmerId,
                 Collectors.reducing(BigDecimal.ZERO, Purchase::getTotalValue, BigDecimal::add)));
         Map<String, BigDecimal> advancesByFarmer = advanceService.listAdvances().stream()
             .collect(Collectors.groupingBy(Advance::getFarmerId,
                 Collectors.reducing(BigDecimal.ZERO, Advance::getAmount, BigDecimal::add)));
         Map<String, BigDecimal> repaymentsByFarmer = repaymentService.listRepayments().stream()
             .collect(Collectors.groupingBy(Repayment::getFarmerId,
                 Collectors.reducing(BigDecimal.ZERO, Repayment::getAmount, BigDecimal::add)));
        return FarmerSummary.builder()
            .withFarmerId(id)
            .withName(farmerService.getFarmerById(id).map(Farmer::getName).orElse(null))
            .withTotalPurchases(purchasesByFarmer.getOrDefault(id, BigDecimal.ZERO))
            .withTotalAdvances(advancesByFarmer.getOrDefault(id, BigDecimal.ZERO))
            .withTotalRepayments(repaymentsByFarmer.getOrDefault(id, BigDecimal.ZERO))
            .withCurrentAdvance(farmerService.getFarmerById(id)
                .map(Farmer::getCurrentAdvance).orElse(BigDecimal.ZERO))
            .build();
    }
}
