package com.bookkeeper.service;

import com.bookkeeper.model.Sale;
import com.bookkeeper.model.SaleRequest;
import org.springframework.stereotype.Service;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Service
public class SaleService {
    private final Map<String, Sale> sales = new ConcurrentHashMap<>();
    private final JournalService journalService;

    public SaleService(JournalService journalService) {
        this.journalService = journalService;
    }

    /**
     * Records a new sale and calculates total amount.
     */
    public Sale addSale(SaleRequest req) {
        String baseId = StringUtils.remove(req.getDate().toString(), '-');
        String id = baseId;
        int suffix = 1;
        while (sales.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }

        BigDecimal totalAmount = req.getQuantity().multiply(req.getRate());
        Sale sale = Sale.builder()
            .withId(id)
            .withDate(req.getDate())
            .withInvoiceNumber(req.getInvoiceNumber())
            .withBuyer(req.getBuyer())
            .withFlowerType(req.getFlowerType())
            .withQuantity(req.getQuantity())
            .withRate(req.getRate())
            .withTotalAmount(totalAmount)
            .withPaymentStatus(req.getPaymentStatus())
            .withDueDate(req.getDueDate())
            .withPaymentDate(req.getPaymentDate())
            .withNotes(req.getNotes())
            .build();

        sales.put(id, sale);

        // Create journal entry
        JournalEntry je = JournalEntry.builder()
            .withDate(sale.getDate())
            .withDescription("Sale " + sale.getId())
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("AccountsReceivable")
                    .withDebit(totalAmount)
                    .withReferenceType("Sale")
                    .withReferenceId(id)
                    .build(),
                JournalEntryLine.builder()
                    .withAccount("Revenue")
                    .withCredit(totalAmount)
                    .withReferenceType("Sale")
                    .withReferenceId(id)
                    .build()
            ))
            .build();
        journalService.addEntry(je);
        return sale;
    }

    public List<Sale> listSales() {
        return new ArrayList<>(sales.values());
    }

    public Optional<Sale> getSaleById(String id) {
        return Optional.ofNullable(sales.get(id));
    }

    public boolean updateSale(String id, SaleRequest req) {
        if (!sales.containsKey(id)) return false;
        BigDecimal totalAmount = req.getQuantity().multiply(req.getRate());
        Sale sale = Sale.builder()
            .withId(id)
            .withDate(req.getDate())
            .withInvoiceNumber(req.getInvoiceNumber())
            .withBuyer(req.getBuyer())
            .withFlowerType(req.getFlowerType())
            .withQuantity(req.getQuantity())
            .withRate(req.getRate())
            .withTotalAmount(totalAmount)
            .withPaymentStatus(req.getPaymentStatus())
            .withDueDate(req.getDueDate())
            .withPaymentDate(req.getPaymentDate())
            .withNotes(req.getNotes())
            .build();
        sales.put(id, sale);
        return true;
    }

    public boolean removeSale(String id) {
        return sales.remove(id) != null;
    }
}
