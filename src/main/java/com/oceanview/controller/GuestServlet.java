package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Guest;
import com.oceanview.service.impl.GuestServiceImpl;
import com.oceanview.service.interfaces.IGuestService;
import com.oceanview.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Complete guest management controller
 */
@WebServlet("/app/guest/*")
public class GuestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IGuestService guestService;

    @Override
    public void init() {
        guestService = new GuestServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listGuests(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("/view")) {
            viewGuest(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/search")) {
            searchGuests(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo.equals("/create")) {
            createGuest(request, response);
        } else if (pathInfo.equals("/update")) {
            updateGuest(request, response);
        } else if (pathInfo.equals("/search")) {
            searchGuests(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * List all guests
     */
    private void listGuests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Guest> guests = guestService.getAllGuests();
            request.setAttribute("guests", guests);
            request.getRequestDispatcher("/WEB-INF/views/guest-list.jsp")
                    .forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Show create guest form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp")
                .forward(request, response);
    }

    /**
     * Create new guest
     */
    private void createGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String firstName = ValidationUtil.sanitizeInput(request.getParameter("firstName"));
            String lastName = ValidationUtil.sanitizeInput(request.getParameter("lastName"));
            String address = ValidationUtil.sanitizeInput(request.getParameter("address"));
            String city = ValidationUtil.sanitizeInput(request.getParameter("city"));
            String country = ValidationUtil.sanitizeInput(request.getParameter("country"));
            String phone = ValidationUtil.sanitizeInput(request.getParameter("phone"));
            String email = ValidationUtil.sanitizeInput(request.getParameter("email"));
            String idType = ValidationUtil.sanitizeInput(request.getParameter("idType"));
            String idNumber = ValidationUtil.sanitizeInput(request.getParameter("idNumber"));

            Guest guest = new Guest();
            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setAddress(address);
            guest.setCity(city);
            guest.setCountry(country);
            guest.setPhone(phone);
            guest.setEmail(email);
            guest.setIdType(idType);
            guest.setIdNumber(idNumber);

            Guest created = guestService.createGuest(guest);

            HttpSession session = request.getSession();
            session.setAttribute("success", "Guest registered successfully!");
            response.sendRedirect(request.getContextPath() +
                    "/app/guest/view?id=" + created.getGuestId());

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            showCreateForm(request, response);
        }
    }

    /**
     * View guest details
     */
    private void viewGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Guest guest = guestService.getGuestById(Integer.parseInt(request.getParameter("id")));

            request.setAttribute("guest", guest);
            request.getRequestDispatcher("/WEB-INF/views/guest-details.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid guest ID");
            listGuests(request, response);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            listGuests(request, response);
        }
    }

    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Guest guest = guestService.getGuestById(Integer.parseInt(request.getParameter("id")));

            request.setAttribute("guest", guest);
            request.setAttribute("editMode", true);
            request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Failed to load guest: " + e.getMessage());
            listGuests(request, response);
        }
    }

    /**
     * Update guest
     */
    private void updateGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String guestIdParam = request.getParameter("guestId");

            Guest guest = guestService.getGuestById(Integer.parseInt(guestIdParam));
            guest.setFirstName(ValidationUtil.sanitizeInput(request.getParameter("firstName")));
            guest.setLastName(ValidationUtil.sanitizeInput(request.getParameter("lastName")));
            guest.setAddress(ValidationUtil.sanitizeInput(request.getParameter("address")));
            guest.setCity(ValidationUtil.sanitizeInput(request.getParameter("city")));
            guest.setCountry(ValidationUtil.sanitizeInput(request.getParameter("country")));
            guest.setPhone(ValidationUtil.sanitizeInput(request.getParameter("phone")));
            guest.setEmail(ValidationUtil.sanitizeInput(request.getParameter("email")));
            guest.setIdType(ValidationUtil.sanitizeInput(request.getParameter("idType")));
            guest.setIdNumber(ValidationUtil.sanitizeInput(request.getParameter("idNumber")));

            guestService.updateGuest(guest);

            HttpSession session = request.getSession();
            session.setAttribute("success", "Guest updated successfully!");
            response.sendRedirect(request.getContextPath() +
                    "/app/guest/view?id=" + guestIdParam);

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    /**
     * Search guests
     */
    private void searchGuests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");

            List<Guest> guests = guestService.searchGuests(keyword);

            request.setAttribute("guests", guests);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/WEB-INF/views/guest-list.jsp")
                    .forward(request, response);

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            listGuests(request, response);
        }
    }
}
