package com.bookkeeper.service;

import com.bookkeeper.model.Repayment;
import org.springframework.stereotype.Service;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentService {
    private final Map<String, Repayment> repayments = new ConcurrentHashMap<>();
    private final FarmerService farmerService;
    private final JournalService journalService;

    public RepaymentService(FarmerService farmerService, JournalService journalService) {
        this.farmerService = farmerService;
        this.journalService = journalService;
    }

    /**
     * Records a repayment and updates farmer's outstanding advance.
     * Returns the saved Repayment or null if not allowed or farmer not found.
     */
    public Repayment addRepayment(Repayment rep) {
        boolean ok = farmerService.recordRepayment(rep.getFarmerId(), rep.getAmount());
        if (!ok) return null;
        String dateStr = rep.getDate().toString().replaceAll("-", "");
        String baseId = "rep" + dateStr + "_" + rep.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (repayments.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        rep.setId(id);
        repayments.put(id, rep);
        // Create journal entry: Dr Cash, Cr AdvancesToFarmers
        JournalEntry je = new JournalEntry();
        je.setDate(rep.getDate());
        je.setDescription("Repayment " + rep.getId());
        List<JournalEntryLine> lines = new ArrayList<>();
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("Cash"); dr.setDebit(rep.getAmount()); dr.setCredit(null);
        dr.setReferenceType("Repayment"); dr.setReferenceId(rep.getId());
        lines.add(dr);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("AdvancesToFarmers"); cr.setDebit(null); cr.setCredit(rep.getAmount());
        cr.setReferenceType("Repayment"); cr.setReferenceId(rep.getId());
        lines.add(cr);
        je.setLines(lines);
        journalService.addEntry(je);
        return rep;
    }

    public List<Repayment> listRepayments() {
        return new ArrayList<>(repayments.values());
    }

    public Optional<Repayment> getRepaymentById(String id) {
        return Optional.ofNullable(repayments.get(id));
    }

    public boolean removeRepayment(String id) {
        Repayment rep = repayments.remove(id);
        if (rep == null) return false;
        // rollback repayment
        farmerService.recordAdvance(rep.getFarmerId(), rep.getAmount());
        return true;
    }
}
