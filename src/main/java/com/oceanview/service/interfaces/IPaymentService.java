package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Payment;
import java.math.BigDecimal;
import java.util.List;

/**
 * IPaymentService - Payment Business Logic Contract
 *
 * Defines the strict rules for payment processing in the hotel system.
 * This service is the ONLY authoritative path for creating, querying and
 * managing payments. Controllers must never touch the DAO directly.
 *
 * Business Rules enforced by implementations:
 * 1. A COMPLETED payment cannot be created for a reservation that
 * already has a COMPLETED payment (duplicate payment prevention).
 * 2. Payment can only be processed for reservations that are in
 * CONFIRMED or CHECKED_IN status.
 * 3. Payment amount must match the reservation's outstanding balance exactly.
 * 4. Payment amounts must be positive and non-zero.
 * 5. Refunds can only be issued against COMPLETED payments.
 */
public interface IPaymentService {

    /**
     * Process a payment for a reservation.
     * Enforces all business rules before inserting the record.
     *
     * @param reservationNumber the reservation to pay for
     * @param amount            the amount being tendered (must equal outstanding
     *                          balance)
     * @param method            the payment method
     * @param notes             optional notes from the desk clerk
     * @param processedByUserId the ID of the user processing the payment
     * @return the created Payment record with generated transaction reference
     * @throws ServiceException if any business rule is violated
     */
    Payment processPayment(String reservationNumber, BigDecimal amount,
            Payment.PaymentMethod method, String notes, int processedByUserId)
            throws ServiceException;

    /**
     * Retrieve all payments linked to a reservation number.
     *
     * @param reservationNumber the reservation identifier
     * @return list of payments, newest first
     * @throws ServiceException on lookup failure
     */
    List<Payment> getPaymentsForReservation(String reservationNumber) throws ServiceException;

    /**
     * Find a specific payment by its internal ID.
     *
     * @param paymentId internal payment identifier
     * @return the Payment record
     * @throws ServiceException if not found or on DB error
     */
    Payment getPaymentById(int paymentId) throws ServiceException;

    /**
     * Issue a refund for a previously COMPLETED payment.
     * Marks the payment as REFUNDED and updates reservation status.
     *
     * @param paymentId         the payment to refund
     * @param processedByUserId the user issuing the refund
     * @throws ServiceException if the payment is not in COMPLETED state
     */
    void refundPayment(int paymentId, int processedByUserId) throws ServiceException;

    /**
     * Get the total amount paid (COMPLETED payments only) for a reservation.
     *
     * @param reservationId internal reservation ID
     * @return sum of all COMPLETED payments, or BigDecimal.ZERO if none
     * @throws ServiceException on error
     */
    BigDecimal getTotalPaidForReservation(int reservationId) throws ServiceException;

    /**
     * Get the outstanding balance for a reservation
     * (totalAmount - sum of COMPLETED payments).
     *
     * @param reservationNumber the reservation identifier
     * @return outstanding amount, or BigDecimal.ZERO if fully paid
     * @throws ServiceException on error
     */
    BigDecimal getOutstandingBalance(String reservationNumber) throws ServiceException;

    /**
     * Check whether a reservation has been fully paid.
     *
     * @param reservationId internal reservation ID
     * @return true if a COMPLETED payment exists that covers the full amount
     * @throws ServiceException on error
     */
    boolean isFullyPaid(int reservationId) throws ServiceException;
}
