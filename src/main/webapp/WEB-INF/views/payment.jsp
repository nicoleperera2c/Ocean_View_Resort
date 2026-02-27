<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Reservation, com.oceanview.model.Payment, java.util.List" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Payment - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <h2>Process Payment</h2>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <% if (request.getAttribute("success") !=null) { %>
                            <div class="alert alert-success">
                                <%= request.getAttribute("success") %>
                            </div>
                            <% } %>
                                <% Reservation reservation=(Reservation) request.getAttribute("reservation"); %>
                                    <% if (reservation !=null) { %>
                                        <div class="card">
                                            <p><strong>Reservation:</strong>
                                                <%= reservation.getReservationNumber() %> |
                                                    <strong>Guest:</strong>
                                                    <%= reservation.getGuest() !=null ?
                                                        reservation.getGuest().getFullName() : "N/A" %> |
                                                        <strong>Total:</strong> Rs. <%= reservation.getTotalAmount() %>
                                            </p>
                                        </div>
                                        <div class="card">
                                            <h3>New Payment</h3>
                                            <form method="post" action="<%=request.getContextPath()%>/app/payment">
                                                <input type="hidden" name="action" value="process">
                                                <input type="hidden" name="reservationNumber"
                                                    value="<%= reservation.getReservationNumber() %>">
                                                <div class="form-row">
                                                    <div class="form-group">
                                                        <label>Amount (Rs.)</label>
                                                        <input type="number" name="amount" step="0.01"
                                                            value="<%= reservation.getTotalAmount() %>" required>
                                                    </div>
                                                    <div class="form-group">
                                                        <label>Payment Method</label>
                                                        <select name="paymentMethod">
                                                            <option value="CASH">Cash</option>
                                                            <option value="CREDIT_CARD">Credit Card</option>
                                                            <option value="DEBIT_CARD">Debit Card</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group"><label>Notes</label><textarea name="notes"
                                                        rows="2"></textarea></div>
                                                <button type="submit" class="btn btn-primary">Process Payment</button>
                                            </form>
                                        </div>
                                        <% @SuppressWarnings("unchecked") List<Payment> payments = (List<Payment>)
                                                request.getAttribute("payments");
                                                %>
                                                <% if (payments !=null && !payments.isEmpty()) { %>
                                                    <h3>Payment History</h3>
                                                    <table>
                                                        <thead>
                                                            <tr>
                                                                <th>Date</th>
                                                                <th>Amount</th>
                                                                <th>Method</th>
                                                                <th>Status</th>
                                                                <th>Reference</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <% for (Payment p : payments) { %>
                                                                <tr>
                                                                    <td>
                                                                        <%= p.getPaymentDate() %>
                                                                    </td>
                                                                    <td>Rs. <%= p.getAmount() %>
                                                                    </td>
                                                                    <td>
                                                                        <%= p.getPaymentMethod() %>
                                                                    </td>
                                                                    <td>
                                                                        <%= p.getPaymentStatus() %>
                                                                    </td>
                                                                    <td>
                                                                        <%= p.getTransactionReference() %>
                                                                    </td>
                                                                </tr>
                                                                <% } %>
                                                        </tbody>
                                                    </table>
                                                    <% } %>
                                                        <% } else { %>
                                                            <div class="card">
                                                                <p>Enter a reservation number to process payment.</p>
                                                                <form method="get"
                                                                    action="<%=request.getContextPath()%>/app/payment">
                                                                    <div class="form-group">
                                                                        <input type="text" name="reservationNumber"
                                                                            placeholder="Reservation Number" required>
                                                                    </div>
                                                                    <button type="submit" class="btn">Look Up</button>
                                                                </form>
                                                            </div>
                                                            <% } %>
            </main>
        </body>

        </html>