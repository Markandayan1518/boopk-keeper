package com.bookkeeper.controller;

import com.bookkeeper.model.*;
import com.bookkeeper.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/kpis")
    public DashboardKpis getKpis() {
        return dashboardService.getKpis();
    }

    @GetMapping("/payout-summaries")
    public List<FarmerPayoutSummary> getPayoutSummaries() {
        return dashboardService.getFarmerPayoutSummaries();
    }

    @GetMapping("/cogs-breakdown")
    public List<CogsBreakdown> getCogsBreakdown() {
        return dashboardService.getCogsBreakdown();
    }

    @GetMapping("/market-variance")
    public List<MarketVariance> getMarketVariance() {
        return dashboardService.getMarketVariance();
    }

    @GetMapping("/cash-flow")
    public CashFlowReport getCashFlow() {
        return dashboardService.getCashFlow();
    }
}
