<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List, com.oceanview.model.Reservation" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Reservations - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>Reservations</h2>
                <a href="<%=request.getContextPath()%>/app/reservation/create" class="btn">+ New Reservation</a>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <% if (session.getAttribute("success") !=null) { %>
                            <div class="alert alert-success">
                                <%= session.getAttribute("success") %>
                            </div>
                            <% session.removeAttribute("success"); %>
                                <% } %>
                                    <% @SuppressWarnings("unchecked") List<Reservation> reservations = (List
                                        <Reservation>) request.getAttribute("reservations");
                                            %>
                                            <% if (reservations !=null && !reservations.isEmpty()) { %>
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th>Reservation #</th>
                                                            <th>Guest</th>
                                                            <th>Room</th>
                                                            <th>Check-In</th>
                                                            <th>Check-Out</th>
                                                            <th>Status</th>
                                                            <th>Total</th>
                                                            <th>Actions</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <% for (Reservation r : reservations) { %>
                                                            <tr>
                                                                <td>
                                                                    <%= r.getReservationNumber() %>
                                                                </td>
                                                                <td>
                                                                    <%= r.getGuest() !=null ? r.getGuest().getFullName()
                                                                        : "N/A" %>
                                                                </td>
                                                                <td>
                                                                    <%= r.getRoom() !=null ? r.getRoom().getRoomNumber()
                                                                        : "N/A" %>
                                                                </td>
                                                                <td>
                                                                    <%= r.getCheckInDate() %>
                                                                </td>
                                                                <td>
                                                                    <%= r.getCheckOutDate() %>
                                                                </td>
                                                                <td><span
                                                                        class="status status-<%= r.getStatus().name().toLowerCase() %>">
                                                                        <%= r.getStatus() %>
                                                                    </span></td>
                                                                <td>Rs. <%= r.getTotalAmount() %>
                                                                </td>
                                                                <td>
                                                                    <a href="<%=request.getContextPath()%>/app/reservation/view?number=<%= r.getReservationNumber() %>"
                                                                        class="btn btn-sm">View</a>
                                                                    <% if (r.canCheckIn()) { %>
                                                                        <a href="<%=request.getContextPath()%>/app/reservation/checkin?reservationNumber=<%= r.getReservationNumber() %>"
                                                                            class="btn btn-sm">Check In</a>
                                                                        <% } %>
                                                                            <% if (r.canCheckOut()) { %>
                                                                                <a href="<%=request.getContextPath()%>/app/reservation/bill?number=<%= r.getReservationNumber() %>"
                                                                                    class="btn btn-sm">Bill</a>
                                                                                <% } %>
                                                                </td>
                                                            </tr>
                                                            <% } %>
                                                    </tbody>
                                                </table>
                                                <% } else { %>
                                                    <p>No reservations found.</p>
                                                    <% } %>
            </main>
        </body>

        </html>