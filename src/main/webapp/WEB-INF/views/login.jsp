<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body class="login-page">
        <div class="login-container">
            <div class="login-box">
                <h1>OCEAN VIEW RESORT</h1>
                <p class="subtitle">Hotel Management System</p>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <% String savedUsername=request.getAttribute("username") !=null ? (String)
                            request.getAttribute("username") : "" ; %>
                            <form method="post" action="<%=request.getContextPath()%>/login">
                                <div class="form-group">
                                    <label for="username">Username</label>
                                    <input type="text" id="username" name="username" value="<%= savedUsername %>"
                                        placeholder="Enter username" required autofocus>
                                </div>
                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password" id="password" name="password" placeholder="Enter password"
                                        required>
                                </div>
                                <div class="form-group checkbox-group">
                                    <label><input type="checkbox" name="remember"> Remember me</label>
                                </div>
                                <button type="submit" class="btn btn-primary btn-block">Login</button>
                            </form>
            </div>
        </div>
    </body>

    </html>