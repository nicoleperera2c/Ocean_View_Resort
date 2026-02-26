<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Error - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <main class="container">
            <h2>Error</h2>
            <div class="alert alert-error">
                <%= request.getAttribute("error") !=null ? request.getAttribute("error")
                    : "An unexpected error occurred." %>
            </div>
            <a href="<%=request.getContextPath()%>/app/dashboard" class="btn">Back to Dashboard</a>
        </main>
    </body>

    </html>