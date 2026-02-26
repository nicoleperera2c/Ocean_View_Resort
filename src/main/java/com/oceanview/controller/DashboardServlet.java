package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.impl.ReportServiceImpl;
import com.oceanview.service.interfaces.IReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Dashboard controller - displays main statistics
 */
@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IReportService reportService;

    @Override
    public void init() {
        reportService = new ReportServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Map<String, Object> stats = reportService.getDashboardStatistics();
            request.setAttribute("stats", stats);

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
                    .forward(request, response);

        } catch (ServiceException e) {
            request.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
