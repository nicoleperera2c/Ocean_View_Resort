package com.oceanview.controller;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IPaymentDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Payment;
import com.oceanview.model.Reservation;
import com.oceanview.model.User;

import com.oceanview.service.impl.ReservationServiceImpl;
import com.oceanview.service.interfaces.IReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * PaymentServlet - handles payment processing for reservations
 */
@WebServlet(name = "PaymentServlet", urlPatterns = { "/app/payment" })
public class PaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IReservationService reservationService;
    private IPaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        reservationService = new ReservationServiceImpl();
        paymentDAO = DAOFactory.getInstance().getPaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationNumber = request.getParameter("reservationNumber");

        if (reservationNumber != null && !reservationNumber.trim().isEmpty()) {
            try {
                Reservation reservation = reservationService.getReservationDetails(reservationNumber);
                request.setAttribute("reservation", reservation);

                // Get existing payments for this reservation
                List<Payment> payments = paymentDAO.findByReservationId(reservation.getReservationId());
                request.setAttribute("payments", payments);

            } catch (ServiceException | DAOException e) {
                request.setAttribute("error", e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("process".equals(action)) {
            processPayment(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/payment");
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationNumber = request.getParameter("reservationNumber");
        String amountStr = request.getParameter("amount");
        String paymentMethodStr = request.getParameter("paymentMethod");
        String notes = request.getParameter("notes");

        try {
            Reservation reservation = reservationService.getReservationDetails(reservationNumber);

            User user = (User) request.getSession().getAttribute("user");
            int userId = user != null ? user.getUserId() : 1; // Fallback to 1 (admin) if session is missing somehow

            Payment payment = new Payment();
            payment.setReservationId(reservation.getReservationId());
            payment.setAmount(new BigDecimal(amountStr));
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethodStr));
            payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
            payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            payment.setProcessedBy(userId);
            payment.setNotes(notes);

            paymentDAO.createPayment(payment);

            request.setAttribute("success",
                    "Payment processed successfully. Transaction Reference: " + payment.getTransactionReference());
            request.setAttribute("reservation", reservation);

            List<Payment> payments = paymentDAO.findByReservationId(reservation.getReservationId());
            request.setAttribute("payments", payments);

        } catch (Exception e) {
            request.setAttribute("error", "Payment failed: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(request, response);
    }
}
