<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Room, com.oceanview.model.RoomType, java.util.List" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>
                <%= request.getAttribute("room") !=null ? "Edit" : "Add" %> Room - Ocean View Resort
            </title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <% Room room=(Room) request.getAttribute("room"); boolean isEdit=(room !=null); String formTitle=isEdit
                    ? "Edit Room" : "Add New Room" ; String formAction=isEdit ? "update" : "add" ; %>

                    <h2>
                        <%= formTitle %>
                    </h2>

                    <% if (request.getAttribute("error") !=null) { %>
                        <div class="alert alert-error">
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                            <div class="card">
                                <form method="post" action="<%=request.getContextPath()%>/app/rooms">
                                    <input type="hidden" name="action" value="<%= formAction %>">
                                    <% if (isEdit) { %>
                                        <input type="hidden" name="roomId" value="<%= room.getRoomId() %>">
                                        <% } %>

                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label for="roomNumber">Room Number *</label>
                                                    <input type="text" id="roomNumber" name="roomNumber"
                                                        value="<%= isEdit ? room.getRoomNumber() : "" %>"
                                                        placeholder="e.g. 101, 202, 305" required maxlength="10">
                                                </div>
                                                <div class="form-group">
                                                    <label for="floorNumber">Floor Number *</label>
                                                    <input type="number" id="floorNumber" name="floorNumber"
                                                        value="<%= isEdit ? room.getFloorNumber() : "" %>" min="1"
                                                        max="20" required placeholder="1-20">
                                                </div>
                                            </div>

                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label for="roomTypeId">Room Type *</label>
                                                    <select id="roomTypeId" name="roomTypeId" required>
                                                        <option value="">-- Select Room Type --</option>
                                                        <% @SuppressWarnings("unchecked") List<RoomType> roomTypes =
                                                            (List<RoomType>) request.getAttribute("roomTypes");
                                                                if (roomTypes != null) {
                                                                for (RoomType rt : roomTypes) {
                                                                boolean selected = isEdit && room.getRoomTypeId() ==
                                                                rt.getRoomTypeId();
                                                                %>
                                                                <option value="<%= rt.getRoomTypeId() %>" <%=selected
                                                                    ? "selected" : "" %>>
                                                                    <%= rt.getTypeName() %> — Rs. <%= rt.getBasePrice()
                                                                            %>/night (Max: <%= rt.getMaxOccupancy() %>
                                                                                guests)
                                                                </option>
                                                                <% } } %>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="status">Status *</label>
                                                    <select id="status" name="status" required>
                                                        <% for (Room.RoomStatus s : Room.RoomStatus.values()) { boolean
                                                            selected=isEdit && room.getStatus()==s; %>
                                                            <option value="<%= s.name() %>" <%=selected ? "selected"
                                                                : "" %>>
                                                                <%= s.name() %>
                                                            </option>
                                                            <% } %>
                                                    </select>
                                                </div>
                                            </div>

                                            <div style="margin-top:1rem;">
                                                <button type="submit" class="btn btn-primary">
                                                    <%= isEdit ? "Update Room" : "Add Room" %>
                                                </button>
                                                <a href="<%=request.getContextPath()%>/app/rooms" class="btn">Cancel</a>
                                            </div>
                                </form>
                            </div>
            </main>
        </body>

        </html>