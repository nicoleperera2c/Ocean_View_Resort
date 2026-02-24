package com.oceanview.dao.impl;

import com.oceanview.dao.interfaces.IGuestDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.Guest;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GuestDAO implementation - handles all guest database operations
 * Uses prepared statements to prevent SQL injection
 */
public class GuestDAOImpl implements IGuestDAO {

    private DatabaseConnection dbConnection;

    public GuestDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public int createGuest(Guest guest) throws DAOException {
        String sql = "INSERT INTO guests (first_name, last_name, phone, email, address, city, country, id_type, id_number) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhone());
            pstmt.setString(4, guest.getEmail());
            pstmt.setString(5, guest.getAddress());
            pstmt.setString(6, guest.getCity());
            pstmt.setString(7, guest.getCountry());
            pstmt.setString(8, guest.getIdType());
            pstmt.setString(9, guest.getIdNumber());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Creating guest failed, no rows affected");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DAOException("Creating guest failed, no ID obtained");
            }

        } catch (SQLException e) {
            throw new DAOException("Error creating guest: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Guest findById(int guestId) throws DAOException {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guestId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding guest by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Guest findByPhone(String phone) throws DAOException {
        String sql = "SELECT * FROM guests WHERE phone = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding guest by phone: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Guest> findAll() throws DAOException {
        String sql = "SELECT * FROM guests ORDER BY registered_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Guest> guests = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }

            return guests;

        } catch (SQLException e) {
            throw new DAOException("Error finding all guests: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Guest> searchGuests(String keyword) throws DAOException {
        String sql = "SELECT * FROM guests WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR email LIKE ? "
                +
                "ORDER BY registered_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Guest> guests = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }

            return guests;

        } catch (SQLException e) {
            throw new DAOException("Error searching guests: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateGuest(Guest guest) throws DAOException {
        String sql = "UPDATE guests SET first_name = ?, last_name = ?, phone = ?, email = ?, " +
                "address = ?, city = ?, country = ?, id_type = ?, id_number = ? WHERE guest_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhone());
            pstmt.setString(4, guest.getEmail());
            pstmt.setString(5, guest.getAddress());
            pstmt.setString(6, guest.getCity());
            pstmt.setString(7, guest.getCountry());
            pstmt.setString(8, guest.getIdType());
            pstmt.setString(9, guest.getIdNumber());
            pstmt.setInt(10, guest.getGuestId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating guest: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean guestExists(String phone) throws DAOException {
        String sql = "SELECT COUNT(*) FROM guests WHERE phone = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            throw new DAOException("Error checking guest existence: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Map ResultSet to Guest object
     * Uses created_at column (matching DB schema)
     */
    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestId(rs.getInt("guest_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setPhone(rs.getString("phone"));
        guest.setEmail(rs.getString("email"));
        guest.setAddress(rs.getString("address"));
        guest.setCity(rs.getString("city"));
        guest.setCountry(rs.getString("country"));
        guest.setIdType(rs.getString("id_type"));
        guest.setIdNumber(rs.getString("id_number"));

        Timestamp createdAt = rs.getTimestamp("registered_at");
        if (createdAt != null) {
            guest.setCreatedAt(createdAt.toLocalDateTime());
        }

        return guest;
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* logged */ }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                /* logged */ }
        }
        if (conn != null) {
            dbConnection.closeConnection(conn);
        }
    }
}
