package com.bookkeeper.service;

import com.bookkeeper.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final FarmerService farmerService;
    private final PurchaseService purchaseService;
    private final SaleService saleService;
    private final AdvanceService advanceService;
    private final RepaymentService repaymentService;

    public DashboardService(FarmerService farmerService,
                            PurchaseService purchaseService,
                            SaleService saleService,
                            AdvanceService advanceService,
                            RepaymentService repaymentService) {
        this.farmerService = farmerService;
        this.purchaseService = purchaseService;
        this.saleService = saleService;
        this.advanceService = advanceService;
        this.repaymentService = repaymentService;
    }

    public DashboardKpis getKpis() {
        DashboardKpis k = new DashboardKpis();
        List<Farmer> farmers = farmerService.listFarmers();
        k.setTotalFarmers(farmers.size());
        k.setTotalPurchases(purchaseService.listPurchases().stream()
                .map(Purchase::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        k.setTotalSales(saleService.listSales().stream()
                .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal adv = advanceService.listAdvances().stream()
                .map(Advance::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rep = repaymentService.listRepayments().stream()
                .map(Repayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        k.setTotalAdvances(adv);
        k.setTotalRepayments(rep);
        k.setTotalOutstanding(farmers.stream()
                .map(Farmer::getCurrentAdvance).reduce(BigDecimal.ZERO, BigDecimal::add));
        return k;
    }

    public List<FarmerPayoutSummary> getFarmerPayoutSummaries() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(p -> fmt.format(p.getDate()) + "|" + p.getFarmerId()))
            .entrySet().stream().map(e -> {
                String[] parts = e.getKey().split("\\|");
                String month = parts[0];
                String farmerId = parts[1];
                BigDecimal total = e.getValue().stream()
                        .map(Purchase::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                Farmer f = farmerService.getFarmerById(farmerId).orElse(null);
                BigDecimal commission = BigDecimal.ZERO;
                if (f != null && f.getCommissionRate() != null) {
                    commission = total.multiply(f.getCommissionRate())
                        .setScale(1, RoundingMode.HALF_UP);
                }
                FarmerPayoutSummary s = new FarmerPayoutSummary();
                s.setFarmerId(farmerId);
                s.setMonth(month);
                s.setTotalPurchases(total);
                s.setCommission(commission);
                s.setNetPayout(total.subtract(commission).setScale(1, RoundingMode.HALF_UP));
                return s;
            }).collect(Collectors.toList());
    }

    public List<CogsBreakdown> getCogsBreakdown() {
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(Purchase::getFlowerType))
            .entrySet().stream().map(e -> {
                CogsBreakdown c = new CogsBreakdown();
                c.setFlowerType(e.getKey());
                c.setTotalCogs(e.getValue().stream()
                        .map(Purchase::getCogs).reduce(BigDecimal.ZERO, BigDecimal::add));
                return c;
            }).collect(Collectors.toList());
    }

    public List<MarketVariance> getMarketVariance() {
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(Purchase::getFlowerType))
            .entrySet().stream().map(e -> {
                MarketVariance m = new MarketVariance();
                m.setFlowerType(e.getKey());
                m.setTotalVariance(e.getValue().stream()
                        .map(Purchase::getVariance).reduce(BigDecimal.ZERO, BigDecimal::add));
                return m;
            }).collect(Collectors.toList());
    }

    public CashFlowReport getCashFlow() {
        BigDecimal inflows = saleService.listSales().stream()
            .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(repaymentService.listRepayments().stream()
                .map(Repayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal outflows = purchaseService.listPurchases().stream()
            .map(Purchase::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(advanceService.listAdvances().stream()
                .map(Advance::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        CashFlowReport r = new CashFlowReport();
        r.setTotalInflows(inflows);
        r.setTotalOutflows(outflows);
        r.setNetFlow(inflows.subtract(outflows));
        return r;
    }

    /** Purchases total by month for charts/reports */
    public List<ChartData> getPurchasesByMonth() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(p -> fmt.format(p.getDate())))
            .entrySet().stream()
            .map(e -> new ChartData(e.getKey(), e.getValue().stream()
                .map(Purchase::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add)))
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }

    /** Sales total by flower type for charts/reports */
    public List<ChartData> getSalesByFlower() {
        return saleService.listSales().stream()
            .collect(Collectors.groupingBy(Sale::getFlowerType))
            .entrySet().stream()
            .map(e -> new ChartData(e.getKey(), e.getValue().stream()
                .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add)))
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }

    /** Advances total over time (by date) for charts/reports */
    public List<ChartData> getAdvancesOverTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return advanceService.listAdvances().stream()
            .collect(Collectors.groupingBy(a -> fmt.format(a.getDate())))
            .entrySet().stream()
            .map(e -> new ChartData(e.getKey(), e.getValue().stream()
                .map(Advance::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)))
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }
}
