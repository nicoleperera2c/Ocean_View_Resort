<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List" %>
        <%@ page import="com.oceanview.model.User" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Manage Users - Ocean View Resort</title>
                <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
            </head>

            <body>
                <jsp:include page="header.jsp" />
                <main class="container">
                    <div class="header-actions">
                        <h2>User Management</h2>
                        <a href="<%=request.getContextPath()%>/app/users/add" class="btn btn-primary">Add New User</a>
                    </div>

                    <% String successMsg=request.getParameter("success"); if (successMsg !=null) { %>
                        <div class="alert alert-success">
                            <%= successMsg %>
                        </div>
                        <% } %>

                            <% String errorMsg=request.getParameter("error"); if (errorMsg !=null) { %>
                                <div class="alert alert-danger">
                                    <%= errorMsg %>
                                </div>
                                <% } %>

                                    <div class="card">
                                        <table class="data-table">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Username</th>
                                                    <th>Full Name</th>
                                                    <th>Role</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% List<User> users = (List<User>) request.getAttribute("users");
                                                        if (users != null && !users.isEmpty()) {
                                                        User loggedInUser = (User) session.getAttribute("user");
                                                        for (User u : users) {
                                                        String roleBadge = u.getRole().name().toLowerCase();
                                                        String statusClass = u.isActive() ? "badge-success" :
                                                        "badge-danger";
                                                        String statusLabel = u.isActive() ? "Active" : "Suspended";
                                                        String btnClass = u.isActive() ? "btn-danger" : "btn-success";
                                                        String btnLabel = u.isActive() ? "Suspend" : "Activate";
                                                        String confirmWord = u.isActive() ? "suspend" : "reactivate";
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <%= u.getUserId() %>
                                                            </td>
                                                            <td>
                                                                <%= u.getUsername() %>
                                                            </td>
                                                            <td>
                                                                <%= u.getFullName() %>
                                                            </td>
                                                            <td><span class="badge badge-<%= roleBadge %>">
                                                                    <%= u.getRole() %>
                                                                </span></td>
                                                            <td><span class="badge <%= statusClass %>">
                                                                    <%= statusLabel %>
                                                                </span></td>
                                                            <td class="actions">
                                                                <a href="<%=request.getContextPath()%>/app/users/edit?id=<%= u.getUserId() %>"
                                                                    class="btn btn-secondary btn-sm">Edit</a>
                                                                <% if (u.getUserId() !=loggedInUser.getUserId()) { %>
                                                                    <form
                                                                        action="<%=request.getContextPath()%>/app/users/toggle-status"
                                                                        method="POST" style="display:inline;">
                                                                        <input type="hidden" name="userId"
                                                                            value="<%= u.getUserId() %>">
                                                                        <button type="submit"
                                                                            class="btn <%= btnClass %> btn-sm"
                                                                            onclick="return confirm('Are you sure you want to <%= confirmWord %> this user?');">
                                                                            <%= btnLabel %>
                                                                        </button>
                                                                    </form>
                                                                    <% } %>
                                                            </td>
                                                        </tr>
                                                        <% } } else { %>
                                                            <tr>
                                                                <td colspan="6" style="text-align: center;">No users
                                                                    found.</td>
                                                            </tr>
                                                            <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                </main>
            </body>

            </html>