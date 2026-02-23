package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Reservation model - represents a hotel room booking
 * Maps to reservations table in database
 */
public class Reservation {

    private int reservationId;
    private String reservationNumber;
    private int guestId;
    private Guest guest;
    private int roomId;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private BigDecimal totalAmount;
    private String specialRequests;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Enum matching DB
     * ENUM('CONFIRMED','CHECKED_IN','CHECKED_OUT','CANCELLED','NO_SHOW')
     */
    public enum ReservationStatus {
        CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW
    }

    public Reservation() {
        this.status = ReservationStatus.CONFIRMED;
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

    // ---- Business Methods ----

    /**
     * Calculate the number of nights for this reservation
     * Uses DAYS.between algorithm for precise date arithmetic
     */
    public long calculateNights() {
        if (checkInDate == null || checkOutDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    /**
     * Calculate total amount given a nightly rate
     * Strategy pattern can override this via BillingStrategy
     */
    public BigDecimal calculateTotalAmount(BigDecimal ratePerNight) {
        if (ratePerNight == null) {
            return BigDecimal.ZERO;
        }
        long nights = calculateNights();
        if (nights <= 0) {
            return BigDecimal.ZERO;
        }
        return ratePerNight.multiply(BigDecimal.valueOf(nights));
    }

    /**
     * Calculate total using attached room's rate
     */
    public BigDecimal calculateTotal() {
        if (room == null || room.getRoomType() == null) {
            return BigDecimal.ZERO;
        }
        return calculateTotalAmount(room.getRoomType().getBasePrice());
    }

    /**
     * Check if guest can check in (must be CONFIRMED and date is today or past)
     */
    public boolean canCheckIn() {
        return status == ReservationStatus.CONFIRMED &&
                checkInDate != null &&
                !checkInDate.isAfter(LocalDate.now());
    }

    /**
     * Check if guest can check out (must be currently checked in)
     */
    public boolean canCheckOut() {
        return status == ReservationStatus.CHECKED_IN;
    }

    /**
     * Check if reservation can be cancelled
     */
    public boolean canBeCancelled() {
        return status == ReservationStatus.CONFIRMED;
    }

    /**
     * Alias for canBeCancelled()
     */
    public boolean canCancel() {
        return canBeCancelled();
    }

    /**
     * Check if reservation is currently active
     */
    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED ||
                status == ReservationStatus.CHECKED_IN;
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
