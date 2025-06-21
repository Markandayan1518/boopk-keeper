package com.bookkeeper.service;

import com.bookkeeper.model.Alert;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.Sale;
import com.bookkeeper.model.SaleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertServiceTest {
    private FarmerService farmerService;
    private JournalService journalService;
    private SaleService saleService;
    private MessageSource messageSource;
    private AlertService alertService;

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
    }

    @Test
    void testCreditLimitBreach() {
        Farmer f = farmerService.addFarmer("A", "City", "", "", BigDecimal.ZERO, new BigDecimal("100"), List.of(), "", "");
        farmerService.recordAdvance(f.getId(), new BigDecimal("150"));
        List<Alert> alerts = alertService.getAlerts();
        assertTrue(alerts.stream().anyMatch(a -> "CreditLimitBreach".equals(a.getType())));
    }

    @Test
    void testLargeAdvanceAlert() {
        Farmer f = farmerService.addFarmer("B", "Town", "", "", BigDecimal.ZERO, new BigDecimal("5000"), List.of(), "", "");
        farmerService.recordAdvance(f.getId(), new BigDecimal("2000"));
        List<Alert> alerts = alertService.getAlerts();
        assertTrue(alerts.stream().anyMatch(a -> "LargeAdvance".equals(a.getType())));
    }

    @Test
    void testOverduePaymentAlert() {
        LocalDate past = LocalDate.now().minusDays(10);
        SaleRequest req = new SaleRequest();
        req.setDate(past);
        req.setInvoiceNumber("INV");
        req.setBuyer("X");
        req.setFlowerType("F");
        req.setQuantity(new BigDecimal("1"));
        req.setRate(new BigDecimal("1"));
        req.setPaymentStatus("PENDING");
        req.setDueDate(past.minusDays(1));
        req.setPaymentDate(null);
        req.setNotes("");
        Sale s = saleService.addSale(req);
        List<Alert> alerts = alertService.getAlerts();
        assertTrue(alerts.stream().anyMatch(a -> "OverduePayment".equals(a.getType()) && a.getEntityId().equals(s.getId())));
    }

    @Test
    void testTamilLocaleMessage() {
        Farmer f = farmerService.addFarmer("C", "Ch", "", "", BigDecimal.ZERO, new BigDecimal("50"), List.of(), "", "");
        farmerService.recordAdvance(f.getId(), new BigDecimal("100"));
        LocaleContextHolder.setLocale(new Locale("ta"));
        List<Alert> alerts = alertService.getAlerts();
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertTrue(alerts.get(0).getMessage().contains("முன்பணம்"));
    }
}
