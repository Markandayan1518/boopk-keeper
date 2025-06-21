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
        String baseId = req.getInvoiceNumber();
        String id = baseId;
        int suffix = 1;
        while (sales.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        Sale sale = new Sale();
        sale.setId(id);
        sale.setDate(req.getDate());
        sale.setInvoiceNumber(req.getInvoiceNumber());
        sale.setBuyer(req.getBuyer());
        sale.setFlowerType(req.getFlowerType());
        sale.setQuantity(req.getQuantity());
        sale.setRate(req.getRate());
        BigDecimal total = req.getRate().multiply(req.getQuantity());
        sale.setTotalAmount(total);
        sale.setPaymentStatus(req.getPaymentStatus());
        sale.setDueDate(req.getDueDate());
        sale.setPaymentDate(req.getPaymentDate());
        sale.setNotes(req.getNotes());
        sales.put(id, sale);
        // Create journal entry: Dr Cash, Cr Sales
        JournalEntry je = new JournalEntry();
        je.setDate(sale.getDate());
        je.setDescription("Sale " + sale.getId());
        List<JournalEntryLine> lines = new ArrayList<>();
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("Cash"); dr.setDebit(total); dr.setCredit(null);
        dr.setReferenceType("Sale"); dr.setReferenceId(sale.getId());
        lines.add(dr);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("Sales"); cr.setDebit(null); cr.setCredit(total);
        cr.setReferenceType("Sale"); cr.setReferenceId(sale.getId());
        lines.add(cr);
        je.setLines(lines);
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
        if (!sales.containsKey(id)) {
            return false;
        }
        Sale sale = sales.get(id);
        sale.setDate(req.getDate());
        sale.setInvoiceNumber(req.getInvoiceNumber());
        sale.setBuyer(req.getBuyer());
        sale.setFlowerType(req.getFlowerType());
        sale.setQuantity(req.getQuantity());
        sale.setRate(req.getRate());
        sale.setTotalAmount(req.getRate().multiply(req.getQuantity()));
        sale.setPaymentStatus(req.getPaymentStatus());
        sale.setDueDate(req.getDueDate());
        sale.setPaymentDate(req.getPaymentDate());
        sale.setNotes(req.getNotes());
        sales.put(id, sale);
        return true;
    }

    public boolean removeSale(String id) {
        return sales.remove(id) != null;
    }
}
