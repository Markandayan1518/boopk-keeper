package com.bookkeeper.service;

import com.bookkeeper.model.Advance;
import com.bookkeeper.model.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdvanceServiceTest {
    private FarmerService farmerService;
    private JournalService journalService;
    private AdvanceService advanceService;

    @BeforeEach
    void setUp() {
        farmerService = new FarmerService();
        journalService = new JournalService();
        advanceService = new AdvanceService(farmerService, journalService);
    }

    @Test
    void testAddAdvanceWithinLimit() {
        Farmer f = farmerService.addFarmer("John", "City", "123", "Addr", BigDecimal.ZERO, new BigDecimal("500"), Arrays.asList(), "", "");
        Advance adv = new Advance();
        adv.setDate(LocalDate.now());
        adv.setFarmerId(f.getId());
        adv.setAmount(new BigDecimal("200"));
        adv.setPaymentMode("Cash");
        adv.setReceiptNumber("R1");
        adv.setRemarks("Test");
        Advance saved = advanceService.addAdvance(adv);
        assertNotNull(saved);
        assertEquals(new BigDecimal("200"), farmerService.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testAddAdvanceOverLimit() {
        Farmer f = farmerService.addFarmer("Jane", "Town", "456", "Addr2", BigDecimal.ZERO, new BigDecimal("100"), Arrays.asList(), "", "");
        Advance adv = new Advance();
        adv.setDate(LocalDate.now());
        adv.setFarmerId(f.getId());
        adv.setAmount(new BigDecimal("150"));
        Advance saved = advanceService.addAdvance(adv);
        assertNull(saved);
        assertEquals(BigDecimal.ZERO, farmerService.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testListAndRemoveAdvance() {
        Farmer f = farmerService.addFarmer("Ann", "Ville", "789", "Addr3", BigDecimal.ZERO, new BigDecimal("500"), Arrays.asList(), "", "");
        Advance adv = new Advance();
        adv.setDate(LocalDate.now()); adv.setFarmerId(f.getId()); adv.setAmount(new BigDecimal("50"));
        Advance saved = advanceService.addAdvance(adv);
        List<Advance> list = advanceService.listAdvances();
        assertTrue(list.stream().anyMatch(a -> a.getId().equals(saved.getId())));

        boolean removed = advanceService.removeAdvance(saved.getId());
        assertTrue(removed);
        assertFalse(advanceService.getAdvanceById(saved.getId()).isPresent());
    }
}
