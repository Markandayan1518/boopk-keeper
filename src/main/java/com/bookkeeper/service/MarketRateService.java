package com.bookkeeper.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory storage of daily market rates per flower type.
 */
@Service
public class MarketRateService {
    private final Map<LocalDate, Map<String, BigDecimal>> rates = new ConcurrentHashMap<>();

    /**
     * Sets the market rate for a given date and flower type.
     */
    public void setRate(LocalDate date, String flowerType, BigDecimal rate) {
        rates.computeIfAbsent(date, d -> new ConcurrentHashMap<>()).put(flowerType, rate);
    }

    /**
     * Retrieves the market rate for a given date and flower type.
     */
    public Optional<BigDecimal> getRate(LocalDate date, String flowerType) {
        Map<String, BigDecimal> dayRates = rates.get(date);
        if (dayRates == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(dayRates.get(flowerType));
    }

    /** Returns all flower rates for a given date (wide format). */
    public Map<String, BigDecimal> getRates(LocalDate date) {
        Map<String, BigDecimal> dayRates = rates.get(date);
        return dayRates == null ? Collections.emptyMap() : new HashMap<>(dayRates);
    }

    /** Returns all recorded rates by date (wide format). */
    public Map<LocalDate, Map<String, BigDecimal>> getAllRates() {
        Map<LocalDate, Map<String, BigDecimal>> copy = new HashMap<>();
        rates.forEach((date, dayRates) -> copy.put(date, new HashMap<>(dayRates)));
        return copy;
    }
}
