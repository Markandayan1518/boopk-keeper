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
        purchaseService.addPurchase(com.bookkeeper.model.PurchaseRequest.builder()
            .withDate(LocalDate.now())
            .withFarmerId(f.getId())
            .withFlowerType("X")
            .withQuality("Q")
            .withQuantity(new BigDecimal("2"))
            .withRatePaid(new BigDecimal("10"))
            .withCogs(BigDecimal.ZERO)
            .withPaymentMode("Cash")
            .withReceiptNumber("R")
            .withNotes("")
            .build());
        // Add sale
        saleService.addSale(com.bookkeeper.model.SaleRequest.builder()
            .withDate(LocalDate.now())
            .withInvoiceNumber("I1")
            .withBuyer("B")
            .withFlowerType("X")
            .withQuantity(new BigDecimal("1"))
            .withRate(new BigDecimal("5"))
            .withPaymentStatus("PAID")
            .withPaymentDate(LocalDate.now())
            .withNotes("")
            .build());
        // Add advance and repayment
        advanceService.addAdvance(com.bookkeeper.model.Advance.builder()
            .withDate(LocalDate.now())
            .withFarmerId(f.getId())
            .withAmount(new BigDecimal("100"))
            .build());
        repaymentService.addRepayment(com.bookkeeper.model.Repayment.builder()
            .withDate(LocalDate.now())
            .withFarmerId(f.getId())
            .withAmount(new BigDecimal("30"))
            .build());

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
        purchaseService.addPurchase(com.bookkeeper.model.PurchaseRequest.builder()
            .withDate(d)
            .withFarmerId(f.getId())
            .withFlowerType("Y")
            .withQuality("Q")
            .withQuantity(new BigDecimal("5"))
            .withRatePaid(new BigDecimal("2"))
            .withCogs(BigDecimal.ZERO)
            .withPaymentMode("Cash")
            .withReceiptNumber("R")
            .withNotes("")
            .build());
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
        purchaseService.addPurchase(com.bookkeeper.model.PurchaseRequest.builder()
            .withDate(d1)
            .withFarmerId(f.getId())
            .withFlowerType("A")
            .withQuality("Q")
            .withQuantity(new BigDecimal("1"))
            .withRatePaid(new BigDecimal("3"))
            .withCogs(new BigDecimal("2"))
            .withPaymentMode("")
            .withReceiptNumber("")
            .withNotes("")
            .build());
        purchaseService.addPurchase(com.bookkeeper.model.PurchaseRequest.builder()
            .withDate(d2)
            .withFarmerId(f.getId())
            .withFlowerType("B")
            .withQuality("Q")
            .withQuantity(new BigDecimal("2"))
            .withRatePaid(new BigDecimal("4"))
            .withCogs(BigDecimal.ZERO)
            .withPaymentMode("")
            .withReceiptNumber("")
            .withNotes("")
            .build());
        List<ChartData> pbm = dashboardService.getPurchasesByMonth();
        assertEquals(2, pbm.size());
        List<CogsBreakdown> cogs = dashboardService.getCogsBreakdown();
        assertEquals(2, cogs.size());
    }
}
