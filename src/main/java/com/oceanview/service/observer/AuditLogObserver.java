package com.oceanview.service.observer;

import com.oceanview.model.Reservation;
import com.oceanview.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Audit Log Observer - logs all reservation state changes to audit_log table
 * Implements the Observer pattern to decouple audit logging from business logic
 */
public class AuditLogObserver implements ReservationObserver {

    private static final Logger LOGGER = Logger.getLogger(AuditLogObserver.class.getName());
    private DatabaseConnection dbConnection;

    public AuditLogObserver() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public void onReservationCreated(Reservation reservation) {
        logAction("RESERVATION_CREATED", "reservations", reservation.getReservationId(),
                null, reservation.getStatus().name(),
                "Reservation " + reservation.getReservationNumber() + " created",
                reservation.getCreatedBy());
    }

    @Override
    public void onCheckIn(Reservation reservation) {
        logAction("CHECK_IN", "reservations", reservation.getReservationId(),
                "CONFIRMED", "CHECKED_IN",
                "Guest checked in for reservation " + reservation.getReservationNumber(),
                reservation.getCreatedBy());
    }

    @Override
    public void onCheckOut(Reservation reservation) {
        logAction("CHECK_OUT", "reservations", reservation.getReservationId(),
                "CHECKED_IN", "CHECKED_OUT",
                "Guest checked out for reservation " + reservation.getReservationNumber(),
                reservation.getCreatedBy());
    }

    @Override
    public void onCancellation(Reservation reservation) {
        logAction("CANCELLATION", "reservations", reservation.getReservationId(),
                reservation.getStatus().name(), "CANCELLED",
                "Reservation " + reservation.getReservationNumber() + " cancelled",
                reservation.getCreatedBy());
    }

    /**
     * Write audit entry to the audit_log table
     */
    private void logAction(String action, String tableName, int recordId,
            String oldValue, String newValue, String description, int userId) {
        String sql = "INSERT INTO audit_log (action, table_name, record_id, old_value, new_value, description, user_id) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, action);
            pstmt.setString(2, tableName);
            pstmt.setInt(3, recordId);
            pstmt.setString(4, oldValue);
            pstmt.setString(5, newValue);
            pstmt.setString(6, description);
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to write audit log: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    /* ignore */ }
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
    }
}
