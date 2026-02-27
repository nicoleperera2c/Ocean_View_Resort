package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IPaymentDAO;
import com.oceanview.dao.interfaces.IReservationDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Payment;
import com.oceanview.model.Reservation;
import com.oceanview.service.interfaces.IPaymentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PaymentServiceImpl - Industrial-grade payment processing engine.
 *
 * This is the ONLY class that may create or modify payment records.
 * All business rules are enforced here before any DB interaction occurs.
 *
 * Enforced Rules:
 * 1. Reservation must be CONFIRMED or CHECKED_IN to accept payment.
 * 2. A COMPLETED payment already exists → duplicate payment is rejected.
 * 3. Payment amount must match the outstanding balance exactly (±0.01
 * tolerance).
 * 4. Amount must be positive (> 0).
 * 5. Refund only allowed on a COMPLETED payment.
 * 6. Every payment gets a cryptographically unique transaction reference.
 */
public class PaymentServiceImpl implements IPaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentServiceImpl.class.getName());

    // Tolerance for floating-point rounding when comparing amounts (Rs. 0.01)
    private static final BigDecimal AMOUNT_TOLERANCE = new BigDecimal("0.01");

    private final IPaymentDAO paymentDAO;
    private final IReservationDAO reservationDAO;

    public PaymentServiceImpl() {
        DAOFactory factory = DAOFactory.getInstance();
        this.paymentDAO = factory.getPaymentDAO();
        this.reservationDAO = factory.getReservationDAO();
    }

    // -------------------------------------------------------------------------
    // Core Payment Processing
    // -------------------------------------------------------------------------

    @Override
    public Payment processPayment(String reservationNumber, BigDecimal amount,
            Payment.PaymentMethod method, String notes, int processedByUserId)
            throws ServiceException {

        // --- 1. Input validation ---
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new ServiceException("Reservation number is required.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Payment amount must be greater than zero.");
        }
        if (method == null) {
            throw new ServiceException("Payment method is required.");
        }

        try {
            // --- 2. Fetch and validate reservation ---
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber.trim());
            if (reservation == null) {
                throw new ServiceException("Reservation not found: " + reservationNumber);
            }

            // --- 3. Status gate: only CONFIRMED or CHECKED_IN reservations can be paid ---
            Reservation.ReservationStatus status = reservation.getStatus();
            if (status != Reservation.ReservationStatus.CONFIRMED &&
                    status != Reservation.ReservationStatus.CHECKED_IN) {
                throw new ServiceException(
                        "Payment cannot be processed. Reservation status is '" + status.name() +
                                "'. Only CONFIRMED or CHECKED_IN reservations can be paid.");
            }

            // --- 4. Duplicate payment guard ---
            if (paymentDAO.hasCompletedPayment(reservation.getReservationId())) {
                BigDecimal alreadyPaid = paymentDAO.getTotalPaidAmount(reservation.getReservationId());
                throw new ServiceException(
                        "Payment already completed for this reservation. " +
                                "Total paid: Rs. " + alreadyPaid.toPlainString() +
                                ". If a refund is required, please use the Refund function.");
            }

            // --- 5. Amount validation: must match outstanding balance (within tolerance)
            // ---
            BigDecimal outstandingBalance = computeOutstandingBalance(reservation);
            if (outstandingBalance.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException(
                        "This reservation has no outstanding balance. It may have already been paid.");
            }
            BigDecimal difference = amount.subtract(outstandingBalance).abs();
            if (difference.compareTo(AMOUNT_TOLERANCE) > 0) {
                throw new ServiceException(
                        "Payment amount Rs. " + amount.toPlainString() +
                                " does not match the outstanding balance of Rs. " + outstandingBalance.toPlainString() +
                                ". Please enter the exact amount due.");
            }

            // --- 6. Build the payment record ---
            Payment payment = new Payment();
            payment.setReservationId(reservation.getReservationId());
            payment.setAmount(outstandingBalance); // Always use server-computed amount, never form amount
            payment.setPaymentMethod(method);
            payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
            payment.setTransactionReference(generateTransactionReference());
            payment.setProcessedBy(processedByUserId);
            payment.setNotes(notes != null ? notes.trim() : null);

            // --- 7. Persist ---
            int paymentId = paymentDAO.createPayment(payment);
            payment.setPaymentId(paymentId);

            LOGGER.log(Level.INFO,
                    "Payment processed: reservationId={0}, amount={1}, txnRef={2}, processedBy={3}",
                    new Object[] { reservation.getReservationId(), outstandingBalance,
                            payment.getTransactionReference(), processedByUserId });

            return payment;

        } catch (DAOException e) {
            LOGGER.log(Level.SEVERE, "DAO error during payment processing: " + e.getMessage(), e);
            throw new ServiceException("Payment processing failed due to a database error. Please try again.", e);
        }
    }

    // -------------------------------------------------------------------------
    // Query Methods
    // -------------------------------------------------------------------------

    @Override
    public List<Payment> getPaymentsForReservation(String reservationNumber) throws ServiceException {
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new ServiceException("Reservation number is required.");
        }
        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber.trim());
            if (reservation == null) {
                throw new ServiceException("Reservation not found: " + reservationNumber);
            }
            return paymentDAO.findByReservationId(reservation.getReservationId());
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving payment history: " + e.getMessage(), e);
        }
    }

    @Override
    public Payment getPaymentById(int paymentId) throws ServiceException {
        if (paymentId <= 0) {
            throw new ServiceException("Valid payment ID is required.");
        }
        try {
            Payment payment = paymentDAO.findById(paymentId);
            if (payment == null) {
                throw new ServiceException("Payment record not found for ID: " + paymentId);
            }
            return payment;
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving payment: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Refund
    // -------------------------------------------------------------------------

    @Override
    public void refundPayment(int paymentId, int processedByUserId) throws ServiceException {
        if (paymentId <= 0) {
            throw new ServiceException("Valid payment ID is required for refund.");
        }
        try {
            Payment payment = paymentDAO.findById(paymentId);
            if (payment == null) {
                throw new ServiceException("Payment not found: ID " + paymentId);
            }
            if (payment.getPaymentStatus() != Payment.PaymentStatus.COMPLETED) {
                throw new ServiceException(
                        "Only COMPLETED payments can be refunded. Current status: " +
                                payment.getPaymentStatus().name());
            }

            boolean updated = paymentDAO.updatePaymentStatus(paymentId, Payment.PaymentStatus.REFUNDED);
            if (!updated) {
                throw new ServiceException("Refund update failed. No rows were modified.");
            }

            LOGGER.log(Level.INFO,
                    "Refund issued: paymentId={0}, amount={1}, processedBy={2}",
                    new Object[] { paymentId, payment.getAmount(), processedByUserId });

        } catch (DAOException e) {
            throw new ServiceException("Refund processing failed: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Balance Utilities
    // -------------------------------------------------------------------------

    @Override
    public BigDecimal getTotalPaidForReservation(int reservationId) throws ServiceException {
        if (reservationId <= 0) {
            throw new ServiceException("Valid reservation ID is required.");
        }
        try {
            return paymentDAO.getTotalPaidAmount(reservationId);
        } catch (DAOException e) {
            throw new ServiceException("Error computing total paid: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getOutstandingBalance(String reservationNumber) throws ServiceException {
        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new ServiceException("Reservation number is required.");
        }
        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber.trim());
            if (reservation == null) {
                throw new ServiceException("Reservation not found: " + reservationNumber);
            }
            return computeOutstandingBalance(reservation);
        } catch (DAOException e) {
            throw new ServiceException("Error computing outstanding balance: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isFullyPaid(int reservationId) throws ServiceException {
        if (reservationId <= 0) {
            throw new ServiceException("Valid reservation ID is required.");
        }
        try {
            return paymentDAO.hasCompletedPayment(reservationId);
        } catch (DAOException e) {
            throw new ServiceException("Error checking payment status: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /**
     * Compute outstanding balance = totalAmount - totalPaid.
     * Clamps to ZERO if overpaid (should not happen due to rule enforcement).
     */
    private BigDecimal computeOutstandingBalance(Reservation reservation) throws DAOException {
        BigDecimal totalAmount = reservation.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        BigDecimal totalPaid = paymentDAO.getTotalPaidAmount(reservation.getReservationId());
        BigDecimal balance = totalAmount.subtract(totalPaid);
        return balance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance;
    }

    /**
     * Generate a unique, URL-safe transaction reference.
     * Format: OVR-XXXXXXXX (OceanView Resort prefix + 8 uppercase hex chars).
     */
    private String generateTransactionReference() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return "OVR-" + uuid.substring(0, 8);
    }
}
