package com.bookkeeper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MarketRateServiceTest {
    private MarketRateService svc;

    @BeforeEach
    void setup() {
        svc = new MarketRateService();
    }

    @Test
    void testSetAndGetRate() {
        LocalDate d = LocalDate.of(2025,6,21);
        svc.setRate(d, "Rose", new BigDecimal("5.50"));
        Optional<BigDecimal> rate = svc.getRate(d, "Rose");
        assertTrue(rate.isPresent());
        assertEquals(new BigDecimal("5.50"), rate.get());
    }

    @Test
    void testGetRatesWideFormat() {
        LocalDate d = LocalDate.of(2025,6,22);
        svc.setRate(d, "Jasmine", new BigDecimal("3.25"));
        svc.setRate(d, "Marigold", new BigDecimal("2.00"));
        Map<String, BigDecimal> rates = svc.getRates(d);
        assertEquals(2, rates.size());
        assertEquals(new BigDecimal("3.25"), rates.get("Jasmine"));
    }

    @Test
    void testGetAllRatesHistory() {
        LocalDate d1 = LocalDate.of(2025,6,20);
        LocalDate d2 = LocalDate.of(2025,6,21);
        svc.setRate(d1, "Lily", new BigDecimal("4.00"));
        svc.setRate(d2, "Rose", new BigDecimal("5.00"));
        Map<LocalDate, Map<String, BigDecimal>> history = svc.getAllRates();
        assertTrue(history.containsKey(d1));
        assertTrue(history.containsKey(d2));
    }
}
