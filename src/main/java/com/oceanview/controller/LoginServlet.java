package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.impl.AuthenticationServiceImpl;
import com.oceanview.service.interfaces.IAuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet - handles user authentication
 * GET: displays login page
 * POST: authenticates credentials and creates session
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IAuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthenticationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/app/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = authService.authenticate(username, password);

            // Create session with user info
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("role", user.getRole().name());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes timeout

            // Create remember-me cookie if requested
            String remember = request.getParameter("remember");
            if ("on".equals(remember)) {
                Cookie userCookie = new Cookie("oceanview_user", username);
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                userCookie.setPath(request.getContextPath());
                response.addCookie(userCookie);
            }

            response.sendRedirect(request.getContextPath() + "/app/dashboard");

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
