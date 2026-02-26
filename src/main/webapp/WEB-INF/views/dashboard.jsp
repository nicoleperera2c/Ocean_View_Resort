<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.Map" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Dashboard - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>Dashboard</h2>
                <% if (session.getAttribute("success") !=null) { %>
                    <div class="alert alert-success">
                        <%= session.getAttribute("success") %>
                    </div>
                    <% session.removeAttribute("success"); %>
                        <% } %>
                            <% @SuppressWarnings("unchecked") Map<String, Object> stats = (Map<String, Object>)
                                    request.getAttribute("stats");
                                    %>
                                    <% if (stats !=null) { %>
                                        <div class="stats-grid">
                                            <div class="stat-card">
                                                <h3>Total Rooms</h3><span class="stat-value">
                                                    <%= stats.get("totalRooms") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Occupied</h3><span class="stat-value">
                                                    <%= stats.get("occupiedRooms") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Available</h3><span class="stat-value">
                                                    <%= stats.get("availableRooms") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Occupancy Rate</h3><span class="stat-value">
                                                    <%= stats.get("occupancyRate") %>%
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Active Reservations</h3><span class="stat-value">
                                                    <%= stats.get("activeReservations") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Today Check-Ins</h3><span class="stat-value">
                                                    <%= stats.get("todayCheckIns") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Today Check-Outs</h3><span class="stat-value">
                                                    <%= stats.get("todayCheckOuts") %>
                                                </span>
                                            </div>
                                            <div class="stat-card">
                                                <h3>Total Revenue</h3><span class="stat-value">Rs. <%=
                                                        String.format("%.2f", stats.get("totalRevenue") !=null ?
                                                        ((java.lang.Number) stats.get("totalRevenue")).doubleValue() :
                                                        0.0) %>
                                                </span>
                                            </div>
                                        </div>
                                        <% } else { %>
                                            <p>No statistics available.</p>
                                            <% } %>
                                                <div class="quick-actions">
                                                    <h3>Quick Actions</h3>
                                                    <a href="<%=request.getContextPath()%>/app/reservation/create"
                                                        class="btn">New Reservation</a>
                                                    <a href="<%=request.getContextPath()%>/app/guest/create"
                                                        class="btn">Register Guest</a>
                                                    <a href="<%=request.getContextPath()%>/app/reservation/checkin"
                                                        class="btn">Check-In</a>
                                                    <a href="<%=request.getContextPath()%>/app/reservation/checkout"
                                                        class="btn">Check-Out</a>
                                                </div>
            </main>
        </body>

        </html>