package com.bookkeeper.service;

import com.bookkeeper.model.Alert;
import com.bookkeeper.model.Farmer;
import com.bookkeeper.model.Sale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    private final FarmerService farmerService;
    private final SaleService saleService;
    private final MessageSource messageSource;
    private final BigDecimal largeAdvanceThreshold = new BigDecimal("1000");

    public AlertService(FarmerService farmerService,
                        SaleService saleService,
                        MessageSource messageSource) {
        this.farmerService = farmerService;
        this.saleService = saleService;
        this.messageSource = messageSource;
    }

    /**
     * Scans system for alerts: credit limit breaches, large advances, overdue payments.
     */
    public List<Alert> getAlerts() {
        List<Alert> alerts = new ArrayList<>();
        LocalDate now = LocalDate.now();
        // Credit limit breach & large advances
        for (Farmer f : farmerService.listFarmers()) {
            BigDecimal advance = f.getCurrentAdvance() != null ? f.getCurrentAdvance() : BigDecimal.ZERO;
            BigDecimal limit = f.getCreditLimit();
            // credit limit breach
            if (limit != null && advance.compareTo(limit) > 0) {
                Alert a = new Alert();
                a.setType("CreditLimitBreach");
                a.setEntityId(f.getId());
                a.setMessage(messageSource.getMessage(
                        "alert.creditLimitBreach",
                        new Object[]{advance, limit},
                        LocaleContextHolder.getLocale()));
                alerts.add(a);
            }
            // large advance
            if (advance.compareTo(largeAdvanceThreshold) > 0) {
                Alert a = new Alert();
                a.setType("LargeAdvance");
                a.setEntityId(f.getId());
                a.setMessage(messageSource.getMessage(
                        "alert.largeAdvance",
                        new Object[]{advance, largeAdvanceThreshold},
                        LocaleContextHolder.getLocale()));
                alerts.add(a);
            }
        }
        // Overdue payments
        for (Sale s : saleService.listSales()) {
            if (s.getDueDate() != null && s.getPaymentStatus() != null
                    && !"PAID".equalsIgnoreCase(s.getPaymentStatus())
                    && s.getDueDate().isBefore(now)) {
                Alert a = new Alert();
                a.setType("OverduePayment");
                a.setEntityId(s.getId());
                a.setMessage(messageSource.getMessage(
                        "alert.overduePayment",
                        new Object[]{s.getId(), s.getDueDate()},
                        LocaleContextHolder.getLocale()));
                alerts.add(a);
            }
        }
        return alerts;
    }
}
