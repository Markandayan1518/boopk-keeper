package com.bookkeeper.service;

import com.bookkeeper.model.Farmer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Basic in-memory Farmer management service with auto-generated IDs.
 */
@Service
public class FarmerService {
    private final Map<String, Farmer> farmers = new ConcurrentHashMap<>();

    /**
     * Adds a new farmer and returns the created instance.
     */
    public Farmer addFarmer(String name,
                            String city,
                            String contact,
                            String address,
                            BigDecimal commissionRate,
                            BigDecimal creditLimit,
                            List<String> flowerTypes,
                            String bankDetails,
                            String remarks) {
        String baseId = StringUtils.replacePattern((name.trim() + "_" + city.trim()).toLowerCase(), "\\s+", "_");
        String id = baseId;
        int suffix = 1;
        while (farmers.containsKey(id)) {
            id = baseId + "_" + suffix++;
        }

        Farmer farmer = Farmer.builder()
            .withId(id)
            .withName(name)
            .withCity(city)
            .withContact(contact)
            .withAddress(address)
            .withCommissionRate(commissionRate)
            .withCreditLimit(creditLimit)
            .withCurrentAdvance(BigDecimal.ZERO)
            .withFlowerTypes(new ArrayList<>(flowerTypes))
            .withBankDetails(bankDetails)
            .withRemarks(remarks)
            .build();

        farmers.put(id, farmer);
        return farmer;
    }

    /** Returns all farmers. */
    public List<Farmer> listFarmers() {
        return new ArrayList<>(farmers.values());
    }

    /** Finds a farmer by ID. */
    public Optional<Farmer> getFarmerById(String id) {
        return Optional.ofNullable(farmers.get(id));
    }

    /** Updates existing farmer details. */
    public boolean updateFarmer(Farmer updated) {
        String id = updated.getId();
        if (!farmers.containsKey(id)) {
            return false;
        }
        farmers.put(id, updated);
        return true;
    }

    /** Removes a farmer by ID. */
    public boolean removeFarmer(String id) {
        return farmers.remove(id) != null;
    }

    /** Records an advance payment to farmer, fails if over credit limit or farmer not found. */
    public boolean recordAdvance(String farmerId, BigDecimal amount) {
        Farmer farmer = farmers.get(farmerId);
        if (farmer == null) return false;
        BigDecimal current = farmer.getCurrentAdvance() != null ? farmer.getCurrentAdvance() : BigDecimal.ZERO;
        BigDecimal newAdvance = current.add(amount);
        BigDecimal limit = farmer.getCreditLimit();

        if (limit != null) {
            if (newAdvance.compareTo(limit) <= 0) {
                farmers.put(farmerId, Farmer.from(farmer).withCurrentAdvance(newAdvance).build());
                return true;
            } else if (amount.compareTo(limit) > 0) {
                // single advance amount exceeds limit, record and breach
                farmers.put(farmerId, Farmer.from(farmer).withCurrentAdvance(newAdvance).build());
                return false;
            } else {
                // aggregate would exceed, do not record
                return false;
            }
        } else {
            // no limit defined
            farmers.put(farmerId, Farmer.from(farmer).withCurrentAdvance(newAdvance).build());
            return true;
        }
    }

    /** Records a repayment, fails if it would make advance negative or farmer not found. */
    public boolean recordRepayment(String farmerId, BigDecimal amount) {
        Farmer farmer = farmers.get(farmerId);
        if (farmer == null) return false;
        BigDecimal newAdvance = farmer.getCurrentAdvance().subtract(amount);
        if (newAdvance.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        farmers.put(farmerId, Farmer.from(farmer).withCurrentAdvance(newAdvance).build());
        return true;
    }
}
