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
import org.apache.commons.lang3.StringUtils;

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
        // check if repayment is valid
        boolean ok = farmerService.recordRepayment(rep.getFarmerId(), rep.getAmount());
        if (!ok) return null;

        // generate unique ID: repYYYMMDD_farmerId[_n]
        String dateStr = StringUtils.remove(rep.getDate().toString(), '-');
        String baseId = "rep" + dateStr + "_" + rep.getFarmerId();
        String id = baseId;
        int suffix = 1;
        while (repayments.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }

        Repayment saved = Repayment.builder()
            .withId(id)
            .withDate(rep.getDate())
            .withFarmerId(rep.getFarmerId())
            .withAmount(rep.getAmount())
            .withPaymentMode(rep.getPaymentMode())
            .withReceiptNumber(rep.getReceiptNumber())
            .withRemarks(rep.getRemarks())
            .build();

        repayments.put(id, saved);

        // Create journal entry
        JournalEntry je = JournalEntry.builder()
            .withDate(rep.getDate())
            .withDescription("Repayment " + saved.getId())
            .withLines(Arrays.asList(
                JournalEntryLine.builder()
                    .withAccount("Cash")
                    .withDebit(rep.getAmount())
                    .withReferenceType("Repayment")
                    .withReferenceId(id)
                    .build(),
                JournalEntryLine.builder()
                    .withAccount("AdvancesToFarmers")
                    .withCredit(rep.getAmount())
                    .withReferenceType("Repayment")
                    .withReferenceId(id)
                    .build()
            ))
            .build();
        journalService.addEntry(je);
        return saved;
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
