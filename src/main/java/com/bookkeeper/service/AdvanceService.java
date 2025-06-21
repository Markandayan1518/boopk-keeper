package com.bookkeeper.service;

import com.bookkeeper.model.Advance;
import com.bookkeeper.model.Farmer;
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
        // check limit before updating farmer's advance
        Farmer f = farmerService.getFarmerById(adv.getFarmerId()).orElse(null);
        if (f == null) return null;
        BigDecimal current = f.getCurrentAdvance() != null ? f.getCurrentAdvance() : BigDecimal.ZERO;
        BigDecimal limit = f.getCreditLimit();
        if (limit != null && current.add(adv.getAmount()).compareTo(limit) > 0) {
            return null;
        }
        // safe to record advance
        farmerService.recordAdvance(adv.getFarmerId(), adv.getAmount());
        // generate unique ID: advYYYYMMDD_farmerId[_n]
        String dateStr = StringUtils.remove(adv.getDate().toString(), '-');
        String baseId = "adv" + dateStr + "_" + adv.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (advances.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }
        // Create new advance with generated ID
        adv = Advance.from(adv)
            .withId(id)
            .build();
        advances.put(id, adv);
        // Create journal entry: Dr AdvancesToFarmers, Cr Cash
        JournalEntry je = JournalEntry.builder()
            .withDate(adv.getDate())
            .withDescription("Advance " + adv.getId())
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("AdvancesToFarmers")
                    .withDebit(adv.getAmount())
                    .withReferenceType("Advance")
                    .withReferenceId(id)
                    .build(),
                JournalEntryLine.builder()
                    .withAccount("Cash")
                    .withCredit(adv.getAmount())
                    .withReferenceType("Advance")
                    .withReferenceId(id)
                    .build()
            ))
            .build();
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
