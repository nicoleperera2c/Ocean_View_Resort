<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Help - Ocean View Resort</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <main class="container">
            <h2>Help and Guidelines</h2>
            <div class="help-content">
                <div class="card">
                    <h3>1. User Authentication</h3>
                    <p>Login with your assigned username and password. Contact the administrator if you need account
                        access. Sessions expire after 30 minutes of inactivity.</p>
                </div>
                <div class="card">
                    <h3>2. Add New Reservation</h3>
                    <p>Navigate to <strong>New Reservation</strong> from the menu. First search for available rooms by
                        selecting check-in and check-out dates. Then select a guest (or register a new one), choose a
                        room, and submit the reservation.</p>
                </div>
                <div class="card">
                    <h3>3. Display Reservation Details</h3>
                    <p>Go to <strong>Reservations</strong> to view all bookings. Click <strong>View</strong> on any
                        reservation to see full details including guest info, room details, dates, and total amount.</p>
                </div>
                <div class="card">
                    <h3>4. Calculate and Print Bill</h3>
                    <p>From a reservation details page, click <strong>View Bill</strong> to see the itemized invoice.
                        The bill shows room charges, duration, and total. Use the <strong>Print</strong> button to print
                        or save as PDF.</p>
                </div>
                <div class="card">
                    <h3>5. Check-In and Check-Out</h3>
                    <p><strong>Check-In:</strong> On the check-in date, the reservation status changes to allow
                        check-in. Click the Check-In button.<br><strong>Check-Out:</strong> For checked-in guests, click
                        Check-Out. The bill will be displayed automatically.</p>
                </div>
                <div class="card">
                    <h3>6. Guest Management</h3>
                    <p>Register new guests via <strong>Guests &gt; Register Guest</strong>. Search existing guests by
                        name, phone, or email. Edit guest information as needed.</p>
                </div>
                <div class="card">
                    <h3>7. Reports and Analytics</h3>
                    <p>Generate occupancy, reservation, and revenue reports from the <strong>Reports</strong> section.
                        Reports can be filtered by date range.</p>
                </div>
                
            </div>
        </main>
    </body>

    </html>