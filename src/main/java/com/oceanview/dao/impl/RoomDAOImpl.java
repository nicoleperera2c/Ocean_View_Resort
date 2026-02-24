package com.oceanview.dao.impl;

import com.oceanview.dao.interfaces.IRoomDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomDAO implementation - handles all room database operations
 * Column names aligned with DB schema: floor_number, base_price, max_occupancy,
 * is_active
 */
public class RoomDAOImpl implements IRoomDAO {

    private DatabaseConnection dbConnection;

    public RoomDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Room findById(int roomId) throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.room_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding room by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Room findByRoomNumber(String roomNumber) throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.room_number = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomNumber);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding room by number: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Room> findAll() throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "ORDER BY r.room_number";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

            return rooms;

        } catch (SQLException e) {
            throw new DAOException("Error finding all rooms: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Room> findAvailableRooms() throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.status = 'AVAILABLE' " +
                "ORDER BY rt.type_name, r.room_number";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

            return rooms;

        } catch (SQLException e) {
            throw new DAOException("Error finding available rooms: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Room> findAvailableRoomsByDateRange(LocalDate checkIn, LocalDate checkOut) throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.room_id NOT IN (" +
                "  SELECT room_id FROM reservations " +
                "  WHERE status IN ('CONFIRMED', 'CHECKED_IN') " +
                "  AND NOT (check_out_date <= ? OR check_in_date >= ?)" +
                ") AND r.status != 'MAINTENANCE' " +
                "ORDER BY rt.type_name, r.room_number";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(checkIn));
            pstmt.setDate(2, Date.valueOf(checkOut));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

            return rooms;

        } catch (SQLException e) {
            throw new DAOException("Error finding available rooms by date range: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Room> findByRoomType(int roomTypeId) throws DAOException {
        String sql = "SELECT r.room_id, r.room_number, r.room_type_id, r.floor, r.status AS room_status, " +
                "rt.room_type_id AS rt_id, rt.type_name, rt.description AS rt_desc, rt.price_per_night, rt.capacity, rt.amenities "
                +
                "FROM rooms r INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                "WHERE r.room_type_id = ? " +
                "ORDER BY r.room_number";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomTypeId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

            return rooms;

        } catch (SQLException e) {
            throw new DAOException("Error finding rooms by type: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateRoomStatus(int roomId, Room.RoomStatus status) throws DAOException {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, roomId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating room status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public List<RoomType> findAllRoomTypes() throws DAOException {
        String sql = "SELECT * FROM room_types ORDER BY price_per_night";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RoomType> roomTypes = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                roomTypes.add(mapRoomTypeFromRoomTypeTable(rs));
            }

            return roomTypes;

        } catch (SQLException e) {
            throw new DAOException("Error finding all room types: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public RoomType findRoomTypeById(int roomTypeId) throws DAOException {
        String sql = "SELECT * FROM room_types WHERE room_type_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomTypeId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRoomTypeFromRoomTypeTable(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding room type by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public int countRoomsByStatus(Room.RoomStatus status) throws DAOException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new DAOException("Error counting rooms by status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Map ResultSet to Room - uses aliased column names from JOIN queries
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomTypeId(rs.getInt("room_type_id"));
        room.setFloorNumber(rs.getInt("floor"));
        room.setStatus(Room.RoomStatus.valueOf(rs.getString("room_status")));

        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(rs.getInt("rt_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setDescription(rs.getString("rt_desc"));
        roomType.setBasePrice(rs.getBigDecimal("price_per_night"));
        roomType.setMaxOccupancy(rs.getInt("capacity"));
        roomType.setAmenities(rs.getString("amenities"));
        roomType.setActive(true); // Default to true as DB has no column
        room.setRoomType(roomType);

        return room;
    }

    /**
     * Map ResultSet to RoomType when querying room_types table directly
     */
    private RoomType mapRoomTypeFromRoomTypeTable(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(rs.getInt("room_type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setDescription(rs.getString("description"));
        roomType.setBasePrice(rs.getBigDecimal("price_per_night"));
        roomType.setMaxOccupancy(rs.getInt("capacity"));
        roomType.setAmenities(rs.getString("amenities"));
        roomType.setActive(true); // Default to true
        return roomType;
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
