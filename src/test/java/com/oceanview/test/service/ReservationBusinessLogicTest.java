package com.oceanview.test.service;

import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.model.Guest;
import com.oceanview.service.strategy.BillingStrategy;
import com.oceanview.service.strategy.StandardBillingStrategy;
import com.oceanview.service.strategy.SeasonalBillingStrategy;
import com.oceanview.service.strategy.LongStayBillingStrategy;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * JUnit tests for reservation business logic methods.
 * Tests the integration between Reservation model business methods
 * and the Strategy pattern billing calculations.
 * All tests use pure Java — no database connection required.
 */
public class ReservationBusinessLogicTest {

    private Reservation reservation;
    private Room room;
    private RoomType roomType;
    private final BigDecimal BASE_RATE = new BigDecimal("150.00");

    @Before
    public void setUp() {
        reservation = new Reservation();
        room = new Room();
        roomType = new RoomType(1, "Deluxe", 4, BASE_RATE);
        room.setRoomType(roomType);
        room.setRoomId(101);
        room.setRoomNumber("301");
    }

    // ==================== Strategy Selection Logic ====================

    @Test
    public void testStandardBillingForShortOffPeakStay() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 4)); // 3 nights, off-peak
        reservation.setRoom(room);

        BillingStrategy strategy = new StandardBillingStrategy();
        BigDecimal total = reservation.calculateTotalAmount(BASE_RATE, strategy);

        // 3 x Rs 150 = Rs 450
        assertEquals(new BigDecimal("450.00"), total);
    }

    @Test
    public void testSeasonalBillingForPeakSeason() {
        reservation.setCheckInDate(LocalDate.of(2026, 1, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 1, 13)); // 3 nights, peak season
        reservation.setRoom(room);

        BillingStrategy strategy = new SeasonalBillingStrategy();
        BigDecimal total = reservation.calculateTotalAmount(BASE_RATE, strategy);

        // 3 x Rs 150 x 1.25 = Rs 562.50
        assertEquals(new BigDecimal("562.50"), total);
    }

    @Test
    public void testLongStay7NightDiscount() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 8)); // 7 nights
        reservation.setRoom(room);

        BillingStrategy strategy = new LongStayBillingStrategy();
        BigDecimal total = reservation.calculateTotalAmount(BASE_RATE, strategy);

        // 7 x Rs 150 = Rs 1050, -10% = Rs 945
        assertEquals(new BigDecimal("945.00"), total);
    }

    @Test
    public void testLongStay14NightDiscount() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 15)); // 14 nights
        reservation.setRoom(room);

        BillingStrategy strategy = new LongStayBillingStrategy();
        BigDecimal total = reservation.calculateTotalAmount(BASE_RATE, strategy);

        // 14 x Rs 150 = Rs 2100, -20% = Rs 1680
        assertEquals(new BigDecimal("1680.00"), total);
    }

    @Test
    public void testLongStay30NightDiscount() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 7, 1)); // 30 nights
        reservation.setRoom(room);

        BillingStrategy strategy = new LongStayBillingStrategy();
        BigDecimal total = reservation.calculateTotalAmount(BASE_RATE, strategy);

        // 30 x Rs 150 = Rs 4500, -30% = Rs 3150
        assertEquals(new BigDecimal("3150.00"), total);
    }

    // ==================== calculateTotal via Room Reference ====================

    @Test
    public void testCalculateTotalUsesRoomRate() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 3)); // 2 nights
        reservation.setRoom(room);

        BigDecimal total = reservation.calculateTotal();
        assertNotNull("calculateTotal should return non-null", total);
        assertTrue("Total should be positive", total.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testCalculateTotalNullRoomReturnsZero() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 3));

        assertEquals("Null room should return zero",
                BigDecimal.ZERO, reservation.calculateTotal());
    }

    @Test
    public void testCalculateTotalNullRoomTypeReturnsZero() {
        Room roomNoType = new Room();
        reservation.setRoom(roomNoType);
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 3));

        assertEquals("Null room type should return zero",
                BigDecimal.ZERO, reservation.calculateTotal());
    }

    // ==================== State Machine Tests ====================

    @Test
    public void testFullReservationLifecycle() {
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(3));
        reservation.setRoom(room);

        // Step 1: New reservation is CONFIRMED
        assertEquals(Reservation.ReservationStatus.CONFIRMED, reservation.getStatus());
        assertTrue("Should be active", reservation.isActive());
        assertTrue("Should be cancellable", reservation.canBeCancelled());
        assertTrue("Should be able to check in today", reservation.canCheckIn());
        assertFalse("Cannot check out yet", reservation.canCheckOut());

        // Step 2: Check in
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        assertTrue("Should be active", reservation.isActive());
        assertFalse("Cannot cancel after check-in", reservation.canBeCancelled());
        assertFalse("Cannot check in again", reservation.canCheckIn());
        assertTrue("Should be able to check out", reservation.canCheckOut());

        // Step 3: Check out
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        assertFalse("Should no longer be active", reservation.isActive());
        assertFalse("Cannot cancel", reservation.canBeCancelled());
        assertFalse("Cannot check in", reservation.canCheckIn());
        assertFalse("Cannot check out again", reservation.canCheckOut());
    }

    @Test
    public void testCancellationLifecycle() {
        reservation.setCheckInDate(LocalDate.now());
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        assertTrue(reservation.canBeCancelled());

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        assertFalse("Cancelled = not active", reservation.isActive());
        assertFalse("Cannot cancel again", reservation.canBeCancelled());
        assertFalse("Cannot check in", reservation.canCheckIn());
    }

    @Test
    public void testNoShowStatus() {
        reservation.setStatus(Reservation.ReservationStatus.NO_SHOW);
        assertFalse(reservation.isActive());
        assertFalse(reservation.canCheckIn());
        assertFalse(reservation.canCheckOut());
        assertFalse(reservation.canBeCancelled());
    }

    // ==================== Object Composition ====================

    @Test
    public void testReservationWithGuestAndRoom() {
        Guest guest = new Guest("Nimal", "Fernando", "0771112233", "nimal@mail.com");
        guest.setGuestId(5);

        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 4));
        reservation.setNumberOfGuests(2);

        assertEquals(5, reservation.getGuestId());
        assertEquals("Nimal Fernando", reservation.getGuest().getFullName());
        assertEquals(101, reservation.getRoomId());
        assertEquals("301", reservation.getRoom().getRoomNumber());
        assertEquals(3, reservation.calculateNights());
        assertEquals(2, reservation.getNumberOfGuests());
    }

    // ==================== Method Overloading (Compile-time Polymorphism)
    // ====================

    @Test
    public void testMethodOverloadingBothVersions() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 4)); // 3 nights

        // Version 1: Simple calculation
        BigDecimal simple = reservation.calculateTotalAmount(BASE_RATE);
        assertEquals(new BigDecimal("450.00"), simple);

        // Version 2: With strategy
        BigDecimal withStrategy = reservation.calculateTotalAmount(
                BASE_RATE, new StandardBillingStrategy());
        assertEquals(new BigDecimal("450.00"), withStrategy);

        // Both should give same result for standard strategy in off-peak
        assertEquals("Both overloaded versions should match for standard off-peak",
                simple, withStrategy);
    }

    @Test
    public void testMethodOverloadingDifferentResults() {
        reservation.setCheckInDate(LocalDate.of(2026, 1, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 1, 13)); // 3 nights, peak season

        // Version 1: Simple (no strategy)
        BigDecimal simple = reservation.calculateTotalAmount(BASE_RATE);
        assertEquals(new BigDecimal("450.00"), simple);

        // Version 2: With seasonal strategy (25% surcharge)
        BigDecimal seasonal = reservation.calculateTotalAmount(
                BASE_RATE, new SeasonalBillingStrategy());
        assertEquals(new BigDecimal("562.50"), seasonal);

        // Seasonal should be more than simple
        assertTrue("Seasonal should be more than simple",
                seasonal.compareTo(simple) > 0);
    }
}
