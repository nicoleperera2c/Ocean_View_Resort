<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.Map" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Report - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>
                    <%= request.getAttribute("reportTitle") !=null ? request.getAttribute("reportTitle") : "Report" %>
                </h2>
                <% @SuppressWarnings("unchecked") Map<String, Object> report = (Map<String, Object>)
                        request.getAttribute("report");
                        %>
                        <% if (report !=null) { %>
                            <div class="stats-grid">
                                <% for (Map.Entry<String, Object> entry : report.entrySet()) {
                                    if (!(entry.getValue() instanceof java.util.List)) { %>
                                    <div class="stat-card">
                                        <h3>
                                            <%= entry.getKey() %>
                                        </h3>
                                        <span class="stat-value">
                                            <%= entry.getValue() %>
                                        </span>
                                    </div>
                                    <% } } %>
                            </div>
                            <% } else { %>
                                <p>No data available.</p>
                                <% } %>
                                    <a href="<%=request.getContextPath()%>/app/reports" class="btn">Back to Reports</a>
            </main>
        </body>

        </html>