package com.bookkeeper.controller;

import com.bookkeeper.model.Alert;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.SaleRequest;
import com.bookkeeper.service.AlertService;
import com.bookkeeper.service.FarmerService;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.service.SaleService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    private MessageSource messageSource;
    private AlertService alertService;
    private AlertController alertController;

    @BeforeEach
    void setUp() {
        farmerService = new FarmerService();
        journalService = new JournalService();
        saleService = new SaleService(journalService);
        ResourceBundleMessageSource src = new ResourceBundleMessageSource();
        src.setBasename("messages");
        src.setDefaultEncoding("UTF-8");
        messageSource = src;
        alertService = new AlertService(farmerService, saleService, messageSource);
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

        SaleRequest req = SaleRequest.builder()
            .withDate(LocalDate.now().minusDays(10))
            .withInvoiceNumber("INV1")
            .withBuyer("Cust")
            .withFlowerType("Rose")
            .withQuantity(BigDecimal.ONE)
            .withRate(BigDecimal.ONE)
            .withPaymentStatus("PENDING")
            .withDueDate(LocalDate.now().minusDays(5))
            .withPaymentDate(null)
            .withNotes("")
            .build();
        saleService.addSale(req);

        List<Alert> expected = alertService.getAlerts();
        List<Alert> actual = alertController.getAlerts();
        assertEquals(expected.size(), actual.size());
        assertFalse(actual.isEmpty());
        assertEquals(expected.get(0).getType(), actual.get(0).getType());
    }
}
