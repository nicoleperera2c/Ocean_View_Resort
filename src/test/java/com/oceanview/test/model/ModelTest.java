package com.oceanview.test.model;

import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.model.Guest;
import com.oceanview.model.Payment;
import com.oceanview.model.User;
import com.oceanview.service.strategy.StandardBillingStrategy;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Comprehensive JUnit tests for all model classes.
 * Tests encapsulation, business methods, enums, constructors,
 * object composition, and backward-compatible aliases.
 */
public class ModelTest {

    private Reservation reservation;
    private Room room;
    private RoomType roomType;
    private Guest guest;

    @Before
    public void setUp() {
        reservation = new Reservation();
        room = new Room();
        roomType = new RoomType();
        guest = new Guest();
    }

    // Reservation: calculateNights 

    @Test
    public void testReservationCalculateNights() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 5));
        assertEquals(4, reservation.calculateNights());
    }

    @Test
    public void testReservationCalculateNightsOneNight() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 11));
        assertEquals(1, reservation.calculateNights());
    }

    @Test
    public void testReservationCalculateNightsWithNullDates() {
        assertEquals(0, reservation.calculateNights());
    }

    @Test
    public void testReservationCalculateNightsNullCheckIn() {
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 5));
        assertEquals(0, reservation.calculateNights());
    }

    @Test
    public void testReservationCalculateNightsNullCheckOut() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        assertEquals(0, reservation.calculateNights());
    }

    //  Reservation: calculateTotalAmount 

    @Test
    public void testReservationCalculateTotalAmount() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 5));

        BigDecimal rate = new BigDecimal("100.00");
        BigDecimal total = reservation.calculateTotalAmount(rate);
        assertEquals(new BigDecimal("400.00"), total);
    }

    @Test
    public void testReservationCalculateTotalAmountNullRate() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 5));
        assertEquals(BigDecimal.ZERO, reservation.calculateTotalAmount(null));
    }

    @Test
    public void testReservationCalculateTotalAmountZeroNights() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 1));
        BigDecimal total = reservation.calculateTotalAmount(new BigDecimal("100.00"));
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testReservationCalculateTotalWithStrategy() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 4));
        BigDecimal rate = new BigDecimal("100.00");

        BigDecimal total = reservation.calculateTotalAmount(rate, new StandardBillingStrategy());
        assertEquals(new BigDecimal("300.00"), total);
    }

    @Test
    public void testReservationCalculateTotalWithNullStrategy() {
        reservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 6, 4));
        BigDecimal rate = new BigDecimal("100.00");

        // null strategy should fall back to basic calculation
        BigDecimal total = reservation.calculateTotalAmount(rate, null);
        assertEquals(new BigDecimal("300.00"), total);
    }

    @Test
    public void testReservationCalculateTotalWithRoom() {
        reservation.setCheckInDate(LocalDate.of(2026, 3, 1));
        reservation.setCheckOutDate(LocalDate.of(2026, 3, 4));

        roomType.setBasePrice(new BigDecimal("150.00"));
        room.setRoomType(roomType);
        reservation.setRoom(room);

        BigDecimal total = reservation.calculateTotal();
        assertNotNull("Total should not be null", total);
        assertTrue("Total should be positive", total.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testReservationCalculateTotalWithNullRoom() {
        assertEquals(BigDecimal.ZERO, reservation.calculateTotal());
    }

    // Reservation: State Checks 

    @Test
    public void testReservationCanCheckInWhenConfirmedAndDateToday() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now());
        assertTrue("CONFIRMED + today = can check in", reservation.canCheckIn());
    }

    @Test
    public void testReservationCanCheckInWhenConfirmedAndDatePast() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now().minusDays(1));
        assertTrue("CONFIRMED + past date = can check in", reservation.canCheckIn());
    }

    @Test
    public void testReservationCannotCheckInWhenFutureDate() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setCheckInDate(LocalDate.now().plusDays(1));
        assertFalse("Future check-in date = cannot check in", reservation.canCheckIn());
    }

    @Test
    public void testReservationCannotCheckInWhenAlreadyCheckedIn() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        reservation.setCheckInDate(LocalDate.now());
        assertFalse("CHECKED_IN = cannot check in again", reservation.canCheckIn());
    }

    @Test
    public void testReservationCannotCheckInWhenCancelled() {
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservation.setCheckInDate(LocalDate.now());
        assertFalse("CANCELLED = cannot check in", reservation.canCheckIn());
    }

    @Test
    public void testReservationCanCheckOutWhenCheckedIn() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        assertTrue(reservation.canCheckOut());
    }

    @Test
    public void testReservationCannotCheckOutWhenConfirmed() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        assertFalse(reservation.canCheckOut());
    }

    @Test
    public void testReservationCannotCheckOutWhenAlreadyOut() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        assertFalse(reservation.canCheckOut());
    }

    @Test
    public void testReservationCanBeCancelledWhenConfirmed() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        assertTrue(reservation.canBeCancelled());
        assertTrue(reservation.canCancel()); // alias
    }

    @Test
    public void testReservationCannotBeCancelledWhenCheckedIn() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        assertFalse(reservation.canBeCancelled());
    }

    @Test
    public void testReservationCannotBeCancelledWhenCheckedOut() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        assertFalse(reservation.canBeCancelled());
    }

    @Test
    public void testReservationIsActiveForConfirmedAndCheckedIn() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        assertTrue(reservation.isActive());

        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        assertTrue(reservation.isActive());
    }

    @Test
    public void testReservationIsNotActiveForOtherStatuses() {
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        assertFalse(reservation.isActive());

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        assertFalse(reservation.isActive());

        reservation.setStatus(Reservation.ReservationStatus.NO_SHOW);
        assertFalse(reservation.isActive());
    }

    // Reservation: Encapsulation (setStatus auto-updates)
    

    @Test
    public void testReservationSetStatusUpdatesTimestamp() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull("updatedAt should be set", reservation.getUpdatedAt());
        assertTrue("updatedAt should be after 'before'",
                !reservation.getUpdatedAt().isBefore(before));
        assertTrue("updatedAt should be before 'after'",
                !reservation.getUpdatedAt().isAfter(after));
    }

    // Reservation: Object Composition 

    @Test
    public void testReservationCompositionWithGuest() {
        Guest g = new Guest("Kamal", "Perera", "0771234567", "kamal@test.com");
        g.setGuestId(10);
        reservation.setGuest(g);

        assertEquals(10, reservation.getGuestId());
        assertNotNull(reservation.getGuest());
        assertEquals("Kamal Perera", reservation.getGuest().getFullName());
    }

    @Test
    public void testReservationCompositionWithRoom() {
        room.setRoomId(5);
        room.setRoomNumber("201");
        reservation.setRoom(room);

        assertEquals(5, reservation.getRoomId());
        assertEquals("201", reservation.getRoom().getRoomNumber());
    }

    // Reservation: Enums 

    @Test
    public void testReservationStatusEnum() {
        assertNotNull(Reservation.ReservationStatus.CONFIRMED);
        assertNotNull(Reservation.ReservationStatus.CHECKED_IN);
        assertNotNull(Reservation.ReservationStatus.CHECKED_OUT);
        assertNotNull(Reservation.ReservationStatus.CANCELLED);
        assertNotNull(Reservation.ReservationStatus.NO_SHOW);
        assertEquals("ReservationStatus should have 5 values",
                5, Reservation.ReservationStatus.values().length);
    }

    @Test
    public void testReservationDefaultStatus() {
        Reservation r = new Reservation();
        assertEquals("Default status should be CONFIRMED",
                Reservation.ReservationStatus.CONFIRMED, r.getStatus());
    }

    @Test
    public void testReservationToString() {
        reservation.setReservationNumber("RES-001");
        String str = reservation.toString();
        assertNotNull(str);
        assertTrue("toString should contain reservation number",
                str.contains("RES-001"));
    }

    // Room Tests 

    @Test
    public void testRoomStatusEnum() {
        assertNotNull(Room.RoomStatus.AVAILABLE);
        assertNotNull(Room.RoomStatus.OCCUPIED);
        assertNotNull(Room.RoomStatus.RESERVED);
        assertNotNull(Room.RoomStatus.MAINTENANCE);
        assertNotNull(Room.RoomStatus.CLEANING);
        assertEquals("RoomStatus should have 5 values",
                5, Room.RoomStatus.values().length);
    }

    @Test
    public void testRoomDefaultStatus() {
        Room r = new Room();
        assertEquals(Room.RoomStatus.AVAILABLE, r.getStatus());
    }

    @Test
    public void testRoomIsAvailable() {
        room.setStatus(Room.RoomStatus.AVAILABLE);
        assertTrue(room.isAvailable());

        room.setStatus(Room.RoomStatus.OCCUPIED);
        assertFalse(room.isAvailable());

        room.setStatus(Room.RoomStatus.MAINTENANCE);
        assertFalse(room.isAvailable());
    }

    @Test
    public void testRoomFloorNumberMapping() {
        room.setFloorNumber(3);
        assertEquals(3, room.getFloorNumber());
        assertEquals("3", room.getFloor());
    }

    @Test
    public void testRoomFloorAlias() {
        room.setFloor("5");
        assertEquals(5, room.getFloorNumber());
    }

    @Test
    public void testRoomFloorAliasInvalidValue() {
        room.setFloor("abc");
        assertEquals("Invalid floor should default to 0", 0, room.getFloorNumber());
    }

    @Test
    public void testRoomConstructor() {
        Room r = new Room(1, "101", 2);
        assertEquals(1, r.getRoomId());
        assertEquals("101", r.getRoomNumber());
        assertEquals(2, r.getRoomTypeId());
        assertEquals(Room.RoomStatus.AVAILABLE, r.getStatus());
    }

    @Test
    public void testRoomSetRoomTypeSyncsId() {
        RoomType rt = new RoomType();
        rt.setRoomTypeId(3);
        room.setRoomType(rt);
        assertEquals(3, room.getRoomTypeId());
    }

    @Test
    public void testRoomToString() {
        room.setRoomNumber("305");
        String str = room.toString();
        assertNotNull(str);
        assertTrue(str.contains("305"));
    }

    // RoomType Tests 

    @Test
    public void testRoomTypeAliasGetters() {
        roomType.setBasePrice(new BigDecimal("200.00"));
        roomType.setMaxOccupancy(4);

        assertEquals(new BigDecimal("200.00"), roomType.getBasePrice());
        assertEquals(4, roomType.getMaxOccupancy());
        assertEquals(new BigDecimal("200.00"), roomType.getPricePerNight());
        assertEquals(4, roomType.getCapacity());
    }

    @Test
    public void testRoomTypeAliasSetter() {
        roomType.setPricePerNight(new BigDecimal("300.00"));
        roomType.setCapacity(6);

        assertEquals(new BigDecimal("300.00"), roomType.getBasePrice());
        assertEquals(6, roomType.getMaxOccupancy());
    }

    @Test
    public void testRoomTypeConstructor() {
        RoomType rt = new RoomType(1, "Deluxe", 4, new BigDecimal("250.00"));
        assertEquals(1, rt.getRoomTypeId());
        assertEquals("Deluxe", rt.getTypeName());
        assertEquals(4, rt.getMaxOccupancy());
        assertEquals(new BigDecimal("250.00"), rt.getBasePrice());
        assertTrue(rt.isActive());
    }

    @Test
    public void testRoomTypeDefaultActive() {
        assertTrue("New RoomType should be active", new RoomType().isActive());
    }

    // Guest Tests 

    @Test
    public void testGuestFullName() {
        Guest g = new Guest("John", "Doe", "1234567890", "john@test.com");
        assertEquals("John Doe", g.getFullName());
    }

    @Test
    public void testGuestConstructor() {
        Guest g = new Guest("Nimal", "Silva", "0771234567", "nimal@mail.com");
        assertEquals("Nimal", g.getFirstName());
        assertEquals("Silva", g.getLastName());
        assertEquals("0771234567", g.getPhone());
        assertEquals("nimal@mail.com", g.getEmail());
        assertNotNull("createdAt should be set", g.getCreatedAt());
    }

    @Test
    public void testGuestCreatedAtAlias() {
        LocalDateTime now = LocalDateTime.now();
        guest.setCreatedAt(now);
        assertEquals(now, guest.getRegisteredAt());
    }

    @Test
    public void testGuestRegisteredAtAlias() {
        LocalDateTime now = LocalDateTime.now();
        guest.setRegisteredAt(now);
        assertEquals(now, guest.getCreatedAt());
    }

    @Test
    public void testGuestToString() {
        guest.setFirstName("Saman");
        guest.setLastName("Kumara");
        String str = guest.toString();
        assertNotNull(str);
        assertTrue(str.contains("Saman Kumara"));
    }

    // Payment Tests 

    @Test
    public void testPaymentMethodEnum() {
        assertNotNull(Payment.PaymentMethod.CASH);
        assertNotNull(Payment.PaymentMethod.CREDIT_CARD);
        assertNotNull(Payment.PaymentMethod.DEBIT_CARD);
        assertNotNull(Payment.PaymentMethod.BANK_TRANSFER);
        assertNotNull(Payment.PaymentMethod.ONLINE);
        assertEquals("PaymentMethod should have 5 values",
                5, Payment.PaymentMethod.values().length);
    }

    @Test
    public void testPaymentStatusEnum() {
        assertNotNull(Payment.PaymentStatus.COMPLETED);
        assertNotNull(Payment.PaymentStatus.PENDING);
        assertNotNull(Payment.PaymentStatus.REFUNDED);
        assertNotNull(Payment.PaymentStatus.FAILED);
        assertEquals("PaymentStatus should have 4 values",
                4, Payment.PaymentStatus.values().length);
    }

    @Test
    public void testPaymentDefaultStatus() {
        Payment p = new Payment();
        assertEquals(Payment.PaymentStatus.PENDING, p.getPaymentStatus());
        assertNotNull("paymentDate should be set", p.getPaymentDate());
    }

    @Test
    public void testPaymentTransactionReferenceAlias() {
        Payment p = new Payment();
        p.setTransactionReference("TXN-12345678");
        assertEquals("TXN-12345678", p.getTransactionReference());
        assertEquals("TXN-12345678", p.getTransactionId());
    }

    @Test
    public void testPaymentTransactionIdAlias() {
        Payment p = new Payment();
        p.setTransactionId("TXN-ABCD");
        assertEquals("TXN-ABCD", p.getTransactionReference());
    }

    @Test
    public void testPaymentStatusAlias() {
        Payment p = new Payment();
        p.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        assertEquals(Payment.PaymentStatus.COMPLETED, p.getPaymentStatus());
        assertEquals(Payment.PaymentStatus.COMPLETED, p.getStatus());
    }

    @Test
    public void testPaymentSetStatusAlias() {
        Payment p = new Payment();
        p.setStatus(Payment.PaymentStatus.REFUNDED);
        assertEquals(Payment.PaymentStatus.REFUNDED, p.getPaymentStatus());
    }

    @Test
    public void testPaymentToString() {
        Payment p = new Payment();
        p.setPaymentId(1);
        p.setAmount(new BigDecimal("5000.00"));
        String str = p.toString();
        assertNotNull(str);
        assertTrue(str.contains("5000.00"));
    }

    // User Tests 

    @Test
    public void testUserRoleEnum() {
        assertNotNull(User.UserRole.ADMIN);
        assertNotNull(User.UserRole.MANAGER);
        assertNotNull(User.UserRole.STAFF);
        assertEquals("UserRole should have 3 values",
                3, User.UserRole.values().length);
    }

    @Test
    public void testUserDefaultConstructor() {
        User u = new User();
        assertTrue("New user should be active", u.isActive());
        assertNotNull("createdAt should be set", u.getCreatedAt());
    }

    @Test
    public void testUserParameterizedConstructor() {
        User u = new User(1, "admin", "Admin User", User.UserRole.ADMIN);
        assertEquals(1, u.getUserId());
        assertEquals("admin", u.getUsername());
        assertEquals("Admin User", u.getFullName());
        assertEquals(User.UserRole.ADMIN, u.getRole());
        assertTrue(u.isActive());
    }

    @Test
    public void testUserHasRole() {
        User u = new User(1, "mgr", "Manager", User.UserRole.MANAGER);
        assertTrue(u.hasRole(User.UserRole.MANAGER));
        assertFalse(u.hasRole(User.UserRole.ADMIN));
        assertFalse(u.hasRole(User.UserRole.STAFF));
    }

    @Test
    public void testUserIsAdmin() {
        User admin = new User(1, "admin", "Admin", User.UserRole.ADMIN);
        assertTrue(admin.isAdmin());

        User staff = new User(2, "staff", "Staff", User.UserRole.STAFF);
        assertFalse(staff.isAdmin());
    }

    @Test
    public void testUserEncapsulation() {
        User u = new User();
        u.setPasswordHash("hashed_secret");
        assertEquals("hashed_secret", u.getPasswordHash());

        u.setEmail("test@ocean.lk");
        assertEquals("test@ocean.lk", u.getEmail());

        u.setPhone("0771234567");
        assertEquals("0771234567", u.getPhone());
    }

    @Test
    public void testUserToString() {
        User u = new User(1, "admin", "Admin User", User.UserRole.ADMIN);
        String str = u.toString();
        assertNotNull(str);
        assertTrue(str.contains("admin"));
        assertTrue(str.contains("Admin User"));
    }
}
