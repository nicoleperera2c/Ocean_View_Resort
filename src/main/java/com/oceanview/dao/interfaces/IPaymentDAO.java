package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.Payment;
import java.math.BigDecimal;
import java.util.List;

/**
 * IPaymentDAO - Data access contract for the payments table.
 * All payment persistence goes through this interface.
 */
public interface IPaymentDAO {

    /** Insert a new payment record and return its generated ID. */
    int createPayment(Payment payment) throws DAOException;

    /** Look up a single payment by its primary key. */
    Payment findById(int paymentId) throws DAOException;

    /** Retrieve all payments for a given reservation, newest first. */
    List<Payment> findByReservationId(int reservationId) throws DAOException;

    /** Update the status of an existing payment (e.g. COMPLETED -> REFUNDED). */
    boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws DAOException;

    /**
     * Check whether the reservation already has at least one COMPLETED payment.
     * Used to enforce the duplicate-payment business rule.
     */
    boolean hasCompletedPayment(int reservationId) throws DAOException;

    /**
     * Sum all COMPLETED payments for a reservation.
     * Returns BigDecimal.ZERO if no completed payments exist.
     */
    BigDecimal getTotalPaidAmount(int reservationId) throws DAOException;

    /** Retrieve every payment record in the system (for reporting). */
    List<Payment> findAll() throws DAOException;
}
