package com.oceanview.dao.impl;

import com.oceanview.dao.interfaces.IReservationDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ReservationDAO implementation - handles all reservation database operations
 * Uses aliased column names in JOINs to avoid ambiguity
 * Column names match DB schema: reservation_status, base_price, max_occupancy,
 * floor_number
 */
public class ReservationDAOImpl implements IReservationDAO {

    private DatabaseConnection dbConnection;

    /** Base SELECT with proper column aliases to avoid ambiguity in JOINs */
    private static final String BASE_SELECT = "SELECT res.reservation_id, res.reservation_number, res.guest_id, res.room_id, "
            +
            "res.check_in_date, res.check_out_date, res.number_of_guests, " +
            "res.status AS reservation_status, res.total_amount, res.special_requests, " +
            "res.created_by, res.created_at AS res_created_at, res.updated_at, " +
            "g.guest_id AS g_id, g.first_name, g.last_name, g.phone AS g_phone, g.email AS g_email, " +
            "g.address, g.city, g.country, " +
            "r.room_id AS r_id, r.room_number, r.floor, r.status AS room_status, " +
            "rt.room_type_id, rt.type_name, rt.price_per_night, rt.capacity, rt.amenities " +
            "FROM reservations res " +
            "INNER JOIN guests g ON res.guest_id = g.guest_id " +
            "INNER JOIN rooms r ON res.room_id = r.room_id " +
            "INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id ";

    public ReservationDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public int createReservation(Reservation reservation) throws DAOException {
        String sql = "INSERT INTO reservations (reservation_number, guest_id, room_id, check_in_date, check_out_date, "
                +
                "number_of_guests, status, total_amount, special_requests, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            if (reservation.getReservationNumber() == null) {
                reservation.setReservationNumber(generateReservationNumber());
            }

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, reservation.getReservationNumber());
            pstmt.setInt(2, reservation.getGuestId());
            pstmt.setInt(3, reservation.getRoomId());
            pstmt.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setInt(6, reservation.getNumberOfGuests());
            pstmt.setString(7, reservation.getStatus().name());
            pstmt.setBigDecimal(8, reservation.getTotalAmount());
            pstmt.setString(9, reservation.getSpecialRequests());
            pstmt.setInt(10, reservation.getCreatedBy());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Creating reservation failed, no rows affected");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DAOException("Creating reservation failed, no ID obtained");
            }

        } catch (SQLException e) {
            throw new DAOException("Error creating reservation: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Reservation findById(int reservationId) throws DAOException {
        String sql = BASE_SELECT + "WHERE res.reservation_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding reservation by ID: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public Reservation findByReservationNumber(String reservationNumber) throws DAOException {
        String sql = BASE_SELECT + "WHERE res.reservation_number = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservationNumber);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error finding reservation by number: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findAll() throws DAOException {
        String sql = BASE_SELECT + "ORDER BY res.created_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding all reservations: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findByGuestId(int guestId) throws DAOException {
        String sql = BASE_SELECT + "WHERE res.guest_id = ? ORDER BY res.check_in_date DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guestId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding reservations by guest: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findByStatus(Reservation.ReservationStatus status) throws DAOException {
        String sql = BASE_SELECT + "WHERE res.status = ? ORDER BY res.check_in_date";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding reservations by status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) throws DAOException {
        String sql = BASE_SELECT +
                "WHERE res.check_in_date >= ? AND res.check_out_date <= ? " +
                "ORDER BY res.check_in_date";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding reservations by date range: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findCheckInsForDate(LocalDate date) throws DAOException {
        String sql = BASE_SELECT +
                "WHERE res.check_in_date = ? AND res.status IN ('CONFIRMED') " +
                "ORDER BY g.last_name";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(date));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding check-ins for date: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findCheckOutsForDate(LocalDate date) throws DAOException {
        String sql = BASE_SELECT +
                "WHERE res.check_out_date = ? AND res.status = 'CHECKED_IN' " +
                "ORDER BY g.last_name";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(date));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding check-outs for date: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public List<Reservation> findActiveReservations() throws DAOException {
        String sql = BASE_SELECT +
                "WHERE res.status IN ('CONFIRMED', 'CHECKED_IN') " +
                "ORDER BY res.check_in_date";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> reservations = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Error finding active reservations: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public boolean updateReservation(Reservation reservation) throws DAOException {
        String sql = "UPDATE reservations SET check_in_date = ?, check_out_date = ?, " +
                "number_of_guests = ?, total_amount = ?, special_requests = ?, " +
                "status = ? WHERE reservation_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setDate(1, Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(2, Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setInt(3, reservation.getNumberOfGuests());
            pstmt.setBigDecimal(4, reservation.getTotalAmount());
            pstmt.setString(5, reservation.getSpecialRequests());
            pstmt.setString(6, reservation.getStatus().name());
            pstmt.setInt(7, reservation.getReservationId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating reservation: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean updateStatus(int reservationId, Reservation.ReservationStatus status) throws DAOException {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, reservationId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error updating reservation status: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    @Override
    public boolean cancelReservation(int reservationId) throws DAOException {
        return updateStatus(reservationId, Reservation.ReservationStatus.CANCELLED);
    }

    @Override
    public boolean checkIn(int reservationId) throws DAOException {
        return updateStatus(reservationId, Reservation.ReservationStatus.CHECKED_IN);
    }

    @Override
    public boolean checkOut(int reservationId) throws DAOException {
        return updateStatus(reservationId, Reservation.ReservationStatus.CHECKED_OUT);
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) throws DAOException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE room_id = ? AND status IN ('CONFIRMED', 'CHECKED_IN') " +
                "AND NOT (check_out_date <= ? OR check_in_date >= ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setDate(2, Date.valueOf(checkIn));
            pstmt.setDate(3, Date.valueOf(checkOut));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return false;

        } catch (SQLException e) {
            throw new DAOException("Error checking room availability: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public String generateReservationNumber() throws DAOException {
        // Reservation number format: RES-YYYYMMDD-NNNN (sequential daily counter)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePrefix = "RES" + LocalDate.now().format(formatter);

        String sql = "SELECT COUNT(*) FROM reservations WHERE reservation_number LIKE ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, datePrefix + "%");

            rs = pstmt.executeQuery();

            int sequence = 1;
            if (rs.next()) {
                sequence = rs.getInt(1) + 1;
            }

            return String.format("%s%04d", datePrefix, sequence);

        } catch (SQLException e) {
            throw new DAOException("Error generating reservation number: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Map ResultSet to Reservation with eager-loaded Guest, Room, and RoomType
     * Uses aliased column names to avoid ambiguity in multi-table JOINs
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setReservationNumber(rs.getString("reservation_number"));
        reservation.setGuestId(rs.getInt("guest_id"));
        reservation.setRoomId(rs.getInt("room_id"));
        reservation.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("reservation_status")));
        reservation.setTotalAmount(rs.getBigDecimal("total_amount"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        reservation.setCreatedBy(rs.getInt("created_by"));

        Timestamp createdAt = rs.getTimestamp("res_created_at");
        if (createdAt != null) {
            reservation.setCreatedAt(createdAt.toLocalDateTime());
        }

        // Eager-load Guest
        Guest guest = new Guest();
        guest.setGuestId(rs.getInt("g_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setPhone(rs.getString("g_phone"));
        guest.setEmail(rs.getString("g_email"));
        guest.setAddress(rs.getString("address"));
        guest.setCity(rs.getString("city"));
        guest.setCountry(rs.getString("country"));
        reservation.setGuest(guest);

        // Eager-load Room with RoomType
        Room room = new Room();
        room.setRoomId(rs.getInt("r_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setFloorNumber(rs.getInt("floor"));
        room.setStatus(Room.RoomStatus.valueOf(rs.getString("room_status")));

        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(rs.getInt("room_type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setBasePrice(rs.getBigDecimal("price_per_night"));
        roomType.setMaxOccupancy(rs.getInt("capacity"));
        roomType.setAmenities(rs.getString("amenities"));
        room.setRoomType(roomType);

        reservation.setRoom(room);

        return reservation;
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
