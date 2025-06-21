package com.bookkeeper.service;

import com.bookkeeper.model.Purchase;
import com.bookkeeper.model.PurchaseRequest;
import org.springframework.stereotype.Service;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Service
public class PurchaseService {
    private final Map<String, Purchase> purchases = new ConcurrentHashMap<>();
    private final MarketRateService marketRateService;
    private final JournalService journalService;

    public PurchaseService(MarketRateService marketRateService, JournalService journalService) {
        this.marketRateService = marketRateService;
        this.journalService = journalService;
    }

    /** Adds a new purchase, calculates derived fields. */
    public Purchase addPurchase(PurchaseRequest req) {
            String baseId = StringUtils.remove(req.getDate().toString(), '-') + "_" + req.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (purchases.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        Purchase p = Purchase.builder()
            .withId(id)
            .withDate(req.getDate())
            .withFarmerId(req.getFarmerId())
            .withFlowerType(req.getFlowerType())
            .withQuality(req.getQuality())
            .withQuantity(req.getQuantity())
            .withRatePaid(req.getRatePaid())
            .withCogs(req.getCogs())
            .withPaymentMode(req.getPaymentMode())
            .withReceiptNumber(req.getReceiptNumber())
            .withNotes(req.getNotes())
            .build();

        // calculate total value
        BigDecimal totalValue = req.getQuantity().multiply(req.getRatePaid()).add(req.getCogs());
        p = Purchase.from(p)
            .withTotalValue(totalValue)
            .build();

        // get market rate if available
        Optional<BigDecimal> rateOpt = marketRateService.getRate(req.getDate(), req.getFlowerType());
        BigDecimal rate = rateOpt.orElse(BigDecimal.ZERO);
        p = Purchase.from(p)
            .withMarketRate(rate)
            .withMarketValue(rate.multiply(req.getQuantity()))
            .withVariance(totalValue.subtract(rate.multiply(req.getQuantity())))
            .build();

        purchases.put(id, p);

        // Create journal entry
        JournalEntry je = JournalEntry.builder()
            .withDate(p.getDate())
            .withDescription("Purchase " + p.getId())
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("InventoryFlowers")
                    .withDebit(p.getTotalValue())
                    .withReferenceType("Purchase")
                    .withReferenceId(id)
                    .build(),
                JournalEntryLine.builder()
                    .withAccount("Cash")
                    .withCredit(p.getTotalValue())
                    .withReferenceType("Purchase")
                    .withReferenceId(id)
                    .build()
            ))
            .build();
        journalService.addEntry(je);
        return p;
    }

    /** List all purchases */
    public List<Purchase> listPurchases() {
        return new ArrayList<>(purchases.values());
    }

    /** Find by ID */
    public Optional<Purchase> getPurchaseById(String id) {
        return Optional.ofNullable(purchases.get(id));
    }

    /** Update existing purchase */
    public boolean updatePurchase(String id, PurchaseRequest req) {
        if (!purchases.containsKey(id)) return false;
        Purchase p = Purchase.builder()
            .withId(id)
            .withDate(req.getDate())
            .withFarmerId(req.getFarmerId())
            .withFlowerType(req.getFlowerType())
            .withQuality(req.getQuality())
            .withQuantity(req.getQuantity())
            .withRatePaid(req.getRatePaid())
            .withCogs(req.getCogs())
            .withPaymentMode(req.getPaymentMode())
            .withReceiptNumber(req.getReceiptNumber())
            .withNotes(req.getNotes())
            .build();

        // recalculate derived values
        BigDecimal totalValue = req.getQuantity().multiply(req.getRatePaid()).add(req.getCogs());
        p = Purchase.from(p)
            .withTotalValue(totalValue)
            .build();

        Optional<BigDecimal> rateOpt = marketRateService.getRate(req.getDate(), req.getFlowerType());
        BigDecimal rate = rateOpt.orElse(BigDecimal.ZERO);
        p = Purchase.from(p)
            .withMarketRate(rate)
            .withMarketValue(rate.multiply(req.getQuantity()))
            .withVariance(totalValue.subtract(rate.multiply(req.getQuantity())))
            .build();

        purchases.put(id, p);
        return true;
    }

    /** Remove purchase */
    public boolean removePurchase(String id) {
        return purchases.remove(id) != null;
    }
}
