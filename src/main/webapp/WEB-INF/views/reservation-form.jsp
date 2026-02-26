<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List, com.oceanview.model.Guest, com.oceanview.model.Room" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>New Reservation - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>New Reservation</h2>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                        <% Object checkInObj=request.getAttribute("checkInDate"); Object
                            checkOutObj=request.getAttribute("checkOutDate"); String checkIn=checkInObj !=null ?
                            checkInObj.toString() : "" ; String checkOut=checkOutObj !=null ? checkOutObj.toString()
                            : "" ; %>

                            <!-- Step 1: Search available rooms -->
                            <div class="card">
                                <h3>Step 1: Search Available Rooms</h3>
                                <form method="post"
                                    action="<%=request.getContextPath()%>/app/reservation/search-available">
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="checkInDate">Check-In Date</label>
                                            <input type="date" id="checkInDate" name="checkInDate"
                                                value="<%= checkIn %>" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="checkOutDate">Check-Out Date</label>
                                            <input type="date" id="checkOutDate" name="checkOutDate"
                                                value="<%= checkOut %>" required>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn">Search Rooms</button>
                                </form>
                            </div>

                            <!-- Step 2: Create Reservation (shown after room search) -->
                            <% @SuppressWarnings("unchecked") List<Room> availableRooms = (List<Room>)
                                    request.getAttribute("availableRooms");
                                    @SuppressWarnings("unchecked")
                                    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
                                            @SuppressWarnings("unchecked")
                                            List<Guest> guests = (List<Guest>) request.getAttribute("guests");
                                                    List<Room> displayRooms = availableRooms != null ? availableRooms :
                                                        rooms;
                                                        %>
                                                        <% if (displayRooms !=null && guests !=null) { %>
                                                            <div class="card">
                                                                <h3>Step 2: Reservation Details</h3>
                                                                <form method="post"
                                                                    action="<%=request.getContextPath()%>/app/reservation/create">
                                                                    <div class="form-group">
                                                                        <label for="guestId">Guest</label>
                                                                        <select id="guestId" name="guestId" required>
                                                                            <option value="">-- Select Guest --</option>
                                                                            <% for (Guest g : guests) { %>
                                                                                <option value="<%= g.getGuestId() %>">
                                                                                    <%= g.getFullName() %> (<%=
                                                                                            g.getPhone() %>)
                                                                                </option>
                                                                                <% } %>
                                                                        </select>
                                                                        <a href="<%=request.getContextPath()%>/app/guest/create"
                                                                            class="link-sm">Register New Guest</a>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label for="roomId">Room</label>
                                                                        <select id="roomId" name="roomId" required>
                                                                            <option value="">-- Select Room --</option>
                                                                            <% for (Room r : displayRooms) { %>
                                                                                <option value="<%= r.getRoomId() %>">
                                                                                    Room <%= r.getRoomNumber() %> - <%=
                                                                                            r.getRoomType() !=null ?
                                                                                            r.getRoomType().getTypeName()
                                                                                            : "" %> (Rs. <%=
                                                                                                r.getRoomType() !=null ?
                                                                                                r.getRoomType().getBasePrice()
                                                                                                : "" %>/night)</option>
                                                                                <% } %>
                                                                        </select>
                                                                    </div>
                                                                    <div class="form-row">
                                                                        <div class="form-group">
                                                                            <label for="resCheckIn">Check-In</label>
                                                                            <input type="date" id="resCheckIn"
                                                                                name="checkInDate"
                                                                                value="<%= checkIn %>" required>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label for="resCheckOut">Check-Out</label>
                                                                            <input type="date" id="resCheckOut"
                                                                                name="checkOutDate"
                                                                                value="<%= checkOut %>" required>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label for="numberOfGuests">Number of
                                                                            Guests</label>
                                                                        <input type="number" id="numberOfGuests"
                                                                            name="numberOfGuests" min="1" max="10"
                                                                            value="1" required>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label for="specialRequests">Special
                                                                            Requests</label>
                                                                        <textarea id="specialRequests"
                                                                            name="specialRequests" rows="3"
                                                                            placeholder="Any special requests..."></textarea>
                                                                    </div>
                                                                    <button type="submit" class="btn btn-primary">Create
                                                                        Reservation</button>
                                                                </form>
                                                            </div>
                                                            <% } %>
            </main>
        </body>

        </html>