package com.oceanview.service.observer;

import com.oceanview.model.Reservation;
import com.oceanview.util.DatabaseConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EmailNotificationObserver - Persists email notifications to both the database
 * (notifications table) and a local notification log file. In this Java EE
 * deployment without javax.mail, notifications are queued in the database and
 * written to disk so an external mail relay process (cron job / scheduled task)
 * can pick them up and send them via SMTP.
 *
 * This is an industry-standard pattern: decouple notification creation from
 * delivery so the web application never blocks on slow SMTP connections.
 *
 * Implements the Observer design pattern for notification decoupling.
 */
public class EmailNotificationObserver implements ReservationObserver {

        private static final Logger LOGGER = Logger.getLogger(EmailNotificationObserver.class.getName());
        private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private static final String NOTIFICATION_LOG_DIR = System.getProperty("catalina.base",
                        System.getProperty("user.home"))
                        + File.separator + "logs" + File.separator + "notifications";

        private final DatabaseConnection dbConnection;

        public EmailNotificationObserver() {
                this.dbConnection = DatabaseConnection.getInstance();
                ensureLogDirectoryExists();
        }

        @Override
        public void onReservationCreated(Reservation reservation) {
                String guestName = extractGuestName(reservation);
                String guestEmail = extractGuestEmail(reservation);
                String roomInfo = reservation.getRoom() != null ? reservation.getRoom().getRoomNumber() : "TBD";

                String subject = "Booking Confirmation - " + reservation.getReservationNumber();
                StringBuilder body = new StringBuilder();
                body.append("Dear ").append(guestName).append(",\n\n");
                body.append("Your reservation has been confirmed with the following details:\n\n");
                body.append("Reservation Number: ").append(reservation.getReservationNumber()).append("\n");
                body.append("Check-in Date: ").append(reservation.getCheckInDate()).append("\n");
                body.append("Check-out Date: ").append(reservation.getCheckOutDate()).append("\n");
                body.append("Room: ").append(roomInfo).append("\n");
                body.append("Total Amount: $").append(reservation.getTotalAmount()).append("\n\n");
                body.append("Please present your reservation number at the front desk during check-in.\n\n");
                body.append("Thank you for choosing Ocean View Resort.\n");
                body.append("Ocean View Resort, Galle, Sri Lanka\n");

                persistNotification(guestEmail, subject, body.toString(), reservation.getReservationNumber(),
                                "BOOKING_CONFIRMATION");
        }

        @Override
        public void onCheckIn(Reservation reservation) {
                String guestName = extractGuestName(reservation);
                String guestEmail = extractGuestEmail(reservation);

                String subject = "Welcome to Ocean View Resort - Check-in Confirmed";
                StringBuilder body = new StringBuilder();
                body.append("Dear ").append(guestName).append(",\n\n");
                body.append("Welcome to Ocean View Resort! Your check-in has been processed successfully.\n\n");
                body.append("Reservation: ").append(reservation.getReservationNumber()).append("\n");
                body.append("Room: ").append(reservation.getRoom() != null ? reservation.getRoom().getRoomNumber() : "")
                                .append("\n");
                body.append("Check-out Date: ").append(reservation.getCheckOutDate()).append("\n\n");
                body.append("For any assistance during your stay, please dial 0 from your room phone.\n\n");
                body.append("Enjoy your stay!\n");
                body.append("Ocean View Resort, Galle, Sri Lanka\n");

                persistNotification(guestEmail, subject, body.toString(), reservation.getReservationNumber(),
                                "CHECK_IN_WELCOME");
        }

        @Override
        public void onCheckOut(Reservation reservation) {
                String guestName = extractGuestName(reservation);
                String guestEmail = extractGuestEmail(reservation);

                String subject = "Thank You for Staying - Check-out Complete";
                StringBuilder body = new StringBuilder();
                body.append("Dear ").append(guestName).append(",\n\n");
                body.append("Thank you for staying at Ocean View Resort.\n\n");
                body.append("Your check-out for reservation ").append(reservation.getReservationNumber());
                body.append(" has been processed.\n");
                body.append("Total Charged: $").append(reservation.getTotalAmount()).append("\n\n");
                body.append("We hope you enjoyed your stay and look forward to welcoming you again.\n\n");
                body.append("Ocean View Resort, Galle, Sri Lanka\n");

                persistNotification(guestEmail, subject, body.toString(), reservation.getReservationNumber(),
                                "CHECK_OUT_THANKS");
        }

        @Override
        public void onCancellation(Reservation reservation) {
                String guestName = extractGuestName(reservation);
                String guestEmail = extractGuestEmail(reservation);

                String subject = "Reservation Cancelled - " + reservation.getReservationNumber();
                StringBuilder body = new StringBuilder();
                body.append("Dear ").append(guestName).append(",\n\n");
                body.append("Your reservation ").append(reservation.getReservationNumber());
                body.append(" has been cancelled as requested.\n\n");
                body.append("If this was done in error, please contact our front desk immediately.\n");
                body.append("Phone: +94 91 222 3333\n\n");
                body.append("Ocean View Resort, Galle, Sri Lanka\n");

                persistNotification(guestEmail, subject, body.toString(), reservation.getReservationNumber(),
                                "CANCELLATION_NOTICE");
        }

        /**
         * Persists a notification record to the database notification queue and writes
         * a copy to the file-based notification log for external mail relay processing.
         */
        private void persistNotification(String recipientEmail, String subject, String body,
                        String reservationNumber, String notificationType) {
                // 1) Persist to database notification queue
                writeNotificationToDatabase(recipientEmail, subject, body, reservationNumber, notificationType);

                // 2) Write to file-based notification log for external mail relay
                writeNotificationToFile(recipientEmail, subject, body, reservationNumber, notificationType);

                LOGGER.log(Level.INFO, "Notification queued: type={0}, reservation={1}, recipient={2}",
                                new Object[] { notificationType, reservationNumber, recipientEmail });
        }

        /**
         * Inserts a notification record into the database for tracking and delivery.
         * Uses the audit_log table with a NOTIFICATION action type to leverage
         * the existing schema without requiring schema modifications.
         */
        private void writeNotificationToDatabase(String recipientEmail, String subject,
                        String body, String reservationNumber, String notificationType) {
                String sql = "INSERT INTO audit_log (action, table_name, record_id, old_value, new_value, description, user_id) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                        conn = dbConnection.getConnection();
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, "NOTIFICATION_" + notificationType);
                        pstmt.setString(2, "reservations");
                        pstmt.setInt(3, 0);
                        pstmt.setString(4, recipientEmail);
                        pstmt.setString(5, subject);
                        pstmt.setString(6, "To: " + recipientEmail + " | Subject: " + subject + " | Reservation: "
                                        + reservationNumber);
                        pstmt.setInt(7, 0);
                        pstmt.executeUpdate();

                } catch (SQLException e) {
                        LOGGER.log(Level.WARNING, "Failed to persist notification to database: " + e.getMessage(), e);
                } finally {
                        closeSilently(pstmt);
                        closeConnection(conn);
                }
        }

        /**
         * Writes notification to a structured log file so an external process
         * (cron/scheduled task) can parse and deliver via SMTP.
         * File format: one notification per file, named by timestamp and reservation.
         */
        private void writeNotificationToFile(String recipientEmail, String subject,
                        String body, String reservationNumber, String notificationType) {
                String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
                String safeTimestamp = timestamp.replace(":", "-").replace(" ", "_");
                String fileName = safeTimestamp + "_" + reservationNumber + "_" + notificationType + ".txt";
                File notificationFile = new File(NOTIFICATION_LOG_DIR, fileName);

                try (PrintWriter writer = new PrintWriter(new FileWriter(notificationFile))) {
                        writer.println("NOTIFICATION_TYPE: " + notificationType);
                        writer.println("TIMESTAMP: " + timestamp);
                        writer.println("RESERVATION: " + reservationNumber);
                        writer.println("TO: " + recipientEmail);
                        writer.println("SUBJECT: " + subject);
                        writer.println("STATUS: PENDING");
                        writer.println("---BEGIN_BODY---");
                        writer.println(body);
                        writer.println("---END_BODY---");
                } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failed to write notification file: " + e.getMessage(), e);
                }
        }

        private String extractGuestName(Reservation reservation) {
                return reservation.getGuest() != null ? reservation.getGuest().getFullName() : "Guest";
        }

        private String extractGuestEmail(Reservation reservation) {
                return reservation.getGuest() != null && reservation.getGuest().getEmail() != null
                                ? reservation.getGuest().getEmail()
                                : "no-email@placeholder.local";
        }

        private void ensureLogDirectoryExists() {
                File logDir = new File(NOTIFICATION_LOG_DIR);
                if (!logDir.exists()) {
                        boolean created = logDir.mkdirs();
                        if (created) {
                                LOGGER.log(Level.INFO, "Created notification log directory: {0}", NOTIFICATION_LOG_DIR);
                        }
                }
        }

        private void closeSilently(PreparedStatement pstmt) {
                if (pstmt != null) {
                        try {
                                pstmt.close();
                        } catch (SQLException e) {
                                /* ignore */ }
                }
        }

        private void closeConnection(Connection conn) {
                if (conn != null) {
                        dbConnection.closeConnection(conn);
                }
        }
}
