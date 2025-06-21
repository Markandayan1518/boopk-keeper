package com.bookkeeper.controller;

import com.bookkeeper.model.Purchase;
import com.bookkeeper.model.PurchaseRequest;
import com.bookkeeper.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<Purchase> listPurchases(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "farmerId", required = false) String farmerId,
            @RequestParam(value = "flowerType", required = false) String flowerType) {
        return purchaseService.listPurchases().stream()
                .filter(p -> date == null || p.getDate().equals(LocalDate.parse(date)))
                .filter(p -> farmerId == null || p.getFarmerId().equals(farmerId))
                .filter(p -> flowerType == null || p.getFlowerType().equalsIgnoreCase(flowerType))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchase(@PathVariable String id) {
        return purchaseService.getPurchaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequest request) {
        Purchase p = purchaseService.addPurchase(request);
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePurchase(@PathVariable String id,
                                               @RequestBody PurchaseRequest request) {
        boolean updated = purchaseService.updatePurchase(id, request);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable String id) {
        boolean deleted = purchaseService.removePurchase(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
