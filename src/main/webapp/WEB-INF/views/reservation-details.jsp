<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Reservation" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Reservation Details - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <% if (request.getAttribute("success") !=null) { %>
                    <div class="alert alert-success">
                        <%= request.getAttribute("success") %>
                    </div>
                    <% } %>
                        <% Reservation reservation=(Reservation) request.getAttribute("reservation"); %>
                            <% if (reservation !=null) { %>
                                <h2>Reservation: <%= reservation.getReservationNumber() %>
                                </h2>
                                <div class="details-grid">
                                    <div class="detail-item"><strong>Status:</strong>
                                        <span class="status status-<%= reservation.getStatus().name().toLowerCase() %>">
                                            <%= reservation.getStatus() %>
                                        </span>
                                    </div>
                                    <div class="detail-item"><strong>Guest:</strong>
                                        <%= reservation.getGuest() !=null ? reservation.getGuest().getFullName() : "N/A"
                                            %>
                                    </div>
                                    <div class="detail-item"><strong>Room:</strong>
                                        <%= reservation.getRoom() !=null ? reservation.getRoom().getRoomNumber() : "N/A"
                                            %>
                                    </div>
                                    <div class="detail-item"><strong>Room Type:</strong>
                                        <%= reservation.getRoom() !=null && reservation.getRoom().getRoomType() !=null ?
                                            reservation.getRoom().getRoomType().getTypeName() : "N/A" %>
                                    </div>
                                    <div class="detail-item"><strong>Check-In:</strong>
                                        <%= reservation.getCheckInDate() %>
                                    </div>
                                    <div class="detail-item"><strong>Check-Out:</strong>
                                        <%= reservation.getCheckOutDate() %>
                                    </div>
                                    <div class="detail-item"><strong>Nights:</strong>
                                        <%= reservation.calculateNights() %>
                                    </div>
                                    <div class="detail-item"><strong>Guests:</strong>
                                        <%= reservation.getNumberOfGuests() %>
                                    </div>
                                    <div class="detail-item"><strong>Total:</strong> Rs. <%=
                                            reservation.getTotalAmount() %>
                                    </div>
                                    <div class="detail-item"><strong>Special Requests:</strong>
                                        <%= reservation.getSpecialRequests() !=null ? reservation.getSpecialRequests()
                                            : "None" %>
                                    </div>
                                </div>
                                <div class="action-buttons">
                                    <% if (reservation.canCheckIn()) { %>
                                        <form method="post"
                                            action="<%=request.getContextPath()%>/app/reservation/checkin"
                                            style="display:inline;">
                                            <input type="hidden" name="reservationNumber"
                                                value="<%= reservation.getReservationNumber() %>">
                                            <input type="hidden" name="number"
                                                value="<%= reservation.getReservationNumber() %>">
                                            <button type="submit" class="btn btn-primary">Check In</button>
                                        </form>
                                        <% } %>
                                            <% if (reservation.canCheckOut()) { %>
                                                <form method="post"
                                                    action="<%=request.getContextPath()%>/app/reservation/checkout"
                                                    style="display:inline;">
                                                    <input type="hidden" name="reservationNumber"
                                                        value="<%= reservation.getReservationNumber() %>">
                                                    <button type="submit" class="btn btn-primary">Check Out</button>
                                                </form>
                                                <% } %>
                                                    <% if (reservation.canBeCancelled()) { %>
                                                        <a href="<%=request.getContextPath()%>/app/reservation/cancel?number=<%= reservation.getReservationNumber() %>"
                                                            class="btn">Cancel</a>
                                                        <% } %>
                                                            <a href="<%=request.getContextPath()%>/app/reservation/bill?number=<%= reservation.getReservationNumber() %>"
                                                                class="btn">View Bill</a>
                                                            <a href="<%=request.getContextPath()%>/app/reservation"
                                                                class="btn">Back to List</a>
                                </div>
                                <% } else { %>
                                    <p>Reservation not found.</p>
                                    <% } %>
            </main>
        </body>

        </html>