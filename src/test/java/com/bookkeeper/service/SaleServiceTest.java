package com.bookkeeper.service;

import com.bookkeeper.model.Sale;
import com.bookkeeper.model.SaleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaleServiceTest {
    private JournalService journalService;
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        journalService = new JournalService();
        saleService = new SaleService(journalService);
    }

    private SaleRequest makeRequest(LocalDate date, String invoice, String buyer, String flower, BigDecimal qty, BigDecimal rate) {
        SaleRequest req = new SaleRequest();
        req.setDate(date);
        req.setInvoiceNumber(invoice);
        req.setBuyer(buyer);
        req.setFlowerType(flower);
        req.setQuantity(qty);
        req.setRate(rate);
        req.setPaymentStatus("PENDING");
        req.setDueDate(date.plusDays(5));
        req.setPaymentDate(null);
        req.setNotes("note");
        return req;
    }

    @Test
    void testAddAndGetSale() {
        LocalDate d = LocalDate.of(2025, 6, 21);
        SaleRequest req = makeRequest(d, "INV1", "Cust", "Rose", new BigDecimal("2"), new BigDecimal("10"));
        Sale s = saleService.addSale(req);
        assertNotNull(s.getId());
        assertEquals(new BigDecimal("20"), s.getTotalAmount());
        List<Sale> list = saleService.listSales();
        assertTrue(list.contains(s));
    }

    @Test
    void testUpdateSale() {
        LocalDate d = LocalDate.now();
        SaleRequest req = makeRequest(d, "INV2", "B", "Jasmine", new BigDecimal("1"), new BigDecimal("5"));
        Sale s = saleService.addSale(req);
        SaleRequest upd = makeRequest(d, "INV2", "B2", "Jasmine", new BigDecimal("2"), new BigDecimal("7"));
        boolean ok = saleService.updateSale(s.getId(), upd);
        assertTrue(ok);
        Sale s2 = saleService.getSaleById(s.getId()).get();
        assertEquals(new BigDecimal("14"), s2.getTotalAmount());
        assertEquals("B2", s2.getBuyer());
    }

    @Test
    void testRemoveSale() {
        SaleRequest req = makeRequest(LocalDate.now(), "INV3", "C", "Lily", new BigDecimal("3"), new BigDecimal("2"));
        Sale s = saleService.addSale(req);
        boolean removed = saleService.removeSale(s.getId());
        assertTrue(removed);
        assertFalse(saleService.getSaleById(s.getId()).isPresent());
    }
}
