package com.oceanview.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Long Stay Billing Strategy - applies discount for extended stays
 * 7+ nights: 10% discount
 * 14+ nights: 20% discount
 * 30+ nights: 30% discount
 * Demonstrates the Strategy pattern with tiered discount logic
 */
public class LongStayBillingStrategy implements BillingStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal ratePerNight, LocalDate checkIn, LocalDate checkOut) {
        if (ratePerNight == null || checkIn == null || checkOut == null) {
            return BigDecimal.ZERO;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal baseTotal = ratePerNight.multiply(BigDecimal.valueOf(nights));
        BigDecimal discount = getDiscountRate(nights);
        BigDecimal discountAmount = baseTotal.multiply(discount);

        return baseTotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Determine discount rate based on length of stay
     */
    private BigDecimal getDiscountRate(long nights) {
        if (nights >= 30) {
            return new BigDecimal("0.30"); // 30% discount
        } else if (nights >= 14) {
            return new BigDecimal("0.20"); // 20% discount
        } else if (nights >= 7) {
            return new BigDecimal("0.10"); // 10% discount
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getStrategyName() {
        return "Long Stay Discount (10% at 7+, 20% at 14+, 30% at 30+ nights)";
    }
}
