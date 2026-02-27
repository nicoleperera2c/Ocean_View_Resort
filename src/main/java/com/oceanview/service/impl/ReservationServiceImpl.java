package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IReservationDAO;
import com.oceanview.dao.interfaces.IRoomDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.interfaces.IReservationService;
import com.oceanview.util.ValidationUtil;

import com.oceanview.service.observer.ReservationObserver;
import com.oceanview.service.observer.AuditLogObserver;
import com.oceanview.service.observer.EmailNotificationObserver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation Service Implementation
 * Handles business logic for reservation management
 * Implements SRP: only reservation-related operations
 */
public class ReservationServiceImpl implements IReservationService {

    private IReservationDAO reservationDAO;
    private IRoomDAO roomDAO;
    private List<ReservationObserver> observers;

    public ReservationServiceImpl() {
        DAOFactory factory = DAOFactory.getInstance();
        this.reservationDAO = factory.getReservationDAO();
        this.roomDAO = factory.getRoomDAO();

        // Initialize Observer Pattern
        this.observers = new ArrayList<>();
        this.observers.add(new AuditLogObserver());
        this.observers.add(new EmailNotificationObserver());
    }

    private void notifyObservers(Reservation reservation, String action) {
        for (ReservationObserver observer : observers) {
            switch (action) {
                case "CREATED":
                    observer.onReservationCreated(reservation);
                    break;
                case "CHECKED_IN":
                    observer.onCheckIn(reservation);
                    break;
                case "CHECKED_OUT":
                    observer.onCheckOut(reservation);
                    break;
                case "CANCELLED":
                    observer.onCancellation(reservation);
                    break;
            }
        }
    }

    /**
     * Strategy Pattern: dynamically select the appropriate billing algorithm.
     * - LongStayBillingStrategy for stays of 7+ nights (discount tiers)
     * - SeasonalBillingStrategy for peak season bookings (Dec-Mar surcharge)
     * - StandardBillingStrategy as the default
     */
    private com.oceanview.service.strategy.BillingStrategy selectBillingStrategy(LocalDate checkIn,
            LocalDate checkOut) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        // Long stays get tiered discounts — this takes priority
        if (nights >= 7) {
            return new com.oceanview.service.strategy.LongStayBillingStrategy();
        }

        // Check if booking falls in peak season (Dec-Mar)
        java.time.Month checkInMonth = checkIn.getMonth();
        if (checkInMonth == java.time.Month.DECEMBER || checkInMonth == java.time.Month.JANUARY ||
                checkInMonth == java.time.Month.FEBRUARY || checkInMonth == java.time.Month.MARCH) {
            return new com.oceanview.service.strategy.SeasonalBillingStrategy();
        }

        // Default: standard billing
        return new com.oceanview.service.strategy.StandardBillingStrategy();
    }

    @Override
    public Reservation createReservation(Reservation reservation) throws ServiceException {
        // Validate dates
        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            throw new ServiceException("Check-in and check-out dates are required");
        }
        if (!ValidationUtil.isValidDateRange(reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            throw new ServiceException(
                    "Invalid date range: check-in must be today or future, check-out must be after check-in");
        }

        // Validate guest and room
        if (reservation.getGuestId() <= 0) {
            throw new ServiceException("Valid guest is required");
        }
        if (reservation.getRoomId() <= 0) {
            throw new ServiceException("Valid room is required");
        }
        if (reservation.getNumberOfGuests() <= 0) {
            throw new ServiceException("Number of guests must be at least 1");
        }

        try {
            // Fetch room details for validation first
            Room room = roomDAO.findById(reservation.getRoomId());
            if (room == null) {
                throw new ServiceException("Room not found");
            }

            // Validate capacity
            if (room.getRoomType() != null && reservation.getNumberOfGuests() > room.getRoomType().getMaxOccupancy()) {
                throw new ServiceException("Number of guests exceeds the maximum room capacity of "
                        + room.getRoomType().getMaxOccupancy() + " guests.");
            }

            // Validate maintenance status
            if (room.getStatus() == Room.RoomStatus.MAINTENANCE) {
                throw new ServiceException("This room is currently under maintenance and cannot be booked.");
            }

            // Lock on the specific room ID to completely prevent race conditions and
            // concurrent double reservations
            synchronized (String.valueOf(reservation.getRoomId()).intern()) {
                // Check room availability strictly
                if (!reservationDAO.isRoomAvailable(reservation.getRoomId(),
                        reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                    throw new ServiceException(
                            "Room is no longer available for the selected dates. Another user may have just booked it.");
                }

                reservation.setRoom(room);

                // Strategy Pattern: dynamically select the billing algorithm
                com.oceanview.service.strategy.BillingStrategy billingStrategy = selectBillingStrategy(
                        reservation.getCheckInDate(), reservation.getCheckOutDate());
                BigDecimal baseRate = room.getRoomType().getBasePrice();
                BigDecimal totalAmount = reservation.calculateTotalAmount(baseRate, billingStrategy);
                reservation.setTotalAmount(totalAmount);

                // Generate reservation number
                reservation.setReservationNumber(reservationDAO.generateReservationNumber());

                // Set default status
                reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

                // Create reservation transactionally
                int reservationId = reservationDAO.createReservation(reservation);
                reservation.setReservationId(reservationId);

                // Trigger Observer Pattern Notification
                notifyObservers(reservation, "CREATED");
            }

            return reservation;

        } catch (DAOException e) {
            throw new ServiceException("Error creating reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public Reservation getReservationDetails(String reservationNumber) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(reservationNumber)) {
            throw new ServiceException("Reservation number is required");
        }

        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                throw new ServiceException("Reservation not found: " + reservationNumber);
            }
            return reservation;
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> getAllReservations() throws ServiceException {
        try {
            return reservationDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving reservations: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> getReservationsByGuest(int guestId) throws ServiceException {
        if (guestId <= 0) {
            throw new ServiceException("Valid guest ID is required");
        }

        try {
            return reservationDAO.findByGuestId(guestId);
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving guest reservations: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) throws ServiceException {
        if (checkIn == null || checkOut == null) {
            throw new ServiceException("Check-in and check-out dates are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new ServiceException("Check-out date must be after check-in date");
        }

        try {
            return roomDAO.findAvailableRoomsByDateRange(checkIn, checkOut);
        } catch (DAOException e) {
            throw new ServiceException("Error finding available rooms: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkInGuest(String reservationNumber) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(reservationNumber)) {
            throw new ServiceException("Reservation number is required");
        }

        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                throw new ServiceException("Reservation not found");
            }

            if (!reservation.canCheckIn()) {
                throw new ServiceException("Reservation cannot be checked in. Status: " + reservation.getStatus());
            }

            // Update reservation status
            boolean updated = reservationDAO.checkIn(reservation.getReservationId());

            if (updated) {
                // Update room status to OCCUPIED
                roomDAO.updateRoomStatus(reservation.getRoomId(), Room.RoomStatus.OCCUPIED);
                // Observer Pattern: notify all observers of check-in event
                reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
                notifyObservers(reservation, "CHECKED_IN");
            }

            return updated;

        } catch (DAOException e) {
            throw new ServiceException("Error checking in guest: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkOutGuest(String reservationNumber) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(reservationNumber)) {
            throw new ServiceException("Reservation number is required");
        }

        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                throw new ServiceException("Reservation not found");
            }

            if (!reservation.canCheckOut()) {
                throw new ServiceException("Reservation cannot be checked out. Status: " + reservation.getStatus());
            }

            // Update reservation status
            boolean updated = reservationDAO.checkOut(reservation.getReservationId());

            if (updated) {
                // Update room status back to AVAILABLE
                roomDAO.updateRoomStatus(reservation.getRoomId(), Room.RoomStatus.AVAILABLE);
                // Observer Pattern: notify all observers of check-out event
                reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
                notifyObservers(reservation, "CHECKED_OUT");
            }

            return updated;

        } catch (DAOException e) {
            throw new ServiceException("Error checking out guest: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelReservation(String reservationNumber) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(reservationNumber)) {
            throw new ServiceException("Reservation number is required");
        }

        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                throw new ServiceException("Reservation not found");
            }

            if (!reservation.canBeCancelled()) {
                throw new ServiceException("Reservation cannot be cancelled. Status: " + reservation.getStatus());
            }

            boolean cancelled = reservationDAO.cancelReservation(reservation.getReservationId());

            if (cancelled) {
                // Free up the room
                roomDAO.updateRoomStatus(reservation.getRoomId(), Room.RoomStatus.AVAILABLE);
                // Observer Pattern: notify all observers of cancellation event
                notifyObservers(reservation, "CANCELLED");
            }

            return cancelled;

        } catch (DAOException e) {
            throw new ServiceException("Error cancelling reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> getTodayCheckIns() throws ServiceException {
        try {
            return reservationDAO.findCheckInsForDate(LocalDate.now());
        } catch (DAOException e) {
            throw new ServiceException("Error getting today's check-ins: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> getTodayCheckOuts() throws ServiceException {
        try {
            return reservationDAO.findCheckOutsForDate(LocalDate.now());
        } catch (DAOException e) {
            throw new ServiceException("Error getting today's check-outs: " + e.getMessage(), e);
        }
    }
}
