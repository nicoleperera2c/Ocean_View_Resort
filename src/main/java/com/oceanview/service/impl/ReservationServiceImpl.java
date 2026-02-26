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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Reservation Service Implementation
 * Handles business logic for reservation management
 * Implements SRP: only reservation-related operations
 */
public class ReservationServiceImpl implements IReservationService {

    private IReservationDAO reservationDAO;
    private IRoomDAO roomDAO;

    public ReservationServiceImpl() {
        DAOFactory factory = DAOFactory.getInstance();
        this.reservationDAO = factory.getReservationDAO();
        this.roomDAO = factory.getRoomDAO();
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

                // Calculate total amount
                BigDecimal totalAmount = reservation.calculateTotal();
                reservation.setTotalAmount(totalAmount);

                // Generate reservation number
                reservation.setReservationNumber(reservationDAO.generateReservationNumber());

                // Set default status
                reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

                // Create reservation transactionally
                int reservationId = reservationDAO.createReservation(reservation);
                reservation.setReservationId(reservationId);
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
