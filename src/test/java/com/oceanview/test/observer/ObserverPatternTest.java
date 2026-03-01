package com.oceanview.test.observer;

import com.oceanview.model.Reservation;
import com.oceanview.model.Guest;
import com.oceanview.service.observer.ReservationObserver;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit tests for the Observer design pattern.
 * Uses a StubObserver (inner class) to verify the observer contract
 * without requiring a database connection.
 * Demonstrates Observer pattern polymorphism and extensibility.
 */
public class ObserverPatternTest {

    private StubObserver observer;
    private Reservation testReservation;

    /**
     * StubObserver - a test-only implementation of ReservationObserver
     * that records each method invocation for assertion purposes.
     * This proves the Observer interface contract is correctly defined.
     */
    static class StubObserver implements ReservationObserver {
        int createdCount = 0;
        int checkInCount = 0;
        int checkOutCount = 0;
        int cancelCount = 0;
        String lastAction = "";
        Reservation lastReservation = null;

        @Override
        public void onReservationCreated(Reservation reservation) {
            createdCount++;
            lastAction = "CREATED";
            lastReservation = reservation;
        }

        @Override
        public void onCheckIn(Reservation reservation) {
            checkInCount++;
            lastAction = "CHECKED_IN";
            lastReservation = reservation;
        }

        @Override
        public void onCheckOut(Reservation reservation) {
            checkOutCount++;
            lastAction = "CHECKED_OUT";
            lastReservation = reservation;
        }

        @Override
        public void onCancellation(Reservation reservation) {
            cancelCount++;
            lastAction = "CANCELLED";
            lastReservation = reservation;
        }

        public int getTotalCalls() {
            return createdCount + checkInCount + checkOutCount + cancelCount;
        }
    }

    @Before
    public void setUp() {
        observer = new StubObserver();

        testReservation = new Reservation();
        testReservation.setReservationId(1);
        testReservation.setReservationNumber("RES-TEST001");
        testReservation.setCheckInDate(LocalDate.of(2026, 6, 1));
        testReservation.setCheckOutDate(LocalDate.of(2026, 6, 5));
        testReservation.setTotalAmount(new BigDecimal("10000.00"));

        Guest guest = new Guest("Kamal", "Perera", "0771234567", "kamal@test.com");
        testReservation.setGuest(guest);
    }

    // ==================== Individual Observer Methods ====================

    @Test
    public void testOnReservationCreated() {
        observer.onReservationCreated(testReservation);

        assertEquals(1, observer.createdCount);
        assertEquals("CREATED", observer.lastAction);
        assertSame(testReservation, observer.lastReservation);
    }

    @Test
    public void testOnCheckIn() {
        observer.onCheckIn(testReservation);

        assertEquals(1, observer.checkInCount);
        assertEquals("CHECKED_IN", observer.lastAction);
        assertSame(testReservation, observer.lastReservation);
    }

    @Test
    public void testOnCheckOut() {
        observer.onCheckOut(testReservation);

        assertEquals(1, observer.checkOutCount);
        assertEquals("CHECKED_OUT", observer.lastAction);
        assertSame(testReservation, observer.lastReservation);
    }

    @Test
    public void testOnCancellation() {
        observer.onCancellation(testReservation);

        assertEquals(1, observer.cancelCount);
        assertEquals("CANCELLED", observer.lastAction);
        assertSame(testReservation, observer.lastReservation);
    }

    // ==================== Multiple Calls ====================

    @Test
    public void testMultipleCreatedNotifications() {
        observer.onReservationCreated(testReservation);
        observer.onReservationCreated(testReservation);
        observer.onReservationCreated(testReservation);

        assertEquals(3, observer.createdCount);
        assertEquals(3, observer.getTotalCalls());
    }

    @Test
    public void testMixedNotifications() {
        observer.onReservationCreated(testReservation);
        observer.onCheckIn(testReservation);
        observer.onCheckOut(testReservation);
        observer.onCancellation(testReservation);

        assertEquals(1, observer.createdCount);
        assertEquals(1, observer.checkInCount);
        assertEquals(1, observer.checkOutCount);
        assertEquals(1, observer.cancelCount);
        assertEquals(4, observer.getTotalCalls());
    }

    // ==================== Observer Polymorphism (Multiple Observers)
    // ====================

    @Test
    public void testMultipleObserversReceiveNotification() {
        StubObserver observer1 = new StubObserver();
        StubObserver observer2 = new StubObserver();
        StubObserver observer3 = new StubObserver();

        List<ReservationObserver> observers = new ArrayList<>();
        observers.add(observer1);
        observers.add(observer2);
        observers.add(observer3);

        // Simulate the notify pattern used in ReservationServiceImpl
        for (ReservationObserver obs : observers) {
            obs.onReservationCreated(testReservation);
        }

        assertEquals("Observer 1 should be notified", 1, observer1.createdCount);
        assertEquals("Observer 2 should be notified", 1, observer2.createdCount);
        assertEquals("Observer 3 should be notified", 1, observer3.createdCount);
    }

    @Test
    public void testObserverListCanGrowDynamically() {
        List<ReservationObserver> observers = new ArrayList<>();
        observers.add(new StubObserver());

        assertEquals(1, observers.size());

        observers.add(new StubObserver());
        observers.add(new StubObserver());

        assertEquals("Observer list should support dynamic growth",
                3, observers.size());

        // All three should receive the event
        for (ReservationObserver obs : observers) {
            obs.onCheckIn(testReservation);
        }
    }

    // ==================== Observer Interface Contract ====================

    @Test
    public void testStubObserverImplementsInterface() {
        assertTrue("StubObserver should implement ReservationObserver",
                observer instanceof ReservationObserver);
    }

    @Test
    public void testObserverReceivesCorrectReservationData() {
        observer.onReservationCreated(testReservation);

        assertEquals("RES-TEST001",
                observer.lastReservation.getReservationNumber());
        assertEquals(new BigDecimal("10000.00"),
                observer.lastReservation.getTotalAmount());
        assertEquals("Kamal Perera",
                observer.lastReservation.getGuest().getFullName());
    }

    @Test
    public void testObserverCountInitiallyZero() {
        StubObserver fresh = new StubObserver();
        assertEquals(0, fresh.getTotalCalls());
        assertEquals(0, fresh.createdCount);
        assertEquals(0, fresh.checkInCount);
        assertEquals(0, fresh.checkOutCount);
        assertEquals(0, fresh.cancelCount);
        assertEquals("", fresh.lastAction);
        assertNull(fresh.lastReservation);
    }
}
