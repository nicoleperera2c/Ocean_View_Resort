<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Guest" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Guest Details - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <% if (session.getAttribute("success") !=null) { %>
                    <div class="alert alert-success">
                        <%= session.getAttribute("success") %>
                    </div>
                    <% session.removeAttribute("success"); %>
                        <% } %>
                            <% Guest guest=(Guest) request.getAttribute("guest"); %>
                                <% if (guest !=null) { %>
                                    <h2>Guest: <%= guest.getFullName() %>
                                    </h2>
                                    <div class="details-grid">
                                        <div class="detail-item"><strong>Phone:</strong>
                                            <%= guest.getPhone() %>
                                        </div>
                                        <div class="detail-item"><strong>Email:</strong>
                                            <%= guest.getEmail() !=null ? guest.getEmail() : "N/A" %>
                                        </div>
                                        <div class="detail-item"><strong>Address:</strong>
                                            <%= guest.getAddress() !=null ? guest.getAddress() : "N/A" %>
                                        </div>
                                        <div class="detail-item"><strong>City:</strong>
                                            <%= guest.getCity() !=null ? guest.getCity() : "N/A" %>
                                        </div>
                                        <div class="detail-item"><strong>Country:</strong>
                                            <%= guest.getCountry() !=null ? guest.getCountry() : "N/A" %>
                                        </div>
                                        <div class="detail-item"><strong>ID Type:</strong>
                                            <%= guest.getIdType() !=null ? guest.getIdType() : "N/A" %>
                                        </div>
                                        <div class="detail-item"><strong>ID Number:</strong>
                                            <%= guest.getIdNumber() !=null ? guest.getIdNumber() : "N/A" %>
                                        </div>
                                    </div>
                                    <div class="action-buttons">
                                        <a href="<%=request.getContextPath()%>/app/guest/edit?id=<%= guest.getGuestId() %>"
                                            class="btn">Edit</a>
                                        <a href="<%=request.getContextPath()%>/app/guest" class="btn">Back to List</a>
                                    </div>
                                    <% } else { %>
                                        <p>Guest not found.</p>
                                        <% } %>
            </main>
        </body>

        </html>