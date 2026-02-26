package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    
    Reservation createReservation(Reservation reservation) throws ServiceException;
    
    Reservation getReservationDetails(String reservationNumber) throws ServiceException;
    
    List<Reservation> getAllReservations() throws ServiceException;
    
    List<Reservation> getReservationsByGuest(int guestId) throws ServiceException;
    
    List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) throws ServiceException;
    
    boolean checkInGuest(String reservationNumber) throws ServiceException;
    
    boolean checkOutGuest(String reservationNumber) throws ServiceException;
    
    boolean cancelReservation(String reservationNumber) throws ServiceException;
    
    List<Reservation> getTodayCheckIns() throws ServiceException;
    
    List<Reservation> getTodayCheckOuts() throws ServiceException;
}
