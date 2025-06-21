package com.bookkeeper.service;

import com.bookkeeper.model.Repayment;
import com.bookkeeper.model.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepaymentServiceTest {
    private FarmerService farmerService;
    private JournalService journalService;
    private AdvanceService advanceService;
    private RepaymentService repaymentService;

    @BeforeEach
    void setUp() {
        farmerService = new FarmerService();
        journalService = new JournalService();
        advanceService = new AdvanceService(farmerService, journalService);
        repaymentService = new RepaymentService(farmerService, journalService);
    }

    @Test
    void testAddRepaymentSuccess() {
        Farmer f = farmerService.addFarmer("F", "C", "", "", BigDecimal.ZERO, new BigDecimal("200"), Arrays.asList(), "", "");
        advanceService.addAdvance(new com.bookkeeper.model.Advance() {{ setDate(LocalDate.now()); setFarmerId(f.getId()); setAmount(new BigDecimal("100")); }});
        Repayment rep = new Repayment();
        rep.setDate(LocalDate.now());
        rep.setFarmerId(f.getId());
        rep.setAmount(new BigDecimal("50"));
        Repayment saved = repaymentService.addRepayment(rep);
        assertNotNull(saved);
        assertEquals(new BigDecimal("50"), farmerService.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testAddRepaymentWithoutAdvance() {
        Repayment rep = new Repayment();
        rep.setDate(LocalDate.now());
        rep.setFarmerId("nope");
        rep.setAmount(new BigDecimal("10"));
        assertNull(repaymentService.addRepayment(rep));
    }

    @Test
    void testListAndRemoveRepayment() {
        Farmer f = farmerService.addFarmer("X", "Y", "", "", BigDecimal.ZERO, new BigDecimal("100"), Arrays.asList(), "", "");
        advanceService.addAdvance(new com.bookkeeper.model.Advance() {{ setDate(LocalDate.now()); setFarmerId(f.getId()); setAmount(new BigDecimal("80")); }});
        Repayment rep = new Repayment(); rep.setDate(LocalDate.now()); rep.setFarmerId(f.getId()); rep.setAmount(new BigDecimal("30"));
        Repayment saved = repaymentService.addRepayment(rep);
        List<Repayment> list = repaymentService.listRepayments();
        assertTrue(list.stream().anyMatch(r -> r.getId().equals(saved.getId())));

        boolean removed = repaymentService.removeRepayment(saved.getId());
        assertTrue(removed);
        assertFalse(repaymentService.getRepaymentById(saved.getId()).isPresent());
    }
}
