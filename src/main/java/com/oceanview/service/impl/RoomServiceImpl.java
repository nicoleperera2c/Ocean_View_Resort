package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IRoomDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.service.interfaces.IRoomService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RoomServiceImpl - Room Management Business Logic.
 *
 * All room CRUD operations flow through this service.
 * Enforces validation, uniqueness, and referential integrity.
 */
public class RoomServiceImpl implements IRoomService {

    private static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class.getName());
    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 20;

    private final IRoomDAO roomDAO;

    public RoomServiceImpl() {
        this.roomDAO = DAOFactory.getInstance().getRoomDAO();
    }

    // -------------------------------------------------------------------------
    // Read Operations
    // -------------------------------------------------------------------------

    @Override
    public List<Room> getAllRooms() throws ServiceException {
        try {
            return roomDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving rooms: " + e.getMessage(), e);
        }
    }

    @Override
    public Room getRoomById(int roomId) throws ServiceException {
        if (roomId <= 0) {
            throw new ServiceException("Invalid room ID.");
        }
        try {
            Room room = roomDAO.findById(roomId);
            if (room == null) {
                throw new ServiceException("Room not found with ID: " + roomId);
            }
            return room;
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving room: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Room> getRoomsByType(int roomTypeId) throws ServiceException {
        try {
            return roomDAO.findByRoomType(roomTypeId);
        } catch (DAOException e) {
            throw new ServiceException("Error filtering rooms by type: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RoomType> getAllRoomTypes() throws ServiceException {
        try {
            return roomDAO.findAllRoomTypes();
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving room types: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    @Override
    public Room addRoom(Room room) throws ServiceException {
        validateRoom(room);

        try {
            // Uniqueness check: no two rooms can have the same number
            Room existing = roomDAO.findByRoomNumber(room.getRoomNumber().trim());
            if (existing != null) {
                throw new ServiceException(
                        "Room number '" + room.getRoomNumber()
                                + "' already exists. Each room must have a unique number.");
            }

            // Validate room type exists
            RoomType type = roomDAO.findRoomTypeById(room.getRoomTypeId());
            if (type == null) {
                throw new ServiceException("Selected room type does not exist.");
            }

            int roomId = roomDAO.createRoom(room);
            room.setRoomId(roomId);
            room.setRoomType(type);

            LOGGER.log(Level.INFO, "Room created: {0} on floor {1}, type={2}",
                    new Object[] { room.getRoomNumber(), room.getFloorNumber(), type.getTypeName() });

            return room;

        } catch (DAOException e) {
            throw new ServiceException("Failed to add room: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    @Override
    public boolean updateRoom(Room room) throws ServiceException {
        validateRoom(room);

        if (room.getRoomId() <= 0) {
            throw new ServiceException("Room ID is required for update.");
        }

        try {
            // Verify the room exists
            Room existingById = roomDAO.findById(room.getRoomId());
            if (existingById == null) {
                throw new ServiceException("Room not found with ID: " + room.getRoomId());
            }

            // Uniqueness check: room number must not belong to another room
            Room existingByNumber = roomDAO.findByRoomNumber(room.getRoomNumber().trim());
            if (existingByNumber != null && existingByNumber.getRoomId() != room.getRoomId()) {
                throw new ServiceException(
                        "Room number '" + room.getRoomNumber() + "' is already assigned to another room.");
            }

            // Validate room type exists
            RoomType type = roomDAO.findRoomTypeById(room.getRoomTypeId());
            if (type == null) {
                throw new ServiceException("Selected room type does not exist.");
            }

            boolean updated = roomDAO.updateRoom(room);
            if (updated) {
                LOGGER.log(Level.INFO, "Room updated: {0}", room.getRoomNumber());
            }
            return updated;

        } catch (DAOException e) {
            throw new ServiceException("Failed to update room: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    @Override
    public boolean deleteRoom(int roomId) throws ServiceException {
        if (roomId <= 0) {
            throw new ServiceException("Invalid room ID.");
        }

        try {
            // Verify room exists
            Room room = roomDAO.findById(roomId);
            if (room == null) {
                throw new ServiceException("Room not found with ID: " + roomId);
            }

            // Guard: cannot delete a room with active reservations
            if (roomDAO.hasActiveReservations(roomId)) {
                throw new ServiceException(
                        "Cannot delete room '" + room.getRoomNumber() +
                                "'. It has active reservations (CONFIRMED or CHECKED_IN). " +
                                "Cancel or complete those reservations first.");
            }

            boolean deleted = roomDAO.deleteRoom(roomId);
            if (deleted) {
                LOGGER.log(Level.INFO, "Room deleted: {0} (ID={1})",
                        new Object[] { room.getRoomNumber(), roomId });
            }
            return deleted;

        } catch (DAOException e) {
            throw new ServiceException("Failed to delete room: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Status Update
    // -------------------------------------------------------------------------

    @Override
    public boolean updateRoomStatus(int roomId, Room.RoomStatus status) throws ServiceException {
        if (roomId <= 0) {
            throw new ServiceException("Invalid room ID.");
        }
        if (status == null) {
            throw new ServiceException("Room status is required.");
        }
        try {
            return roomDAO.updateRoomStatus(roomId, status);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update room status: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Shared Validation (DRY — used by both addRoom and updateRoom)
    // -------------------------------------------------------------------------

    /**
     * Validates the room object's fields.
     * Extracted to a single private method to satisfy the DRY principle.
     * Both addRoom and updateRoom reuse this same validation.
     */
    private void validateRoom(Room room) throws ServiceException {
        if (room == null) {
            throw new ServiceException("Room data is required.");
        }
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new ServiceException("Room number is required.");
        }
        if (room.getRoomNumber().trim().length() > 10) {
            throw new ServiceException("Room number must be 10 characters or fewer.");
        }
        if (room.getFloorNumber() < MIN_FLOOR || room.getFloorNumber() > MAX_FLOOR) {
            throw new ServiceException(
                    "Floor number must be between " + MIN_FLOOR + " and " + MAX_FLOOR + ".");
        }
        if (room.getRoomTypeId() <= 0) {
            throw new ServiceException("A valid room type must be selected.");
        }
        if (room.getStatus() == null) {
            room.setStatus(Room.RoomStatus.AVAILABLE);
        }
    }
}
