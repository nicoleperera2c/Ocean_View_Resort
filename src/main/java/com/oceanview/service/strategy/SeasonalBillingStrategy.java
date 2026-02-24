package com.oceanview.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;

/**
 * Seasonal Billing Strategy - adjusts pricing based on peak/off-peak seasons
 * Peak season (Dec-Mar): 25% surcharge
 * Off-peak season (Apr-Nov): standard rate
 * Demonstrates the Strategy pattern with different pricing logic
 */
public class SeasonalBillingStrategy implements BillingStrategy {

    private static final BigDecimal PEAK_MULTIPLIER = new BigDecimal("1.25");

    @Override
    public BigDecimal calculateTotal(BigDecimal ratePerNight, LocalDate checkIn, LocalDate checkOut) {
        if (ratePerNight == null || checkIn == null || checkOut == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        LocalDate current = checkIn;

        while (current.isBefore(checkOut)) {
            if (isPeakSeason(current)) {
                total = total.add(ratePerNight.multiply(PEAK_MULTIPLIER));
            } else {
                total = total.add(ratePerNight);
            }
            current = current.plusDays(1);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Check if date falls within peak tourist season (Dec-Mar)
     */
    private boolean isPeakSeason(LocalDate date) {
        Month month = date.getMonth();
        return month == Month.DECEMBER || month == Month.JANUARY ||
                month == Month.FEBRUARY || month == Month.MARCH;
    }

    @Override
    public String getStrategyName() {
        return "Seasonal Billing (25% peak surcharge Dec-Mar)";
    }
}
