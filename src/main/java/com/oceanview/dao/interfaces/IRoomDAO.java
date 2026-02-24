package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import java.time.LocalDate;
import java.util.List;

public interface IRoomDAO {
    
    Room findById(int roomId) throws DAOException;
    
    Room findByRoomNumber(String roomNumber) throws DAOException;
    
    List<Room> findAll() throws DAOException;
    
    List<Room> findAvailableRooms() throws DAOException;
    
    List<Room> findAvailableRoomsByDateRange(LocalDate checkIn, LocalDate checkOut) throws DAOException;
    
    List<Room> findByRoomType(int roomTypeId) throws DAOException;
    
    boolean updateRoomStatus(int roomId, Room.RoomStatus status) throws DAOException;
    
    List<RoomType> findAllRoomTypes() throws DAOException;
    
    RoomType findRoomTypeById(int roomTypeId) throws DAOException;
    
    int countRoomsByStatus(Room.RoomStatus status) throws DAOException;
}
