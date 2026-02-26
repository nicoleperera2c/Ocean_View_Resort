<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Guest" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Guest - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <% Guest guest=(Guest) request.getAttribute("guest"); Boolean editMode=(Boolean)
                    request.getAttribute("editMode"); boolean isEdit=editMode !=null && editMode; String
                    formAction=isEdit ? "update" : "create" ; %>
                    <h2>
                        <%= isEdit ? "Edit Guest" : "Register New Guest" %>
                    </h2>
                    <% if (request.getAttribute("error") !=null) { %>
                        <div class="alert alert-error">
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>
                            <div class="card">
                                <form method="post" action="<%=request.getContextPath()%>/app/guest/<%= formAction %>">
                                    <% if (isEdit && guest !=null) { %>
                                        <input type="hidden" name="guestId" value="<%= guest.getGuestId() %>">
                                        <% } %>
                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label>First Name *</label>
                                                    <input type="text" name="firstName"
                                                        value="<%= guest != null ? guest.getFirstName() : "" %>"
                                                        required>
                                                </div>
                                                <div class="form-group">
                                                    <label>Last Name *</label>
                                                    <input type="text" name="lastName"
                                                        value="<%= guest != null ? guest.getLastName() : "" %>"
                                                        required>
                                                </div>
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label>Phone *</label>
                                                    <input type="text" name="phone"
                                                        value="<%= guest != null ? guest.getPhone() : "" %>" required
                                                        placeholder="10 digits">
                                                </div>
                                                <div class="form-group">
                                                    <label>Email</label>
                                                    <input type="email" name="email"
                                                        value="<%= guest != null && guest.getEmail() != null ? guest.getEmail() : "" %>">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label>Address</label>
                                                <input type="text" name="address"
                                                    value="<%= guest != null && guest.getAddress() != null ? guest.getAddress() : "" %>">
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label>City</label>
                                                    <input type="text" name="city"
                                                        value="<%= guest != null && guest.getCity() != null ? guest.getCity() : "" %>">
                                                </div>
                                                <div class="form-group">
                                                    <label>Country</label>
                                                    <input type="text" name="country"
                                                        value="<%= guest != null && guest.getCountry() != null ? guest.getCountry() : "" %>">
                                                </div>
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group">
                                                    <label>ID Type</label>
                                                    <select name="idType">
                                                        <option value="NIC">NIC</option>
                                                        <option value="PASSPORT">Passport</option>
                                                        <option value="DRIVING_LICENSE">Driving License</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>ID Number</label>
                                                    <input type="text" name="idNumber"
                                                        value="<%= guest != null && guest.getIdNumber() != null ? guest.getIdNumber() : "" %>">
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-primary">
                                                <%= isEdit ? "Update Guest" : "Register Guest" %>
                                            </button>
                                            <a href="<%=request.getContextPath()%>/app/guest" class="btn">Cancel</a>
                                </form>
                            </div>
            </main>
        </body>

        </html>