package com.oceanview.service.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Standard Billing Strategy - base nights x rate calculation
 * Default strategy when no special pricing applies
 */
public class StandardBillingStrategy implements BillingStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal ratePerNight, LocalDate checkIn, LocalDate checkOut) {
        if (ratePerNight == null || checkIn == null || checkOut == null) {
            return BigDecimal.ZERO;
        }
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            return BigDecimal.ZERO;
        }
        return ratePerNight.multiply(BigDecimal.valueOf(nights));
    }

    @Override
    public String getStrategyName() {
        return "Standard Billing";
    }
}
