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
        List<Farmer> farmers = farmerService.listFarmers();
        BigDecimal totalPurchases = purchaseService.listPurchases().stream()
                .map(Purchase::getTotalValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSales = saleService.listSales().stream()
                .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal advances = advanceService.listAdvances().stream()
                .map(Advance::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal repayments = repaymentService.listRepayments().stream()
                .map(Repayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outstanding = farmers.stream()
                .map(Farmer::getCurrentAdvance).reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardKpis.builder()
            .withTotalFarmers(farmers.size())
            .withTotalPurchases(totalPurchases)
            .withTotalSales(totalSales)
            .withTotalAdvances(advances)
            .withTotalRepayments(repayments)
            .withTotalOutstanding(outstanding)
            .build();
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
                BigDecimal netPayout = total.subtract(commission).setScale(1, RoundingMode.HALF_UP);

                return FarmerPayoutSummary.builder()
                    .withFarmerId(farmerId)
                    .withMonth(month)
                    .withTotalPurchases(total)
                    .withCommission(commission)
                    .withNetPayout(netPayout)
                    .build();
            }).collect(Collectors.toList());
    }

    public List<CogsBreakdown> getCogsBreakdown() {
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(Purchase::getFlowerType))
            .entrySet().stream().map(e -> {
                BigDecimal totalCogs = e.getValue().stream()
                    .map(Purchase::getCogs)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return CogsBreakdown.builder()
                    .withFlowerType(e.getKey())
                    .withTotalCogs(totalCogs)
                    .build();
            }).collect(Collectors.toList());
    }

    public List<MarketVariance> getMarketVariance() {
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(Purchase::getFlowerType))
            .entrySet().stream().map(e -> {
                BigDecimal totalVariance = e.getValue().stream()
                    .map(Purchase::getVariance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return MarketVariance.builder()
                    .withFlowerType(e.getKey())
                    .withTotalVariance(totalVariance)
                    .build();
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

        return CashFlowReport.builder()
            .withTotalInflows(inflows)
            .withTotalOutflows(outflows)
            .withNetFlow(inflows.subtract(outflows))
            .build();
    }

    /** Purchases total by month for charts/reports */
    public List<ChartData> getPurchasesByMonth() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        return purchaseService.listPurchases().stream()
            .collect(Collectors.groupingBy(p -> fmt.format(p.getDate())))
            .entrySet().stream()
            .map(e -> ChartData.builder()
                .withLabel(e.getKey())
                .withValue(e.getValue().stream()
                    .map(Purchase::getTotalValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build())
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }

    /** Sales total by flower type for charts/reports */
    public List<ChartData> getSalesByFlower() {
        return saleService.listSales().stream()
            .collect(Collectors.groupingBy(Sale::getFlowerType))
            .entrySet().stream()
            .map(e -> ChartData.builder()
                .withLabel(e.getKey())
                .withValue(e.getValue().stream()
                    .map(Sale::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build())
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }

    /** Advances total over time (by date) for charts/reports */
    public List<ChartData> getAdvancesOverTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return advanceService.listAdvances().stream()
            .collect(Collectors.groupingBy(a -> fmt.format(a.getDate())))
            .entrySet().stream()
            .map(e -> ChartData.builder()
                .withLabel(e.getKey())
                .withValue(e.getValue().stream()
                    .map(Advance::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build())
            .sorted(Comparator.comparing(ChartData::getLabel))
            .collect(Collectors.toList());
    }
}
