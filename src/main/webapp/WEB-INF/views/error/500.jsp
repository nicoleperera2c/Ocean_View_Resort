<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>500 Internal Error - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body>
        <main class="container">
            <h2>500 - Internal Server Error</h2>
            <p>An unexpected error occurred. Please try again later.</p>
            <a href="<%=request.getContextPath()%>/login" class="btn btn-primary">Go to Login</a>
        </main>
    </body>

    </html>