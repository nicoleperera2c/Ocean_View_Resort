package com.oceanview.service.observer;

import com.oceanview.model.Reservation;

/**
 * Observer Pattern - Reservation Observer Interface
 * Allows multiple observers to react to reservation state changes
 * Demonstrates the Observer design pattern for the assignment
 */
public interface ReservationObserver {

    /**
     * Called when a reservation is created
     */
    void onReservationCreated(Reservation reservation);

    /**
     * Called when a guest checks in
     */
    void onCheckIn(Reservation reservation);

    /**
     * Called when a guest checks out
     */
    void onCheckOut(Reservation reservation);

    /**
     * Called when a reservation is cancelled
     */
    void onCancellation(Reservation reservation);
}
