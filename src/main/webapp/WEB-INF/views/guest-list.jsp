<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List, com.oceanview.model.Guest" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Guests - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>Guests</h2>
                <a href="<%=request.getContextPath()%>/app/guest/create" class="btn">+ Register Guest</a>
                <form method="post" action="<%=request.getContextPath()%>/app/guest/search" class="search-form">
                    <input type="text" name="keyword" placeholder="Search by name, phone, email..."
                        value="<%= request.getAttribute(" keyword") !=null ? request.getAttribute("keyword") : "" %>">
                    <button type="submit" class="btn">Search</button>
                </form>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <% @SuppressWarnings("unchecked") List<Guest> guests = (List<Guest>)
                                request.getAttribute("guests");
                                %>
                                <% if (guests !=null && !guests.isEmpty()) { %>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Phone</th>
                                                <th>Email</th>
                                                <th>City</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (Guest g : guests) { %>
                                                <tr>
                                                    <td>
                                                        <%= g.getFullName() %>
                                                    </td>
                                                    <td>
                                                        <%= g.getPhone() %>
                                                    </td>
                                                    <td>
                                                        <%= g.getEmail() !=null ? g.getEmail() : "" %>
                                                    </td>
                                                    <td>
                                                        <%= g.getCity() !=null ? g.getCity() : "" %>
                                                    </td>
                                                    <td>
                                                        <a href="<%=request.getContextPath()%>/app/guest/view?id=<%= g.getGuestId() %>"
                                                            class="btn btn-sm">View</a>
                                                        <a href="<%=request.getContextPath()%>/app/guest/edit?id=<%= g.getGuestId() %>"
                                                            class="btn btn-sm">Edit</a>
                                                    </td>
                                                </tr>
                                                <% } %>
                                        </tbody>
                                    </table>
                                    <% } else { %>
                                        <p>No guests found.</p>
                                        <% } %>
            </main>
        </body>

        </html>