<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Reservation, com.oceanview.model.Payment, java.util.List, java.math.BigDecimal"
        %>
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
                <h2>Payment Processing</h2>

                <%--====================ALERTS====================--%>
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

                                    <%--====================LOOKUP FORM (no reservation selected
                                        yet)====================--%>
                                        <% Reservation reservation=(Reservation) request.getAttribute("reservation"); if
                                            (reservation==null) { %>
                                            <div class="card">
                                                <h3>Look Up Reservation</h3>
                                                <form method="get" action="<%=request.getContextPath()%>/app/payment">
                                                    <div class="form-group">
                                                        <label>Reservation Number</label>
                                                        <input type="text" name="reservationNumber"
                                                            placeholder="e.g. OVR-20240001" required>
                                                    </div>
                                                    <button type="submit" class="btn btn-primary">Look Up</button>
                                                </form>
                                            </div>
                                            <% } else { Boolean fullyPaid=(Boolean) request.getAttribute("fullyPaid");
                                                if (fullyPaid==null) fullyPaid=false; BigDecimal
                                                outstandingBalance=(BigDecimal)
                                                request.getAttribute("outstandingBalance"); if
                                                (outstandingBalance==null) outstandingBalance=BigDecimal.ZERO; %>

                                                <%--====================RESERVATION SUMMARY====================--%>
                                                    <div class="card">
                                                        <h3>Reservation Summary</h3>
                                                        <table class="bill-table">
                                                            <tr>
                                                                <td><strong>Reservation #</strong></td>
                                                                <td>
                                                                    <%= reservation.getReservationNumber() %>
                                                                </td>
                                                                <td><strong>Status</strong></td>
                                                                <td><span
                                                                        class="badge badge-<%= reservation.getStatus().name().toLowerCase() %>">
                                                                        <%= reservation.getStatus() %>
                                                                    </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><strong>Guest</strong></td>
                                                                <td>
                                                                    <%= reservation.getGuest() !=null ?
                                                                        reservation.getGuest().getFullName() : "N/A" %>
                                                                </td>
                                                                <td><strong>Room</strong></td>
                                                                <td>
                                                                    <%= reservation.getRoom() !=null ?
                                                                        reservation.getRoom().getRoomNumber() : "N/A" %>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><strong>Check-In</strong></td>
                                                                <td>
                                                                    <%= reservation.getCheckInDate() %>
                                                                </td>
                                                                <td><strong>Check-Out</strong></td>
                                                                <td>
                                                                    <%= reservation.getCheckOutDate() %>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><strong>Total Amount</strong></td>
                                                                <td>Rs. <%= reservation.getTotalAmount() %>
                                                                </td>
                                                                <td><strong>Outstanding Balance</strong></td>
                                                                <% String
                                                                    balanceColor=outstandingBalance.compareTo(BigDecimal.ZERO)>
                                                                    0 ? "#c0392b" : "#27ae60";
                                                                    %>
                                                                    <td><strong style='color: <%= balanceColor %>'>
                                                                            Rs. <%= outstandingBalance.toPlainString()
                                                                                %>
                                                                        </strong></td>
                                                            </tr>
                                                        </table>
                                                    </div>

                                                    <%--====================PAYMENT FORM (only if not already paid &
                                                        payable status) --%>
                                                        <% String statusName=reservation.getStatus().name(); boolean
                                                            canPay=!fullyPaid && (statusName.equals("CONFIRMED") ||
                                                            statusName.equals("CHECKED_IN")); %>

                                                            <% if (fullyPaid) { %>
                                                                <div class="alert alert-success">
                                                                    <strong>Fully Paid.</strong> This reservation has
                                                                    been settled in full. No further payment is
                                                                    required.
                                                                </div>

                                                                <% } else if (!canPay) { %>
                                                                    <div class="alert alert-error">
                                                                        <strong>Payment Not Allowed.</strong>
                                                                        Payments can only be processed for reservations
                                                                        with status <em>CONFIRMED</em> or
                                                                        <em>CHECKED_IN</em>.
                                                                        Current status: <strong>
                                                                            <%= reservation.getStatus() %>
                                                                        </strong>.
                                                                    </div>

                                                                    <% } else { %>
                                                                        <%--====================PAYMENT
                                                                            FORM====================--%>
                                                                            <div class="card">
                                                                                <h3>Process Payment</h3>
                                                                                <p class="form-hint">The amount due has
                                                                                    been pre-filled by the system and
                                                                                    cannot be changed.</p>
                                                                                <form method="post"
                                                                                    action="<%=request.getContextPath()%>/app/payment">
                                                                                    <input type="hidden" name="action"
                                                                                        value="process">
                                                                                    <input type="hidden"
                                                                                        name="reservationNumber"
                                                                                        value="<%= reservation.getReservationNumber() %>">
                                                                                    <div class="form-row">
                                                                                        <div class="form-group">
                                                                                            <label>Amount Due (Rs.) —
                                                                                                Server Computed</label>
                                                                                            <%-- Amount is READ-ONLY.
                                                                                                The server ignores the
                                                                                                form value anyway. --%>
                                                                                                <input type="text"
                                                                                                    name="amount"
                                                                                                    value="<%= outstandingBalance.toPlainString() %>"
                                                                                                    readonly
                                                                                                    style="background:#f5f5f5; cursor:not-allowed;">
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <label>Payment
                                                                                                Method</label>
                                                                                            <select name="paymentMethod"
                                                                                                required>
                                                                                                <option value="">--
                                                                                                    Select --</option>
                                                                                                <option value="CASH">
                                                                                                    Cash</option>
                                                                                                <option
                                                                                                    value="CREDIT_CARD">
                                                                                                    Credit Card</option>
                                                                                                <option
                                                                                                    value="DEBIT_CARD">
                                                                                                    Debit Card</option>
                                                                                                <option
                                                                                                    value="BANK_TRANSFER">
                                                                                                    Bank Transfer
                                                                                                </option>
                                                                                                <option value="ONLINE">
                                                                                                    Online Payment
                                                                                                </option>
                                                                                            </select>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="form-group">
                                                                                        <label>Notes (optional)</label>
                                                                                        <textarea name="notes" rows="2"
                                                                                            placeholder="E.g. Receipt #, card last 4 digits..."></textarea>
                                                                                    </div>
                                                                                    <button type="submit"
                                                                                        class="btn btn-primary">Confirm
                                                                                        &amp; Process Payment</button>
                                                                                    <a href="<%=request.getContextPath()%>/app/reservation/bill?number=<%= reservation.getReservationNumber() %>"
                                                                                        class="btn">View Bill</a>
                                                                                </form>
                                                                            </div>
                                                                            <% } %>

                                                                                <%--====================PAYMENT
                                                                                    HISTORY====================--%>
                                                                                    <% @SuppressWarnings("unchecked")
                                                                                        List<Payment> payments = (List
                                                                                        <Payment>)
                                                                                            request.getAttribute("payments");
                                                                                            %>
                                                                                            <% if (payments !=null &&
                                                                                                !payments.isEmpty()) {
                                                                                                %>
                                                                                                <div class="card">
                                                                                                    <h3>Payment History
                                                                                                    </h3>
                                                                                                    <table
                                                                                                        class="bill-table">
                                                                                                        <thead>
                                                                                                            <tr>
                                                                                                                <th>ID
                                                                                                                </th>
                                                                                                                <th>Date
                                                                                                                </th>
                                                                                                                <th>Amount
                                                                                                                    (Rs.)
                                                                                                                </th>
                                                                                                                <th>Method
                                                                                                                </th>
                                                                                                                <th>Status
                                                                                                                </th>
                                                                                                                <th>Transaction
                                                                                                                    Ref
                                                                                                                </th>
                                                                                                                <th>Action
                                                                                                                </th>
                                                                                                            </tr>
                                                                                                        </thead>
                                                                                                        <tbody>
                                                                                                            <% for
                                                                                                                (Payment
                                                                                                                p :
                                                                                                                payments)
                                                                                                                { %>
                                                                                                                <tr>
                                                                                                                    <td>
                                                                                                                        <%= p.getPaymentId()
                                                                                                                            %>
                                                                                                                    </td>
                                                                                                                    <td>
                                                                                                                        <%= p.getPaymentDate()
                                                                                                                            !=null
                                                                                                                            ?
                                                                                                                            p.getPaymentDate().toLocalDate()
                                                                                                                            : "-"
                                                                                                                            %>
                                                                                                                    </td>
                                                                                                                    <td>
                                                                                                                        <%= p.getAmount()
                                                                                                                            %>
                                                                                                                    </td>
                                                                                                                    <td>
                                                                                                                        <%= p.getPaymentMethod()
                                                                                                                            %>
                                                                                                                    </td>
                                                                                                                    <td><span
                                                                                                                            class="badge badge-<%= p.getPaymentStatus().name().toLowerCase() %>">
                                                                                                                            <%= p.getPaymentStatus()
                                                                                                                                %>
                                                                                                                        </span>
                                                                                                                    </td>
                                                                                                                    <td><code><%= p.getTransactionReference() %></code>
                                                                                                                    </td>
                                                                                                                    <td>
                                                                                                                        <% if
                                                                                                                            (p.getPaymentStatus()==Payment.PaymentStatus.COMPLETED)
                                                                                                                            {
                                                                                                                            %>
                                                                                                                            <form
                                                                                                                                method="post"
                                                                                                                                action="<%=request.getContextPath()%>/app/payment"
                                                                                                                                onsubmit="return confirm('Are you sure you want to refund payment <%= p.getPaymentId() %>?');">
                                                                                                                                <input
                                                                                                                                    type="hidden"
                                                                                                                                    name="action"
                                                                                                                                    value="refund">
                                                                                                                                <input
                                                                                                                                    type="hidden"
                                                                                                                                    name="paymentId"
                                                                                                                                    value="<%= p.getPaymentId() %>">
                                                                                                                                <input
                                                                                                                                    type="hidden"
                                                                                                                                    name="reservationNumber"
                                                                                                                                    value="<%= reservation.getReservationNumber() %>">
                                                                                                                                <button
                                                                                                                                    type="submit"
                                                                                                                                    class="btn btn-danger btn-sm">Refund</button>
                                                                                                                            </form>
                                                                                                                            <% } else
                                                                                                                                {
                                                                                                                                %>
                                                                                                                                <span
                                                                                                                                    style="color:#999">—</span>
                                                                                                                                <% }
                                                                                                                                    %>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <% } %>
                                                                                                        </tbody>
                                                                                                    </table>
                                                                                                </div>
                                                                                                <% } %>

                                                                                                    <div
                                                                                                        style="margin-top:1rem;">
                                                                                                        <a href="<%=request.getContextPath()%>/app/payment"
                                                                                                            class="btn">Search
                                                                                                            Another
                                                                                                            Reservation</a>
                                                                                                        <a href="<%=request.getContextPath()%>/app/reservation"
                                                                                                            class="btn">Back
                                                                                                            to
                                                                                                            Reservations</a>
                                                                                                    </div>

                                                                                                    <% } %>
            </main>
        </body>

        </html>