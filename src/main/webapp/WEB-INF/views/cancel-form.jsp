<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Cancel Reservation - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <main class="container">
            <h2>Cancel Reservation</h2>
            <% if (request.getAttribute("error") !=null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
                <% } %>
                    <% String resNumber=request.getAttribute("reservationNumber") !=null ? (String)
                        request.getAttribute("reservationNumber") : "" ; %>
                        <div class="card">
                            <p>Are you sure you want to cancel reservation: <strong>
                                    <%= resNumber %>
                                </strong>?</p>
                            <form method="post" action="<%=request.getContextPath()%>/app/reservation/cancel">
                                <input type="hidden" name="reservationNumber" value="<%= resNumber %>">
                                <input type="hidden" name="confirmCancel" value="yes">
                                <button type="submit" class="btn btn-danger">Confirm Cancellation</button>
                                <a href="<%=request.getContextPath()%>/app/reservation" class="btn">Go Back</a>
                            </form>
                        </div>
        </main>
    </body>

    </html>