<%@ page import="com.oceanview.model.User" %>
    <% User currentUser=(User) session.getAttribute("user"); %>
        <header>
            <div class="container header-content">
                <h1>OCEAN VIEW RESORT</h1>
                <% if (currentUser !=null) { %>
                    <span class="user-info">Welcome, <%= currentUser.getFullName() %> | <%= currentUser.getRole() %>
                    </span>
                    <% } %>
            </div>
        </header>
        <nav>
            <div class="container">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/app/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/app/reservation/create">New Reservation</a></li>
                    <li><a href="<%=request.getContextPath()%>/app/reservation">Reservations</a></li>
                    <li><a href="<%=request.getContextPath()%>/app/guest">Guests</a></li>

                    <%-- Admin & Manager Only --%>
                        <% if (currentUser !=null && (currentUser.getRole()==User.UserRole.ADMIN ||
                            currentUser.getRole()==User.UserRole.MANAGER)) { %>
                            <li><a href="<%=request.getContextPath()%>/app/rooms">Rooms</a></li>
                            <li><a href="<%=request.getContextPath()%>/app/reports">Reports</a></li>
                            <% } %>

                                <%-- Admin Only --%>
                                    <% if (currentUser !=null && currentUser.getRole()==User.UserRole.ADMIN) { %>
                                        <li><a href="<%=request.getContextPath()%>/app/users">Manage Users</a></li>
                                        <% } %>

                                            <li><a href="<%=request.getContextPath()%>/app/help">Help</a></li>
                                            <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
                </ul>
            </div>
        </nav>