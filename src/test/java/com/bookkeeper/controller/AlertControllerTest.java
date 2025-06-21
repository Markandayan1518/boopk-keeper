package com.bookkeeper.controller;

import com.bookkeeper.model.Alert;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.SaleRequest;
import com.bookkeeper.service.AlertService;
import com.bookkeeper.service.FarmerService;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertControllerTest {
    private FarmerService farmerService;
    private JournalService journalService;
    private SaleService saleService;
    private AlertService alertService;
    private AlertController alertController;

    @BeforeEach
    void setUp() {
        farmerService = new FarmerService();
        journalService = new JournalService();
        saleService = new SaleService(journalService);
        alertService = new AlertService(farmerService, saleService);
        alertController = new AlertController(alertService);
    }

    @Test
    void testGetAlertsEmpty() {
        List<Alert> alerts = alertController.getAlerts();
        assertTrue(alerts.isEmpty());
    }

    @Test
    void testGetAlertsWithData() {
        Farmer f = farmerService.addFarmer("Bob", "Town", "", "", BigDecimal.ZERO,
                new BigDecimal("50"), List.of(), "", "");
        farmerService.recordAdvance(f.getId(), new BigDecimal("100"));

        SaleRequest req = new SaleRequest();
        req.setDate(LocalDate.now().minusDays(10));
        req.setInvoiceNumber("INV1");
        req.setBuyer("Cust");
        req.setFlowerType("Rose");
        req.setQuantity(BigDecimal.ONE);
        req.setRate(BigDecimal.ONE);
        req.setPaymentStatus("PENDING");
        req.setDueDate(LocalDate.now().minusDays(5));
        req.setPaymentDate(null);
        req.setNotes("");
        saleService.addSale(req);

        List<Alert> expected = alertService.getAlerts();
        List<Alert> actual = alertController.getAlerts();
        assertEquals(expected.size(), actual.size());
        assertFalse(actual.isEmpty());
        assertEquals(expected.get(0).getType(), actual.get(0).getType());
    }
}
