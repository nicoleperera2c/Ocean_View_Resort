package com.oceanview.dao.impl;

import com.oceanview.dao.interfaces.IPaymentDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.Payment;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAO implementation - handles all payment database operations
 * Column names match DB schema: payment_status, transaction_reference,
 * payment_method
 */
public class PaymentDAOImpl implements IPaymentDAO {

    private DatabaseConnection dbConnection;

    public PaymentDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public int createPayment(Payment payment) throws DAOException {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_method, status, " +
                "transaction_id, processed_by, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, payment.getReservationId());
            pstmt.setBigDecimal(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod().name());
            pstmt.setString(4, payment.getPaymentStatus().name());
            pstmt.setString(5, payment.getTransactionReference());
            pstmt.setInt(6, payment.getProcessedBy());
            pstmt.setString(7, payment.getNotes());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Creating payment failed, no rows affected");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DAOException("Creating payment failed, no ID obtained");
            }

        } catch (SQLException e) {
            throw new DAOException("Error creating payment: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Payment findById(int paymentId) throws DAOException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paymentId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding payment by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Payment> findByReservationId(int reservationId) throws DAOException {
        String sql = "SELECT * FROM payments WHERE reservation_id = ? ORDER BY payment_date DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Payment> payments = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

            return payments;

        } catch (SQLException e) {
            throw new DAOException("Error finding payments by reservation: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws DAOException {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, paymentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating payment status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean hasCompletedPayment(int reservationId) throws DAOException {
        String sql = "SELECT COUNT(*) FROM payments WHERE reservation_id = ? AND status = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DAOException("Error checking completed payment: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public java.math.BigDecimal getTotalPaidAmount(int reservationId) throws DAOException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE reservation_id = ? AND status = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return java.math.BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DAOException("Error computing total paid: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Payment> findAll() throws DAOException {
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Payment> payments = new ArrayList<>();
        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
        } catch (SQLException e) {
            throw new DAOException("Error finding all payments: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(rs.getString("payment_method")));
        payment.setPaymentStatus(Payment.PaymentStatus.valueOf(rs.getString("status")));
        payment.setTransactionReference(rs.getString("transaction_id"));
        payment.setProcessedBy(rs.getInt("processed_by"));
        payment.setNotes(rs.getString("notes"));

        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }

        return payment;
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignored */ }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                /* ignored */ }
        }
        if (conn != null) {
            dbConnection.closeConnection(conn);
        }
    }
}
