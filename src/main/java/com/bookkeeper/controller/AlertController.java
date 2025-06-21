package com.bookkeeper.controller;

import com.bookkeeper.model.Alert;
import com.bookkeeper.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing system alerts.
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * Returns current alerts such as credit limit breaches or overdue payments.
     */
    @GetMapping
    public List<Alert> getAlerts() {
        return alertService.getAlerts();
    }
}
