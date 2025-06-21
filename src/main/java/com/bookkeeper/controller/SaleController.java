package com.bookkeeper.controller;

import com.bookkeeper.model.Sale;
import com.bookkeeper.model.SaleRequest;
import com.bookkeeper.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<Sale> listSales(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            @RequestParam(value = "buyer", required = false) String buyer,
            @RequestParam(value = "flowerType", required = false) String flowerType,
            @RequestParam(value = "paymentStatus", required = false) String paymentStatus) {
        return saleService.listSales().stream()
                .filter(s -> date == null || s.getDate().equals(LocalDate.parse(date)))
                .filter(s -> invoiceNumber == null || s.getInvoiceNumber().equalsIgnoreCase(invoiceNumber))
                .filter(s -> buyer == null || s.getBuyer().equalsIgnoreCase(buyer))
                .filter(s -> flowerType == null || s.getFlowerType().equalsIgnoreCase(flowerType))
                .filter(s -> paymentStatus == null || s.getPaymentStatus().equalsIgnoreCase(paymentStatus))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable String id) {
        Optional<Sale> sale = saleService.getSaleById(id);
        return sale.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequest request) {
        Sale saved = saleService.addSale(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSale(@PathVariable String id, @RequestBody SaleRequest request) {
        boolean updated = saleService.updateSale(id, request);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable String id) {
        boolean removed = saleService.removeSale(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
