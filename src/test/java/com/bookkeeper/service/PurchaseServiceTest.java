package com.bookkeeper.service;

import com.bookkeeper.model.Purchase;
import com.bookkeeper.model.PurchaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bookkeeper.service.JournalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest {
    private MarketRateService marketRateService;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        marketRateService = new MarketRateService();
        JournalService journalService = new JournalService();
        purchaseService = new PurchaseService(marketRateService, journalService);
    }

    private PurchaseRequest sampleRequest(LocalDate date, String farmerId, String type, BigDecimal qty, BigDecimal ratePaid, BigDecimal cogs) {
        PurchaseRequest req = new PurchaseRequest();
        req.setDate(date);
        req.setFarmerId(farmerId);
        req.setFlowerType(type);
        req.setQuality("A");
        req.setQuantity(qty);
        req.setRatePaid(ratePaid);
        req.setCogs(cogs);
        req.setPaymentMode("Cash");
        req.setReceiptNumber("RCPT123");
        req.setNotes("Test note");
        return req;
    }

    @Test
    void testAddPurchaseCalculationsWithMarketRate() {
        LocalDate date = LocalDate.of(2025, 6, 21);
        marketRateService.setRate(date, "Rose", new BigDecimal("5.00"));
        PurchaseRequest req = sampleRequest(date, "f1", "Rose", new BigDecimal("10"), new BigDecimal("4.50"), new BigDecimal("2.00"));
        Purchase p = purchaseService.addPurchase(req);
        assertNotNull(p.getId());
        BigDecimal expectedTotal = new BigDecimal("4.50").multiply(new BigDecimal("10")).add(new BigDecimal("2.00"));
        assertEquals(expectedTotal, p.getTotalValue());
        assertEquals(new BigDecimal("5.00"), p.getMarketRate());
        BigDecimal expectedMarketValue = new BigDecimal("5.00").multiply(new BigDecimal("10"));
        assertEquals(expectedMarketValue, p.getMarketValue());
        assertEquals(expectedTotal.subtract(expectedMarketValue), p.getVariance());
        assertEquals("Cash", p.getPaymentMode());
        assertEquals("RCPT123", p.getReceiptNumber());
        assertEquals("Test note", p.getNotes());
    }

    @Test
    void testAddPurchaseWithoutMarketRateDefaultsZero() {
        LocalDate date = LocalDate.of(2025, 6, 22);
        PurchaseRequest req = sampleRequest(date, "f2", "Jasmine", new BigDecimal("5"), new BigDecimal("3.00"), BigDecimal.ZERO);
        Purchase p = purchaseService.addPurchase(req);
        assertEquals(BigDecimal.ZERO, p.getMarketRate());
        assertEquals(BigDecimal.ZERO, p.getMarketValue());
        assertEquals(p.getTotalValue(), p.getVariance());
    }

    @Test
    void testIdUniqueness() {
        LocalDate date = LocalDate.of(2025, 6, 21);
        PurchaseRequest req1 = sampleRequest(date, "f3", "Lily", new BigDecimal("2"), new BigDecimal("2.00"), BigDecimal.ZERO);
        PurchaseRequest req2 = sampleRequest(date, "f3", "Lily", new BigDecimal("2"), new BigDecimal("2.00"), BigDecimal.ZERO);
        Purchase p1 = purchaseService.addPurchase(req1);
        Purchase p2 = purchaseService.addPurchase(req2);
        assertEquals(p1.getId() + "_1", p2.getId());
    }

    @Test
    void testListGetUpdateRemove() {
        LocalDate date = LocalDate.now();
        PurchaseRequest req = sampleRequest(date, "f4", "Rose", new BigDecimal("1"), new BigDecimal("1.00"), BigDecimal.ZERO);
        Purchase p = purchaseService.addPurchase(req);
        List<Purchase> list = purchaseService.listPurchases();
        assertTrue(list.contains(p));

        Optional<Purchase> found = purchaseService.getPurchaseById(p.getId());
        assertTrue(found.isPresent());

        // update by changing ratePaid
        req.setRatePaid(new BigDecimal("2.00"));
        boolean updated = purchaseService.updatePurchase(p.getId(), req);
        assertTrue(updated);
        // After update there should still only be one purchase record
        assertEquals(1, purchaseService.listPurchases().size());
        Purchase updatedP = purchaseService.getPurchaseById(p.getId()).get();
        assertEquals(new BigDecimal("2.00").multiply(req.getQuantity()).add(req.getCogs()), updatedP.getTotalValue());

        boolean removed = purchaseService.removePurchase(p.getId());
        assertTrue(removed);
        assertFalse(purchaseService.getPurchaseById(p.getId()).isPresent());
    }
}
