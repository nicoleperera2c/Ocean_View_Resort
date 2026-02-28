<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Reservation" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Bill - Ocean View Resort</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
        </head>

        <body>
            <jsp:include page="header.jsp" />
            <main class="container">
                <% Reservation reservation=(Reservation) request.getAttribute("reservation"); %>
                    <% if (reservation !=null) { %>
                        <div class="bill-container">
                            <h2>INVOICE</h2>
                            <div class="bill-header">
                                <p><strong>Ocean View Resort</strong></p>
                                <p>Reservation: <%= reservation.getReservationNumber() %>
                                </p>
                                <p>Date: <%= java.time.LocalDate.now() %>
                                </p>
                            </div>
                            <hr>
                            <div class="bill-guest">
                                <p><strong>Guest:</strong>
                                    <%= reservation.getGuest() !=null ? reservation.getGuest().getFullName() : "N/A" %>
                                </p>
                                <p><strong>Phone:</strong>
                                    <%= reservation.getGuest() !=null ? reservation.getGuest().getPhone() : "N/A" %>
                                </p>
                                <p><strong>Email:</strong>
                                    <%= reservation.getGuest() !=null ? reservation.getGuest().getEmail() : "N/A" %>
                                </p>
                            </div>
                            <hr>
                            <table class="bill-table">
                                <thead>
                                    <tr>
                                        <th>Description</th>
                                        <th>Details</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Room <%= reservation.getRoom() !=null ?
                                                reservation.getRoom().getRoomNumber() : "" %>
                                        </td>
                                        <td>
                                            <%= reservation.getRoom() !=null && reservation.getRoom().getRoomType()
                                                !=null ? reservation.getRoom().getRoomType().getTypeName() : "" %>
                                        </td>
                                        <td>Rs. <%= reservation.getRoom() !=null && reservation.getRoom().getRoomType()
                                                !=null ? reservation.getRoom().getRoomType().getBasePrice() : "0.00" %>
                                                /night</td>
                                    </tr>
                                    <tr>
                                        <td>Stay Duration</td>
                                        <td>
                                            <%= reservation.getCheckInDate() %> to <%= reservation.getCheckOutDate() %>
                                        </td>
                                        <td>
                                            <%= reservation.calculateNights() %> night(s)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Number of Guests</td>
                                        <td>
                                            <%= reservation.getNumberOfGuests() %>
                                        </td>
                                        <td>-</td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr class="total-row">
                                        <td colspan="2"><strong>TOTAL</strong></td>
                                        <td><strong>Rs. <%= reservation.getTotalAmount() %></strong></td>
                                    </tr>
                                </tfoot>
                            </table>
                            <div class="bill-actions" data-html2canvas-ignore="true">
                                <a href="<%=request.getContextPath()%>/app/payment?reservationNumber=<%= reservation.getReservationNumber() %>"
                                    class="btn btn-primary">Process Payment</a>
                                <button onclick="downloadPDF()" class="btn">Download PDF Bill</button>
                                <a href="<%=request.getContextPath()%>/app/reservation" class="btn">Back</a>
                            </div>
                        </div>
                        <% } else { %>
                            <p>Reservation not found.</p>
                            <% } %>
            </main>

            <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
            <script>
                function downloadPDF() {
                    const element = document.querySelector('.bill-container');
                    const resNumber = '<%= reservation != null ? reservation.getReservationNumber() : "Unknown" %>';

                    const opt = {
                        margin: 10,
                        filename: 'Invoice_' + resNumber + '.pdf',
                        image: { type: 'jpeg', quality: 0.98 },
                        html2canvas: { scale: 2 },
                        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
                    };

                    // Generate and download the PDF
                    html2pdf().set(opt).from(element).save();
                }
            </script>
        </body>

        </html>