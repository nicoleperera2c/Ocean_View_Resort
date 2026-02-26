package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.service.impl.GuestServiceImpl;
import com.oceanview.service.impl.ReservationServiceImpl;
import com.oceanview.service.interfaces.IGuestService;
import com.oceanview.service.interfaces.IReservationService;
import com.oceanview.util.DateUtil;
import com.oceanview.util.ValidationUtil;
import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IRoomDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Complete reservation controller handling all reservation operations
 */
@WebServlet("/app/reservation/*")
public class ReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IReservationService reservationService;
    private IGuestService guestService;
    private IRoomDAO roomDAO;

    @Override
    public void init() {
        reservationService = new ReservationServiceImpl();
        guestService = new GuestServiceImpl();
        roomDAO = DAOFactory.getInstance().getRoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listReservations(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("/view")) {
            viewReservation(request, response);
        } else if (pathInfo.equals("/checkin")) {
            showCheckInForm(request, response);
        } else if (pathInfo.equals("/checkout")) {
            showCheckOutForm(request, response);
        } else if (pathInfo.equals("/cancel")) {
            showCancelForm(request, response);
        } else if (pathInfo.equals("/bill")) {
            showBill(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/app/reservation");
        } else if (pathInfo.equals("/create")) {
            createReservation(request, response);
        } else if (pathInfo.equals("/checkin")) {
            checkInGuest(request, response);
        } else if (pathInfo.equals("/checkout")) {
            checkOutGuest(request, response);
        } else if (pathInfo.equals("/cancel")) {
            cancelReservation(request, response);
        } else if (pathInfo.equals("/search-available")) {
            searchAvailableRooms(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * List all reservations
     */
    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            request.setAttribute("reservations", reservations);
            request.getRequestDispatcher("/WEB-INF/views/reservation-list.jsp")
                    .forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Show create reservation form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Guest> guests = guestService.getAllGuests();
            List<Room> rooms = roomDAO.findAll();

            request.setAttribute("guests", guests);
            request.setAttribute("rooms", rooms);
            request.getRequestDispatcher("/WEB-INF/views/reservation-form.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Failed to load form: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Create new reservation
     */
    private void createReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String checkInStr = request.getParameter("checkInDate");
            String checkOutStr = request.getParameter("checkOutDate");
            int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));
            String specialRequests = request.getParameter("specialRequests");

            LocalDate checkInDate = DateUtil.parseDate(checkInStr);
            LocalDate checkOutDate = DateUtil.parseDate(checkOutStr);

            if (checkInDate == null || checkOutDate == null) {
                throw new ServiceException("Invalid date format");
            }

            Reservation reservation = new Reservation();
            reservation.setGuestId(Integer.parseInt(request.getParameter("guestId")));
            reservation.setRoomId(roomId);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setNumberOfGuests(numberOfGuests);
            reservation.setSpecialRequests(specialRequests);
            reservation.setCreatedBy(user.getUserId());

            Reservation created = reservationService.createReservation(reservation);

            session.setAttribute("success", "Reservation created successfully! " +
                    "Reservation Number: " + created.getReservationNumber());
            response.sendRedirect(request.getContextPath() +
                    "/app/reservation/view?number=" + created.getReservationNumber());

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input format");
            showCreateForm(request, response);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            showCreateForm(request, response);
        }
    }

    /**
     * Search available rooms
     */
    private void searchAvailableRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String checkInStr = request.getParameter("checkInDate");
            String checkOutStr = request.getParameter("checkOutDate");

            LocalDate checkInDate = DateUtil.parseDate(checkInStr);
            LocalDate checkOutDate = DateUtil.parseDate(checkOutStr);

            if (!ValidationUtil.isValidDateRange(checkInDate, checkOutDate)) {
                throw new ServiceException(
                        "Invalid date range: check-in must be today or future, check-out must be after check-in");
            }

            List<Room> availableRooms = reservationService.getAvailableRooms(checkInDate, checkOutDate);

            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("checkInDate", checkInDate);
            request.setAttribute("checkOutDate", checkOutDate);

            List<Guest> guests = guestService.getAllGuests();
            request.setAttribute("guests", guests);

            request.getRequestDispatcher("/WEB-INF/views/reservation-form.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Failed to search rooms: " + e.getMessage());
            showCreateForm(request, response);
        }
    }

    /**
     * View reservation details
     */
    private void viewReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reservationNumber = request.getParameter("number");

            if (ValidationUtil.isNullOrEmpty(reservationNumber)) {
                throw new ServiceException("Reservation number is required");
            }

            Reservation reservation = reservationService.getReservationDetails(
                    reservationNumber);

            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/reservation-details.jsp")
                    .forward(request, response);

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Show check-in form
     */
    private void showCheckInForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/checkin-form.jsp")
                .forward(request, response);
    }

    /**
     * Check in guest
     */
    private void checkInGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reservationNumber = request.getParameter("reservationNumber");

            boolean success = reservationService.checkInGuest(reservationNumber);

            if (success) {
                request.getSession().setAttribute("success", "Guest checked in successfully!");
                response.sendRedirect(request.getContextPath() + "/app/reservation/view?number=" + reservationNumber);
            } else {
                request.setAttribute("error", "Failed to check in guest");
                showCheckInForm(request, response);
            }

        } catch (ServiceException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showCheckInForm(request, response);
        }
    }

    /**
     * Show check-out form
     */
    private void showCheckOutForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/checkout-form.jsp")
                .forward(request, response);
    }

    /**
     * Check out guest
     */
    private void checkOutGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reservationNumber = request.getParameter("reservationNumber");

            boolean success = reservationService.checkOutGuest(reservationNumber);

            if (success) {
                request.getSession().setAttribute("success",
                        "Guest checked out successfully! Please generate the final bill.");
                response.sendRedirect(request.getContextPath() + "/app/reservation/view?number=" + reservationNumber);
            } else {
                request.setAttribute("error", "Failed to check out guest");
                showCheckOutForm(request, response);
            }

        } catch (ServiceException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showCheckOutForm(request, response);
        }
    }

    /**
     * Show cancellation form
     */
    private void showCancelForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reservationNumber = request.getParameter("number");
        request.setAttribute("reservationNumber", reservationNumber);
        request.getRequestDispatcher("/WEB-INF/views/cancel-form.jsp")
                .forward(request, response);
    }

    /**
     * Cancel reservation
     */
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reservationNumber = request.getParameter("reservationNumber");
            String confirmCancel = request.getParameter("confirmCancel");

            if (!"yes".equals(confirmCancel)) {
                request.setAttribute("error", "Please confirm cancellation");
                showCancelForm(request, response);
                return;
            }

            boolean success = reservationService.cancelReservation(reservationNumber);

            if (success) {
                HttpSession session = request.getSession();
                session.setAttribute("success", "Reservation cancelled successfully");
                response.sendRedirect(request.getContextPath() + "/app/reservation");
            } else {
                request.setAttribute("error", "Failed to cancel reservation");
                showCancelForm(request, response);
            }

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            showCancelForm(request, response);
        }
    }

    /**
     * Show bill
     */
    private void showBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String reservationNumber = request.getParameter("number");

            Reservation reservation = reservationService.getReservationDetails(
                    reservationNumber);

            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/bill.jsp")
                    .forward(request, response);

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }
}
