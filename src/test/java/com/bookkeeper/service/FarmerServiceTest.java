package com.bookkeeper.service;

import com.bookkeeper.model.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FarmerServiceTest {
    private FarmerService service;

    @BeforeEach
    void setUp() {
        service = new FarmerService();
    }

    @Test
    void testAddAndGetFarmer() {
        Farmer f = service.addFarmer("Alice", "Wonderland", "12345", "Somewhere",
                new BigDecimal("0.10"), new BigDecimal("1000"), Arrays.asList("Rose", "Jasmine"), "", "");
        assertNotNull(f.getId());
        Optional<Farmer> found = service.getFarmerById(f.getId());
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testIdUniqueness() {
        Farmer f1 = service.addFarmer("Bob", "Builder", "11111", "Place1",
                BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(), "", "");
        Farmer f2 = service.addFarmer("Bob", "Builder", "22222", "Place2",
                BigDecimal.ONE, BigDecimal.ONE, Arrays.asList(), "", "");
        assertEquals(f1.getId() + "_1", f2.getId());
    }

    @Test
    void testListFarmers() {
        service.addFarmer("A", "B", "", "", BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(), "", "");
        service.addFarmer("C", "D", "", "", BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(), "", "");
        List<Farmer> list = service.listFarmers();
        assertEquals(2, list.size());
    }

    @Test
    void testUpdateFarmer() {
        Farmer f = service.addFarmer("Carl", "City", "", "", BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(), "", "");
        f.setContact("99999");
        boolean updated = service.updateFarmer(f);
        assertTrue(updated);
        Optional<Farmer> found = service.getFarmerById(f.getId());
        assertTrue(found.isPresent());
        assertEquals("99999", found.get().getContact());
    }

    @Test
    void testRemoveFarmer() {
        Farmer f = service.addFarmer("Dave", "Town", "", "", BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(), "", "");
        boolean removed = service.removeFarmer(f.getId());
        assertTrue(removed);
        assertFalse(service.getFarmerById(f.getId()).isPresent());
    }

    @Test
    void testUpdateNonexistent() {
        Farmer fake = new Farmer();
        fake.setId("nope");
        assertFalse(service.updateFarmer(fake));
    }

    @Test
    void testRemoveNonexistent() {
        assertFalse(service.removeFarmer("nothing"));
    }

    @Test
    void testRecordAdvanceSuccess() {
        Farmer f = service.addFarmer("Eve", "City", "", "", BigDecimal.ZERO, new BigDecimal("500"), Arrays.asList(), "", "");
        boolean ok = service.recordAdvance(f.getId(), new BigDecimal("200"));
        assertTrue(ok);
        assertEquals(new BigDecimal("200"), service.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testRecordAdvanceOverLimit() {
        Farmer f = service.addFarmer("Adam", "Town", "", "", BigDecimal.ZERO, new BigDecimal("100"), Arrays.asList(), "", "");
        boolean ok1 = service.recordAdvance(f.getId(), new BigDecimal("80"));
        boolean ok2 = service.recordAdvance(f.getId(), new BigDecimal("30"));
        assertTrue(ok1);
        assertFalse(ok2);
        assertEquals(new BigDecimal("80"), service.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testRecordRepaymentSuccess() {
        Farmer f = service.addFarmer("Sam", "Ville", "", "", BigDecimal.ZERO, new BigDecimal("1000"), Arrays.asList(), "", "");
        service.recordAdvance(f.getId(), new BigDecimal("300"));
        boolean ok = service.recordRepayment(f.getId(), new BigDecimal("150"));
        assertTrue(ok);
        assertEquals(new BigDecimal("150"), service.getFarmerById(f.getId()).get().getCurrentAdvance());
    }

    @Test
    void testRecordRepaymentBelowZero() {
        Farmer f = service.addFarmer("Tom", "Town", "", "", BigDecimal.ZERO, new BigDecimal("100"), Arrays.asList(), "", "");
        boolean ok = service.recordRepayment(f.getId(), new BigDecimal("10"));
        assertFalse(ok);
        assertEquals(BigDecimal.ZERO, service.getFarmerById(f.getId()).get().getCurrentAdvance());
    }
}
