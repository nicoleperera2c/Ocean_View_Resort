package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.Guest;
import java.util.List;

public interface IGuestDAO {
    
    int createGuest(Guest guest) throws DAOException;
    
    Guest findById(int guestId) throws DAOException;
    
    Guest findByPhone(String phone) throws DAOException;
    
    List<Guest> findAll() throws DAOException;
    
    List<Guest> searchGuests(String keyword) throws DAOException;
    
    boolean updateGuest(Guest guest) throws DAOException;
    
    boolean guestExists(String phone) throws DAOException;
}
