package com.oceanview.dao.impl;

import com.oceanview.dao.interfaces.IUserDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.User;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO implementation - handles all user database operations
 * Uses prepared statements to prevent SQL injection
 */
public class UserDAOImpl implements IUserDAO {

    private DatabaseConnection dbConnection;

    public UserDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public int createUser(User user) throws DAOException {
        String sql = "INSERT INTO users (username, password_hash, full_name, role, email, phone, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone());
            pstmt.setBoolean(7, user.isActive());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Creating user failed, no rows affected");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DAOException("Creating user failed, no ID obtained");
            }

        } catch (SQLException e) {
            throw new DAOException("Error creating user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public User findById(int userId) throws DAOException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding user by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public User findByUsername(String username) throws DAOException {
        String sql = "SELECT * FROM users WHERE username = ? AND active = true";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding user by username: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<User> findAll() throws DAOException {
        String sql = "SELECT * FROM users WHERE active = true ORDER BY created_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            return users;

        } catch (SQLException e) {
            throw new DAOException("Error finding all users: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateUser(User user) throws DAOException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, role = ? WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getRole().name());
            pstmt.setInt(5, user.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean deleteUser(int userId) throws DAOException {
        String sql = "UPDATE users SET active = false WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error deleting user: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean isUsernameAvailable(String username) throws DAOException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return false;

        } catch (SQLException e) {
            throw new DAOException("Error checking username availability: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateLastLogin(int userId) throws DAOException {
        String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating last login: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean changePassword(int userId, String newPasswordHash) throws DAOException {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error changing password: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Map a ResultSet row to a User object
     * Column names match DB schema: is_active, created_at, last_login
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("active"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }

        return user;
    }

    @Override
    public List<User> findAllIncludingInactive() throws DAOException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            return users;

        } catch (SQLException e) {
            throw new DAOException("Error finding all users: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateStatus(int userId, boolean active) throws DAOException {
        String sql = "UPDATE users SET active = ? WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, active);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating user status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
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
