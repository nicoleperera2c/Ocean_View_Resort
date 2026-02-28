<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Room, java.util.List" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Manage Rooms - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                    <h2>Manage Rooms</h2>
                    <a href="<%=request.getContextPath()%>/app/rooms/add" class="btn btn-primary">+ Add New Room</a>
                </div>

                <%-- Alerts from redirects --%>
                    <% if (request.getParameter("success") !=null) { %>
                        <div class="alert alert-success">
                            <%= request.getParameter("success") %>
                        </div>
                        <% } %>
                            <% if (request.getParameter("error") !=null) { %>
                                <div class="alert alert-error">
                                    <%= request.getParameter("error") %>
                                </div>
                                <% } %>
                                    <% if (request.getAttribute("error") !=null) { %>
                                        <div class="alert alert-error">
                                            <%= request.getAttribute("error") %>
                                        </div>
                                        <% } %>

                                            <% @SuppressWarnings("unchecked") List<Room> rooms = (List<Room>)
                                                    request.getAttribute("rooms");
                                                    %>

                                                    <% if (rooms !=null && !rooms.isEmpty()) { %>
                                                        <table>
                                                            <thead>
                                                                <tr>
                                                                    <th>Room #</th>
                                                                    <th>Type</th>
                                                                    <th>Floor</th>
                                                                    <th>Price/Night (Rs.)</th>
                                                                    <th>Capacity</th>
                                                                    <th>Status</th>
                                                                    <th>Actions</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <% for (Room r : rooms) { %>
                                                                    <tr>
                                                                        <td><strong>
                                                                                <%= r.getRoomNumber() %>
                                                                            </strong></td>
                                                                        <td>
                                                                            <%= r.getRoomType() !=null ?
                                                                                r.getRoomType().getTypeName() : "N/A" %>
                                                                        </td>
                                                                        <td>
                                                                            <%= r.getFloorNumber() %>
                                                                        </td>
                                                                        <td>
                                                                            <%= r.getRoomType() !=null ?
                                                                                r.getRoomType().getBasePrice() : "-" %>
                                                                        </td>
                                                                        <td>
                                                                            <%= r.getRoomType() !=null ?
                                                                                r.getRoomType().getMaxOccupancy() : "-"
                                                                                %>
                                                                        </td>
                                                                        <td>
                                                                            <span
                                                                                class="badge badge-<%= r.getStatus().name().toLowerCase() %>">
                                                                                <%= r.getStatus() %>
                                                                            </span>
                                                                        </td>
                                                                        <td>
                                                                            <a href="<%=request.getContextPath()%>/app/rooms/edit?id=<%= r.getRoomId() %>"
                                                                                class="btn btn-sm">Edit</a>
                                                                            <form method="post"
                                                                                action="<%=request.getContextPath()%>/app/rooms"
                                                                                style="display:inline;"
                                                                                onsubmit="return confirm('Are you sure you want to delete Room <%= r.getRoomNumber() %>?');">
                                                                                <input type="hidden" name="action"
                                                                                    value="delete">
                                                                                <input type="hidden" name="roomId"
                                                                                    value="<%= r.getRoomId() %>">
                                                                                <button type="submit"
                                                                                    class="btn btn-danger btn-sm">Delete</button>
                                                                            </form>
                                                                        </td>
                                                                    </tr>
                                                                    <% } %>
                                                            </tbody>
                                                        </table>
                                                        <p style="color:#666; margin-top:0.5rem;">Total: <%=
                                                                rooms.size() %> room(s)</p>
                                                        <% } else { %>
                                                            <div class="card">
                                                                <p>No rooms found. Click <strong>+ Add New Room</strong>
                                                                    to create one.</p>
                                                            </div>
                                                            <% } %>
            </main>
        </body>

        </html>