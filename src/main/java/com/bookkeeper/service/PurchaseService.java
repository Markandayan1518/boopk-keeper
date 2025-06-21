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
        String baseId = "p" + StringUtils.remove(req.getDate().toString(), '-') + "_" + req.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (purchases.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        Purchase p = new Purchase();
        p.setId(id);
        p.setDate(req.getDate());
        p.setFarmerId(req.getFarmerId());
        p.setFlowerType(req.getFlowerType());
        p.setQuality(req.getQuality());
        p.setQuantity(req.getQuantity());
        p.setRatePaid(req.getRatePaid());
        p.setCogs(req.getCogs());
        // totalValue = (ratePaid * quantity) + cogs
        BigDecimal total = req.getRatePaid().multiply(req.getQuantity()).add(req.getCogs());
        p.setTotalValue(total);
        // marketRate lookup
        BigDecimal mRate = marketRateService.getRate(req.getDate(), req.getFlowerType()).orElse(BigDecimal.ZERO);
        p.setMarketRate(mRate);
        // marketValue = mRate * quantity
        BigDecimal mValue = mRate.multiply(req.getQuantity());
        p.setMarketValue(mValue);
        // variance = total - marketValue
        p.setVariance(total.subtract(mValue));
        p.setPaymentMode(req.getPaymentMode());
        p.setReceiptNumber(req.getReceiptNumber());
        p.setNotes(req.getNotes());
        purchases.put(id, p);
        // Create corresponding journal entry: Dr Purchases, Cr Cash
        JournalEntry je = new JournalEntry();
        je.setDate(p.getDate());
        je.setDescription("Purchase " + p.getId());
        List<JournalEntryLine> lines = new ArrayList<>();
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("Purchases"); dr.setDebit(p.getTotalValue()); dr.setCredit(BigDecimal.ZERO);
        dr.setReferenceType("Purchase"); dr.setReferenceId(p.getId());
        lines.add(dr);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("Cash"); cr.setDebit(BigDecimal.ZERO); cr.setCredit(p.getTotalValue());
        cr.setReferenceType("Purchase"); cr.setReferenceId(p.getId());
        lines.add(cr);
        je.setLines(lines);
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
        Purchase p = purchases.get(id);
        p.setDate(req.getDate());
        p.setFarmerId(req.getFarmerId());
        p.setFlowerType(req.getFlowerType());
        p.setQuality(req.getQuality());
        p.setQuantity(req.getQuantity());
        p.setRatePaid(req.getRatePaid());
        p.setCogs(req.getCogs());
        // Recalculate totals and market values without creating new journal entry
        BigDecimal total = req.getRatePaid().multiply(req.getQuantity()).add(req.getCogs());
        p.setTotalValue(total);
        BigDecimal mRate = marketRateService.getRate(req.getDate(), req.getFlowerType()).orElse(BigDecimal.ZERO);
        p.setMarketRate(mRate);
        BigDecimal mValue = mRate.multiply(req.getQuantity());
        p.setMarketValue(mValue);
        p.setVariance(total.subtract(mValue));
        p.setPaymentMode(req.getPaymentMode());
        p.setReceiptNumber(req.getReceiptNumber());
        p.setNotes(req.getNotes());
        purchases.put(id, p);
        return true;
    }

    /** Remove purchase */
    public boolean removePurchase(String id) {
        return purchases.remove(id) != null;
    }
}
