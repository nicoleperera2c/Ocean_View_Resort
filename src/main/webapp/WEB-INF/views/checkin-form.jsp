<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Check-In - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <main class="container">
            <h2>Guest Check-In</h2>
            <% if (request.getAttribute("error") !=null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
                <% } %>
                    <div class="card">
                        <% String resNum=request.getParameter("number"); if (resNum==null) resNum=(String)
                            request.getAttribute("reservationNumber"); resNum=(resNum !=null) ? resNum : "" ; %>
                            <form method="post" action="<%=request.getContextPath()%>/app/reservation/checkin">
                                <div class="form-group">
                                    <label for="reservationNumber">Reservation Number</label>
                                    <input type="text" id="reservationNumber" name="reservationNumber"
                                        value="<%= resNum %>" <%=!resNum.isEmpty() ? "readonly" : "" %> required>
                                </div>
                                <button type="submit" class="btn btn-primary">Check In Guest</button>
                                <a href="<%=request.getContextPath()%>/app/reservation" class="btn">Back</a>
                            </form>
                    </div>
        </main>
    </body>

    </html>