package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.service.impl.ReportServiceImpl;
import com.oceanview.service.interfaces.IReportService;
import com.oceanview.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * ReportServlet - generates occupancy, revenue, and reservation reports
 */
@WebServlet(name = "ReportServlet", urlPatterns = { "/app/reports" })
public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private IReportService reportService;

    @Override
    public void init() throws ServletException {
        reportService = new ReportServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reportType = request.getParameter("type");

        if (reportType != null) {
            generateReport(request, response, reportType);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reportType = request.getParameter("type");
        generateReport(request, response, reportType);
    }

    private void generateReport(HttpServletRequest request, HttpServletResponse response,
            String reportType) throws ServletException, IOException {
        try {
            Map<String, Object> report = null;

            switch (reportType != null ? reportType : "") {
                case "occupancy":
                    report = reportService.getOccupancyReport(LocalDate.now());
                    request.setAttribute("reportTitle", "Occupancy Report");
                    break;

                case "reservation":
                    LocalDate startDate = DateUtil.parseDate(request.getParameter("startDate"));
                    LocalDate endDate = DateUtil.parseDate(request.getParameter("endDate"));
                    if (startDate == null)
                        startDate = LocalDate.now().minusMonths(1);
                    if (endDate == null)
                        endDate = LocalDate.now();
                    report = reportService.getReservationReport(startDate, endDate);
                    request.setAttribute("reportTitle", "Reservation Report");
                    break;

                case "revenue":
                    LocalDate revStart = DateUtil.parseDate(request.getParameter("startDate"));
                    LocalDate revEnd = DateUtil.parseDate(request.getParameter("endDate"));
                    if (revStart == null)
                        revStart = LocalDate.now().minusMonths(1);
                    if (revEnd == null)
                        revEnd = LocalDate.now();
                    report = reportService.getRevenueReport(revStart, revEnd);
                    request.setAttribute("reportTitle", "Revenue Report");
                    break;

                default:
                    request.setAttribute("error", "Invalid report type");
                    break;
            }

            if (report != null) {
                request.setAttribute("report", report);
                request.setAttribute("reportType", reportType);
            }

        } catch (ServiceException e) {
            request.setAttribute("error", "Error generating report: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);
    }
}
