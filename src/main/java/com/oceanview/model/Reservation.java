package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reservation {
    
    private int reservationId;
    private String reservationNumber;
    private int guestId;
    private Guest guest;
    private int roomId;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime actualCheckIn;
    private LocalDateTime actualCheckOut;
    private int numberOfGuests;
    private ReservationStatus status;
    private BigDecimal totalAmount;
    private String specialRequests;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum ReservationStatus {
        PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    }
    
    public Reservation() {
        this.status = ReservationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public String getReservationNumber() {
        return reservationNumber;
    }
    
    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
    
    public int getGuestId() {
        return guestId;
    }
    
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
    
    public Guest getGuest() {
        return guest;
    }
    
    public void setGuest(Guest guest) {
        this.guest = guest;
        if (guest != null) {
            this.guestId = guest.getGuestId();
        }
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
        if (room != null) {
            this.roomId = room.getRoomId();
        }
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public LocalDateTime getActualCheckIn() {
        return actualCheckIn;
    }
    
    public void setActualCheckIn(LocalDateTime actualCheckIn) {
        this.actualCheckIn = actualCheckIn;
    }
    
    public LocalDateTime getActualCheckOut() {
        return actualCheckOut;
    }
    
    public void setActualCheckOut(LocalDateTime actualCheckOut) {
        this.actualCheckOut = actualCheckOut;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public long calculateNights() {
        if (checkInDate == null || checkOutDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    public BigDecimal calculateTotal() {
        if (room == null || room.getRoomType() == null) {
            return BigDecimal.ZERO;
        }
        long nights = calculateNights();
        return room.getRoomType().getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));
    }
    
    public boolean canCheckIn() {
        return status == ReservationStatus.CONFIRMED && 
               LocalDate.now().equals(checkInDate);
    }
    
    public boolean canCheckOut() {
        return status == ReservationStatus.CHECKED_IN;
    }
    
    public boolean canCancel() {
        return status == ReservationStatus.PENDING || 
               status == ReservationStatus.CONFIRMED;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber='" + reservationNumber + '\'' +
                ", guest=" + (guest != null ? guest.getFullName() : "N/A") +
                ", room=" + (room != null ? room.getRoomNumber() : "N/A") +
                ", checkIn=" + checkInDate +
                ", checkOut=" + checkOutDate +
                ", status=" + status +
                ", total=" + totalAmount +
                '}';
    }
}
