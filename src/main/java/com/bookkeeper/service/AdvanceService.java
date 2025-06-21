package com.bookkeeper.service;

import com.bookkeeper.model.Advance;
import org.springframework.stereotype.Service;
import com.bookkeeper.service.JournalService;
import com.bookkeeper.model.JournalEntry;
import com.bookkeeper.model.JournalEntryLine;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Service
public class AdvanceService {
    private final Map<String, Advance> advances = new ConcurrentHashMap<>();
    private final FarmerService farmerService;
    private final JournalService journalService;

    public AdvanceService(FarmerService farmerService, JournalService journalService) {
        this.farmerService = farmerService;
        this.journalService = journalService;
    }

    /**
     * Records an advance payment and updates farmer's outstanding advance.
     * Returns the saved Advance or null if limit exceeded or farmer not found.
     */
    public Advance addAdvance(Advance adv) {
        // check and update farmer advance
        boolean ok = farmerService.recordAdvance(adv.getFarmerId(), adv.getAmount());
        if (!ok) return null;
        // generate unique ID: advYYYYMMDD_farmerId[_n]
        String dateStr = StringUtils.remove(adv.getDate().toString(), '-');
        String baseId = "adv" + dateStr + "_" + adv.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (advances.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        adv.setId(id);
        advances.put(id, adv);
        // Create journal entry: Dr AdvancesToFarmers, Cr Cash
        JournalEntry je = new JournalEntry();
        je.setDate(adv.getDate());
        je.setDescription("Advance " + adv.getId());
        List<JournalEntryLine> lines = new ArrayList<>();
        JournalEntryLine dr = new JournalEntryLine();
        dr.setAccount("AdvancesToFarmers"); dr.setDebit(adv.getAmount()); dr.setCredit(null);
        dr.setReferenceType("Advance"); dr.setReferenceId(id);
        lines.add(dr);
        JournalEntryLine cr = new JournalEntryLine();
        cr.setAccount("Cash"); cr.setDebit(null); cr.setCredit(adv.getAmount());
        cr.setReferenceType("Advance"); cr.setReferenceId(id);
        lines.add(cr);
        je.setLines(lines);
        journalService.addEntry(je);
        return adv;
    }

    public List<Advance> listAdvances() {
        return new ArrayList<>(advances.values());
    }

    public Optional<Advance> getAdvanceById(String id) {
        return Optional.ofNullable(advances.get(id));
    }

    public boolean removeAdvance(String id) {
        Advance adv = advances.remove(id);
        if (adv == null) return false;
        // roll back farmer advance
        farmerService.recordRepayment(adv.getFarmerId(), adv.getAmount());
        return true;
    }
}
