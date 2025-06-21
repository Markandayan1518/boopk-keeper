package com.bookkeeper.service;

import com.bookkeeper.model.ChartData;
import com.bookkeeper.model.CogsBreakdown;
import com.bookkeeper.model.DashboardKpis;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.FarmerPayoutSummary;
import java.util.Arrays;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DashboardServiceTest {
    private DashboardService dashboardService;
    private FarmerService farmerService;
    private PurchaseService purchaseService;
    private SaleService saleService;
    private AdvanceService advanceService;
    private RepaymentService repaymentService;

    @BeforeEach
    void setUp() {
        // Initialize dependencies
        JournalService journalService = new JournalService();
        MarketRateService marketRateService = new MarketRateService();
        farmerService = new FarmerService();
        purchaseService = new PurchaseService(marketRateService, journalService);
        saleService = new SaleService(journalService);
        advanceService = new AdvanceService(farmerService, journalService);
        repaymentService = new RepaymentService(farmerService, journalService);
        dashboardService = new DashboardService(farmerService, purchaseService, saleService, advanceService, repaymentService);
    }

    @Test
    void testGetKpis() {
        // Setup data
        Farmer f = farmerService.addFarmer("A", "C", "", "", BigDecimal.ZERO, new BigDecimal("1000"), Arrays.asList(), "", "");
        // No transactions
        DashboardKpis kpis = dashboardService.getKpis();
        assertEquals(1, kpis.getTotalFarmers());
        assertEquals(BigDecimal.ZERO, kpis.getTotalPurchases());
        assertEquals(BigDecimal.ZERO, kpis.getTotalSales());
        assertEquals(BigDecimal.ZERO, kpis.getTotalAdvances());
        assertEquals(BigDecimal.ZERO, kpis.getTotalRepayments());
        assertEquals(BigDecimal.ZERO, kpis.getTotalOutstanding());

        // Add purchase
        purchaseService.addPurchase(new com.bookkeeper.model.PurchaseRequest() {{
            setDate(LocalDate.now()); setFarmerId(f.getId()); setFlowerType("X"); setQuality("Q"); setQuantity(new BigDecimal("2")); setRatePaid(new BigDecimal("10")); setCogs(BigDecimal.ZERO);
            setPaymentMode("Cash"); setReceiptNumber("R"); setNotes("");
        }});
        // Add sale
        saleService.addSale(new com.bookkeeper.model.SaleRequest() {{
            setDate(LocalDate.now()); setInvoiceNumber("I1"); setBuyer("B"); setFlowerType("X"); setQuantity(new BigDecimal("1")); setRate(new BigDecimal("5")); setPaymentStatus("PAID"); setPaymentDate(LocalDate.now()); setNotes("");
        }});
        // Add advance and repayment
        advanceService.addAdvance(new com.bookkeeper.model.Advance() {{ setDate(LocalDate.now()); setFarmerId(f.getId()); setAmount(new BigDecimal("100")); }});
        repaymentService.addRepayment(new com.bookkeeper.model.Repayment() {{ setDate(LocalDate.now()); setFarmerId(f.getId()); setAmount(new BigDecimal("30")); }});

        kpis = dashboardService.getKpis();
        assertEquals(new BigDecimal("20"), kpis.getTotalPurchases());
        assertEquals(new BigDecimal("5"), kpis.getTotalSales());
        assertEquals(new BigDecimal("100"), kpis.getTotalAdvances());
        assertEquals(new BigDecimal("30"), kpis.getTotalRepayments());
        assertEquals(new BigDecimal("70"), kpis.getTotalOutstanding());
    }

    @Test
    void testGetFarmerPayoutSummaries() {
        Farmer f = farmerService.addFarmer("F", "C", "", "", new BigDecimal("0.10"), new BigDecimal("1000"), Arrays.asList(), "", "");
        LocalDate d = LocalDate.of(2025, 6, 21);
        purchaseService.addPurchase(new com.bookkeeper.model.PurchaseRequest() {{
            setDate(d); setFarmerId(f.getId()); setFlowerType("Y"); setQuality("Q"); setQuantity(new BigDecimal("5")); setRatePaid(new BigDecimal("2")); setCogs(BigDecimal.ZERO);
            setPaymentMode("Cash"); setReceiptNumber("R"); setNotes("");
        }});
        List<FarmerPayoutSummary> sums = dashboardService.getFarmerPayoutSummaries();
        assertEquals(1, sums.size());
        FarmerPayoutSummary s = sums.get(0);
        assertEquals(f.getId(), s.getFarmerId());
        assertEquals("2025-06", s.getMonth());
        assertEquals(new BigDecimal("10"), s.getTotalPurchases());
        assertEquals(new BigDecimal("1.0"), s.getCommission());
        assertEquals(new BigDecimal("9.0"), s.getNetPayout());
    }

    @Test
    void testChartDataAndCogs() {
        Farmer f = farmerService.addFarmer("G", "H", "", "", BigDecimal.ZERO, new BigDecimal("1000"), Arrays.asList(), "", "");
        LocalDate d1 = LocalDate.of(2025, 6, 20);
        LocalDate d2 = LocalDate.of(2025, 7, 1);
        purchaseService.addPurchase(new com.bookkeeper.model.PurchaseRequest() {{ setDate(d1); setFarmerId(f.getId()); setFlowerType("A"); setQuality("Q"); setQuantity(new BigDecimal("1")); setRatePaid(new BigDecimal("3")); setCogs(new BigDecimal("2"));
            setPaymentMode(""); setReceiptNumber(""); setNotes(""); }});
        purchaseService.addPurchase(new com.bookkeeper.model.PurchaseRequest() {{ setDate(d2); setFarmerId(f.getId()); setFlowerType("B"); setQuality("Q"); setQuantity(new BigDecimal("2")); setRatePaid(new BigDecimal("4")); setCogs(BigDecimal.ZERO);
            setPaymentMode(""); setReceiptNumber(""); setNotes(""); }});
        List<ChartData> pbm = dashboardService.getPurchasesByMonth();
        assertEquals(2, pbm.size());
        List<CogsBreakdown> cogs = dashboardService.getCogsBreakdown();
        assertEquals(2, cogs.size());
    }
}
