package com.oceanview.service.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Strategy Pattern - Billing Strategy Interface
 * Allows different billing algorithms to be used interchangeably
 * Demonstrates the Strategy design pattern for the assignment
 */
public interface BillingStrategy {

    /**
     * Calculate the total bill for a stay
     * 
     * @param ratePerNight the base room rate per night
     * @param checkIn      check-in date
     * @param checkOut     check-out date
     * @return calculated total amount
     */
    BigDecimal calculateTotal(BigDecimal ratePerNight, LocalDate checkIn, LocalDate checkOut);

    /**
     * Get the name of this billing strategy
     * 
     * @return strategy description
     */
    String getStrategyName();
}
