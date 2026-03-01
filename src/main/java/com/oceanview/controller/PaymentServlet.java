package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Payment;
import com.oceanview.model.Reservation;
import com.oceanview.model.User;
import com.oceanview.service.impl.PaymentServiceImpl;
import com.oceanview.service.impl.ReservationServiceImpl;
import com.oceanview.service.interfaces.IPaymentService;
import com.oceanview.service.interfaces.IReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * PaymentServlet - Payment controller.
 *
 * All business logic is delegated to IPaymentService.
 * This servlet only handles HTTP routing, input parsing and view forwarding.
 *
 * URL: /app/payment
 * GET ?reservationNumber=XXX → payment page with pre-loaded reservation
 * POST action=process → process a payment
 * POST action=refund → issue a refund
 */
@WebServlet(name = "PaymentServlet", urlPatterns = { "/app/payment" })
public class PaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IPaymentService paymentService;
    private IReservationService reservationService;

    @Override
    public void init() throws ServletException {
        paymentService = new PaymentServiceImpl();
        reservationService = new ReservationServiceImpl();
    }

    // GET — display the payment page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reservationNumber = request.getParameter("reservationNumber");

        if (reservationNumber != null && !reservationNumber.trim().isEmpty()) {
            try {
                Reservation reservation = reservationService.getReservationDetails(reservationNumber.trim());
                request.setAttribute("reservation", reservation);

                // Payment history
                List<Payment> payments = paymentService.getPaymentsForReservation(reservationNumber.trim());
                request.setAttribute("payments", payments);

                // Outstanding balance — shown to clerk so they know exact amount
                BigDecimal outstanding = paymentService.getOutstandingBalance(reservationNumber.trim());
                request.setAttribute("outstandingBalance", outstanding);

                // Whether this reservation has already been paid
                boolean fullyPaid = paymentService.isFullyPaid(reservation.getReservationId());
                request.setAttribute("fullyPaid", fullyPaid);

            } catch (ServiceException e) {
                request.setAttribute("error", e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(request, response);
    }

    // POST — process payment or issue refund

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("process".equals(action)) {
            handleProcessPayment(request, response);
        } else if ("refund".equals(action)) {
            handleRefund(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/payment");
        }
    }

    // Payment Processing

    private void handleProcessPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reservationNumber = request.getParameter("reservationNumber");
        String amountStr = request.getParameter("amount");
        String paymentMethodStr = request.getParameter("paymentMethod");
        String notes = request.getParameter("notes");

        // Parse and validate inputs before calling the service
        BigDecimal amount = null;
        Payment.PaymentMethod method = null;

        try {
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new ServiceException("Amount is required.");
            }
            amount = new BigDecimal(amountStr.trim());

            if (paymentMethodStr == null || paymentMethodStr.trim().isEmpty()) {
                throw new ServiceException("Payment method is required.");
            }
            method = Payment.PaymentMethod.valueOf(paymentMethodStr.trim());

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format. Please enter a valid number.");
            reloadPaymentPage(request, response, reservationNumber);
            return;
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid payment method selected.");
            reloadPaymentPage(request, response, reservationNumber);
            return;
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            reloadPaymentPage(request, response, reservationNumber);
            return;
        }

        // Extract the authenticated user from session
        User user = (User) request.getSession().getAttribute("user");
        int userId = (user != null) ? user.getUserId() : 0;

        try {
            // Delegate ALL business logic to the service — no logic in the servlet
            Payment payment = paymentService.processPayment(
                    reservationNumber, amount, method, notes, userId);

            request.setAttribute("success",
                    "Payment of Rs. " + payment.getAmount().toPlainString() +
                            " processed successfully. Transaction Reference: " + payment.getTransactionReference());

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }

        reloadPaymentPage(request, response, reservationNumber);
    }

    // Refund Processing

    private void handleRefund(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentIdStr = request.getParameter("paymentId");
        String reservationNumber = request.getParameter("reservationNumber");

        User user = (User) request.getSession().getAttribute("user");
        int userId = (user != null) ? user.getUserId() : 0;

        try {
            if (paymentIdStr == null || paymentIdStr.trim().isEmpty()) {
                throw new ServiceException("Payment ID is required for a refund.");
            }
            int paymentId = Integer.parseInt(paymentIdStr.trim());
            paymentService.refundPayment(paymentId, userId);

            request.setAttribute("success", "Refund issued successfully for Payment ID: " + paymentId);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid payment ID.");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }

        reloadPaymentPage(request, response, reservationNumber);
    }

    // Shared: reload the payment page with fresh data

    private void reloadPaymentPage(HttpServletRequest request, HttpServletResponse response,
            String reservationNumber) throws ServletException, IOException {

        if (reservationNumber != null && !reservationNumber.trim().isEmpty()) {
            try {
                Reservation reservation = reservationService.getReservationDetails(reservationNumber.trim());
                request.setAttribute("reservation", reservation);

                List<Payment> payments = paymentService.getPaymentsForReservation(reservationNumber.trim());
                request.setAttribute("payments", payments);

                BigDecimal outstanding = paymentService.getOutstandingBalance(reservationNumber.trim());
                request.setAttribute("outstandingBalance", outstanding);

                boolean fullyPaid = paymentService.isFullyPaid(reservation.getReservationId());
                request.setAttribute("fullyPaid", fullyPaid);

            } catch (ServiceException e) {
                request.setAttribute("error", e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(request, response);
    }
}
