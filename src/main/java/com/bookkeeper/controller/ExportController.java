package com.bookkeeper.controller;

import com.bookkeeper.model.ChartData;
import com.bookkeeper.model.FarmerPayoutSummary;
import com.bookkeeper.model.CogsBreakdown;
import com.bookkeeper.service.DashboardService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    private final DashboardService dashboardService;

    public ExportController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/purchases-by-month", produces = "text/csv")
    public void exportPurchasesByMonth(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PurchasesByMonth.csv");
        List<ChartData> data = dashboardService.getPurchasesByMonth();
        try (CSVPrinter printer = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("Month", "TotalPurchases"))) {
            for (ChartData d : data) {
                printer.printRecord(d.getLabel(), d.getValue());
            }
        }
    }

    @GetMapping(value = "/sales-by-flower", produces = "text/csv")
    public void exportSalesByFlower(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SalesByFlower.csv");
        List<ChartData> data = dashboardService.getSalesByFlower();
        try (CSVPrinter printer = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("FlowerType", "TotalSales"))) {
            for (ChartData d : data) {
                printer.printRecord(d.getLabel(), d.getValue());
            }
        }
    }

    @GetMapping(value = "/advances-over-time", produces = "text/csv")
    public void exportAdvancesOverTime(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AdvancesOverTime.csv");
        List<ChartData> data = dashboardService.getAdvancesOverTime();
        try (CSVPrinter printer = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("Date", "TotalAdvances"))) {
            for (ChartData d : data) {
                printer.printRecord(d.getLabel(), d.getValue());
            }
        }
    }

    @GetMapping(value = "/payout-summaries", produces = "text/csv")
    public void exportPayoutSummaries(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PayoutSummaries.csv");
        List<FarmerPayoutSummary> list = dashboardService.getFarmerPayoutSummaries();
        try (CSVPrinter printer = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("FarmerId", "Month", "TotalPurchases", "Commission", "NetPayout"))) {
            for (FarmerPayoutSummary s : list) {
                printer.printRecord(
                    s.getFarmerId(), s.getMonth(), s.getTotalPurchases(), s.getCommission(), s.getNetPayout()
                );
            }
        }
    }

    @GetMapping(value = "/cogs-breakdown", produces = "text/csv")
    public void exportCogsBreakdown(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CogsBreakdown.csv");
        List<CogsBreakdown> list = dashboardService.getCogsBreakdown();
        try (CSVPrinter printer = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("FlowerType", "TotalCogs"))) {
            for (CogsBreakdown c : list) {
                printer.printRecord(c.getFlowerType(), c.getTotalCogs());
            }
        }
    }
}
