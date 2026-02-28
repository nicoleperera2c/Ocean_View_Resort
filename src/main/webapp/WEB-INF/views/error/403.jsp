<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>403 Forbidden - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        <style>
            .error-container {
                text-align: center;
                padding: 50px 20px;
                margin-top: 50px;
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }

            .error-code {
                font-size: 80px;
                color: #d9534f;
                margin: 0;
                font-weight: bold;
            }

            .error-message {
                font-size: 24px;
                color: #333;
                margin-bottom: 20px;
            }

            .error-details {
                color: #666;
                margin-bottom: 30px;
            }
        </style>
    </head>

    <body>
        <jsp:include page="../header.jsp" />
        <main class="container">
            <div class="error-container">
                <h1 class="error-code">403</h1>
                <h2 class="error-message">Access Denied</h2>
                <p class="error-details">
                    You do not have the required permissions to view this page or perform this action.<br>
                    If you believe this is an error, please contact your system administrator.
                </p>
                <a href="<%=request.getContextPath()%>/app/dashboard" class="btn btn-primary">Return to Dashboard</a>
            </div>
        </main>
    </body>

    </html>