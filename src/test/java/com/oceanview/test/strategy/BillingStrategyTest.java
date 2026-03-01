package com.oceanview.test.strategy;

import com.oceanview.service.strategy.BillingStrategy;
import com.oceanview.service.strategy.StandardBillingStrategy;
import com.oceanview.service.strategy.SeasonalBillingStrategy;
import com.oceanview.service.strategy.LongStayBillingStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Comprehensive JUnit tests for the Strategy design pattern implementation.
 * Tests all three billing strategies with normal, boundary, and edge cases.
 */
public class BillingStrategyTest {

    private final BigDecimal RATE = new BigDecimal("100.00");

    // ==================== Standard Billing ====================

    @Test
    public void testStandardBilling3Nights() {
        BillingStrategy strategy = new StandardBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 4);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        assertEquals(new BigDecimal("300.00"), total);
    }

    @Test
    public void testStandardBillingOneNight() {
        BillingStrategy strategy = new StandardBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 2);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        assertEquals(new BigDecimal("100.00"), total);
    }

    @Test
    public void testStandardBillingNullRate() {
        BillingStrategy strategy = new StandardBillingStrategy();
        BigDecimal total = strategy.calculateTotal(null, LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testStandardBillingSameDayCheckout() {
        BillingStrategy strategy = new StandardBillingStrategy();
        LocalDate same = LocalDate.of(2026, 6, 1);
        BigDecimal total = strategy.calculateTotal(RATE, same, same);
        assertEquals("Same day = 0 nights = Rs 0", BigDecimal.ZERO, total);
    }

    @Test
    public void testStandardBillingHighRate() {
        BillingStrategy strategy = new StandardBillingStrategy();
        BigDecimal highRate = new BigDecimal("25000.00");
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 3);

        BigDecimal total = strategy.calculateTotal(highRate, checkIn, checkOut);
        assertEquals(new BigDecimal("50000.00"), total);
    }

    @Test
    public void testStandardBillingName() {
        assertEquals("Standard Billing", new StandardBillingStrategy().getStrategyName());
    }

    // ==================== Seasonal Billing ====================

    @Test
    public void testSeasonalBillingOffPeak() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        // June is off-peak — no surcharge
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 4);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        assertEquals(new BigDecimal("300.00"), total);
    }

    @Test
    public void testSeasonalBillingPeakJanuary() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        // January is peak season (25% surcharge)
        LocalDate checkIn = LocalDate.of(2026, 1, 10);
        LocalDate checkOut = LocalDate.of(2026, 1, 12);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 2 nights x Rs 100 x 1.25 = Rs 250
        assertEquals(new BigDecimal("250.00"), total);
    }

    @Test
    public void testSeasonalBillingPeakDecember() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 12, 20);
        LocalDate checkOut = LocalDate.of(2026, 12, 23);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 3 nights x Rs 100 x 1.25 = Rs 375
        assertEquals(new BigDecimal("375.00"), total);
    }

    @Test
    public void testSeasonalBillingPeakFebruary() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 2, 1);
        LocalDate checkOut = LocalDate.of(2026, 2, 3);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 2 nights x Rs 100 x 1.25 = Rs 250
        assertEquals(new BigDecimal("250.00"), total);
    }

    @Test
    public void testSeasonalBillingPeakMarch() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 2 nights x Rs 100 x 1.25 = Rs 250
        assertEquals(new BigDecimal("250.00"), total);
    }

    @Test
    public void testSeasonalBillingNullRate() {
        BillingStrategy strategy = new SeasonalBillingStrategy();
        BigDecimal total = strategy.calculateTotal(null, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3));
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testSeasonalBillingName() {
        assertEquals("Seasonal Billing (25% peak surcharge Dec-Mar)", new SeasonalBillingStrategy().getStrategyName());
    }

    // ==================== Long Stay Billing ====================

    @Test
    public void testLongStayNoDiscount() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 5); // 4 nights — no discount

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        assertEquals(new BigDecimal("400.00"), total);
    }

    @Test
    public void testLongStay6NightsNoDiscount() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 7); // 6 nights — still no discount

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        assertEquals(new BigDecimal("600.00"), total);
    }

    @Test
    public void testLongStay7NightDiscount() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 8); // 7 nights = 10% discount

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 7 x Rs 100 = Rs 700, -10% = Rs 630
        assertEquals(new BigDecimal("630.00"), total);
    }

    @Test
    public void testLongStay13NightsStill10Percent() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 14); // 13 nights = still 10%

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 13 x Rs 100 = Rs 1300, -10% = Rs 1170
        assertEquals(new BigDecimal("1170.00"), total);
    }

    @Test
    public void testLongStay14NightDiscount() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 15); // 14 nights = 20% discount

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 14 x Rs 100 = Rs 1400, -20% = Rs 1120
        assertEquals(new BigDecimal("1120.00"), total);
    }

    @Test
    public void testLongStay30NightDiscount() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 7, 1); // 30 nights = 30% discount

        BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
        // 30 x Rs 100 = Rs 3000, -30% = Rs 2100
        assertEquals(new BigDecimal("2100.00"), total);
    }

    @Test
    public void testLongStayNullRate() {
        BillingStrategy strategy = new LongStayBillingStrategy();
        BigDecimal total = strategy.calculateTotal(null, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10));
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testLongStayBillingName() {
        assertEquals("Long Stay Discount (10% at 7+, 20% at 14+, 30% at 30+ nights)",
                new LongStayBillingStrategy().getStrategyName());
    }

    // ==================== Strategy Pattern: Polymorphism ====================

    @Test
    public void testStrategyPolymorphism() {
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 4);

        BillingStrategy[] strategies = {
                new StandardBillingStrategy(),
                new SeasonalBillingStrategy(),
                new LongStayBillingStrategy()
        };

        for (BillingStrategy strategy : strategies) {
            BigDecimal total = strategy.calculateTotal(RATE, checkIn, checkOut);
            assertNotNull("Strategy " + strategy.getStrategyName() + " should return non-null", total);
            assertTrue("Strategy " + strategy.getStrategyName() + " should return positive",
                    total.compareTo(BigDecimal.ZERO) > 0);
        }
    }

    @Test
    public void testEachStrategyHasUniqueName() {
        String standard = new StandardBillingStrategy().getStrategyName();
        String seasonal = new SeasonalBillingStrategy().getStrategyName();
        String longStay = new LongStayBillingStrategy().getStrategyName();

        assertFalse("Strategy names must be unique",
                standard.equals(seasonal) || seasonal.equals(longStay) || standard.equals(longStay));
    }

    @Test
    public void testOffPeakStandardAndSeasonalMatch() {
        // In off-peak season with short stay, Standard and Seasonal should give same
        // result
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 4);

        BigDecimal standard = new StandardBillingStrategy().calculateTotal(RATE, checkIn, checkOut);
        BigDecimal seasonal = new SeasonalBillingStrategy().calculateTotal(RATE, checkIn, checkOut);

        assertEquals("Off-peak: Standard and Seasonal should produce same total",
                standard, seasonal);
    }

    @Test
    public void testPeakSeasonCostsMoreThanStandard() {
        LocalDate checkIn = LocalDate.of(2026, 1, 10);
        LocalDate checkOut = LocalDate.of(2026, 1, 13);

        BigDecimal standard = new StandardBillingStrategy().calculateTotal(RATE, checkIn, checkOut);
        BigDecimal seasonal = new SeasonalBillingStrategy().calculateTotal(RATE, checkIn, checkOut);

        assertTrue("Peak season should cost more than standard billing",
                seasonal.compareTo(standard) > 0);
    }

    @Test
    public void testLongStayCostsLessThanStandard() {
        LocalDate checkIn = LocalDate.of(2026, 6, 1);
        LocalDate checkOut = LocalDate.of(2026, 6, 8); // 7 nights

        BigDecimal standard = new StandardBillingStrategy().calculateTotal(RATE, checkIn, checkOut);
        BigDecimal longStay = new LongStayBillingStrategy().calculateTotal(RATE, checkIn, checkOut);

        assertTrue("Long stay discount should cost less than standard",
                longStay.compareTo(standard) < 0);
    }
}
