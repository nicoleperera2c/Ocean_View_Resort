package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.Reservation;
import java.time.LocalDate;
import java.util.List;

/**
 * Reservation DAO interface - defines all reservation data access operations
 */
public interface IReservationDAO {

    int createReservation(Reservation reservation) throws DAOException;

    Reservation findById(int reservationId) throws DAOException;

    Reservation findByReservationNumber(String reservationNumber) throws DAOException;

    List<Reservation> findAll() throws DAOException;

    List<Reservation> findByGuestId(int guestId) throws DAOException;

    List<Reservation> findByStatus(Reservation.ReservationStatus status) throws DAOException;

    List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) throws DAOException;

    List<Reservation> findCheckInsForDate(LocalDate date) throws DAOException;

    List<Reservation> findCheckOutsForDate(LocalDate date) throws DAOException;

    /** Find all active reservations (CONFIRMED or CHECKED_IN) */
    List<Reservation> findActiveReservations() throws DAOException;

    boolean updateReservation(Reservation reservation) throws DAOException;

    boolean updateStatus(int reservationId, Reservation.ReservationStatus status) throws DAOException;

    boolean cancelReservation(int reservationId) throws DAOException;

    boolean checkIn(int reservationId) throws DAOException;

    boolean checkOut(int reservationId) throws DAOException;

    boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) throws DAOException;

    String generateReservationNumber() throws DAOException;
}
