<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.Map, java.util.List, com.oceanview.model.Reservation" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Reports - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>Reports</h2>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <div class="report-actions">
                            <a href="<%=request.getContextPath()%>/app/reports?type=occupancy" class="btn">Occupancy
                                Report</a>
                            <form method="post" action="<%=request.getContextPath()%>/app/reports"
                                style="display:inline-block;">
                                <input type="hidden" name="type" value="reservation">
                                <input type="date" name="startDate" required> to <input type="date" name="endDate"
                                    required>
                                <button type="submit" class="btn">Reservation Report</button>
                            </form>
                            <form method="post" action="<%=request.getContextPath()%>/app/reports"
                                style="display:inline-block;">
                                <input type="hidden" name="type" value="revenue">
                                <input type="date" name="startDate" required> to <input type="date" name="endDate"
                                    required>
                                <button type="submit" class="btn">Revenue Report</button>
                            </form>
                        </div>
                        <% @SuppressWarnings("unchecked") Map<String, Object> report = (Map<String, Object>)
                                request.getAttribute("report");
                                String reportType = (String) request.getAttribute("reportType");
                                %>
                                <% if (report !=null) { %>
                                    <div class="card">
                                        <h3>
                                            <%= request.getAttribute("reportTitle") %>
                                        </h3>
                                        <% if ("occupancy".equals(reportType)) { %>
                                            <div class="stats-grid">
                                                <div class="stat-card">
                                                    <h3>Total Rooms</h3><span class="stat-value">
                                                        <%= report.get("totalRooms") %>
                                                    </span>
                                                </div>
                                                <div class="stat-card">
                                                    <h3>Occupied</h3><span class="stat-value">
                                                        <%= report.get("occupiedRooms") %>
                                                    </span>
                                                </div>
                                                <div class="stat-card">
                                                    <h3>Available</h3><span class="stat-value">
                                                        <%= report.get("availableRooms") %>
                                                    </span>
                                                </div>
                                                <div class="stat-card">
                                                    <h3>Maintenance</h3><span class="stat-value">
                                                        <%= report.get("maintenanceRooms") %>
                                                    </span>
                                                </div>
                                                <div class="stat-card">
                                                    <h3>Occupancy Rate</h3><span class="stat-value">
                                                        <%= report.get("occupancyRate") %>%
                                                    </span>
                                                </div>
                                            </div>
                                            <% } else if ("reservation".equals(reportType)) { %>
                                                <div class="stats-grid">
                                                    <div class="stat-card">
                                                        <h3>Total</h3><span class="stat-value">
                                                            <%= report.get("totalReservations") %>
                                                        </span>
                                                    </div>
                                                    <div class="stat-card">
                                                        <h3>Confirmed</h3><span class="stat-value">
                                                            <%= report.get("confirmed") %>
                                                        </span>
                                                    </div>
                                                    <div class="stat-card">
                                                        <h3>Checked In</h3><span class="stat-value">
                                                            <%= report.get("checkedIn") %>
                                                        </span>
                                                    </div>
                                                    <div class="stat-card">
                                                        <h3>Checked Out</h3><span class="stat-value">
                                                            <%= report.get("checkedOut") %>
                                                        </span>
                                                    </div>
                                                    <div class="stat-card">
                                                        <h3>Cancelled</h3><span class="stat-value">
                                                            <%= report.get("cancelled") %>
                                                        </span>
                                                    </div>
                                                </div>
                                                <% } else if ("revenue".equals(reportType)) { double
                                                    totalRevenue=report.get("totalRevenue") !=null ? ((java.lang.Number)
                                                    report.get("totalRevenue")).doubleValue() : 0.0; double
                                                    avgRevenue=report.get("averageRevenue") !=null ? ((java.lang.Number)
                                                    report.get("averageRevenue")).doubleValue() : 0.0; %>
                                                    <div class="stats-grid">
                                                        <div class="stat-card">
                                                            <h3>Total Revenue</h3><span class="stat-value">Rs. <%=
                                                                    String.format("%.2f", totalRevenue) %></span>
                                                        </div>
                                                        <div class="stat-card">
                                                            <h3>Completed</h3><span class="stat-value">
                                                                <%= report.get("completedReservations") %>
                                                            </span>
                                                        </div>
                                                        <div class="stat-card">
                                                            <h3>Average Revenue</h3><span class="stat-value">Rs. <%=
                                                                    String.format("%.2f", avgRevenue) %></span>
                                                        </div>
                                                    </div>
                                                    <% } %>
                                    </div>
                                    <% } %>
            </main>
        </body>

        </html>