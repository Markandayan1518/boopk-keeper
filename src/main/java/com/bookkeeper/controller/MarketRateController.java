package com.bookkeeper.controller;

import com.bookkeeper.service.MarketRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/market-rates")
public class MarketRateController {
    private final MarketRateService marketRateService;

    public MarketRateController(MarketRateService marketRateService) {
        this.marketRateService = marketRateService;
    }

    @PostMapping("/{date}/{flowerType}")
    public ResponseEntity<Void> setRate(@PathVariable String date,
                                        @PathVariable String flowerType,
                                        @RequestParam BigDecimal rate) {
        LocalDate d = LocalDate.parse(date);
        marketRateService.setRate(d, flowerType, rate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{date}/{flowerType}")
    public ResponseEntity<BigDecimal> getRate(@PathVariable String date,
                                              @PathVariable String flowerType) {
        LocalDate d = LocalDate.parse(date);
        return marketRateService.getRate(d, flowerType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
