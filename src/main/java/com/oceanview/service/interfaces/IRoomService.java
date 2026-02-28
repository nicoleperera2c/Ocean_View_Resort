package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import java.util.List;

/**
 * IRoomService - Room Management Business Logic Contract.
 *
 * Business Rules:
 * 1. Room numbers must be unique across the hotel.
 * 2. Floor number must be between 1 and 20.
 * 3. Room type must be a valid, existing type.
 * 4. Rooms with active reservations (CONFIRMED/CHECKED_IN) cannot be deleted.
 * 5. Room numbers follow hotel format (e.g. 101, 202, 305).
 */
public interface IRoomService {

    /** Get all rooms with their room type information. */
    List<Room> getAllRooms() throws ServiceException;

    /** Get a single room by its internal ID. */
    Room getRoomById(int roomId) throws ServiceException;

    /** Get rooms filtered by room type. */
    List<Room> getRoomsByType(int roomTypeId) throws ServiceException;

    /** Get all available room types for dropdowns. */
    List<RoomType> getAllRoomTypes() throws ServiceException;

    /**
     * Create a new room. Validates uniqueness of room number,
     * floor range, and room type existence.
     */
    Room addRoom(Room room) throws ServiceException;

    /**
     * Update an existing room's details.
     * Validates the same rules as addRoom, plus verifies the room exists.
     */
    boolean updateRoom(Room room) throws ServiceException;

    /**
     * Delete a room by ID. Blocked if room has active reservations.
     */
    boolean deleteRoom(int roomId) throws ServiceException;

    /** Update only the status of a room. */
    boolean updateRoomStatus(int roomId, Room.RoomStatus status) throws ServiceException;
}
