package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Guest;
import java.util.List;

public interface IGuestService {
    
    Guest createGuest(Guest guest) throws ServiceException;
    
    Guest getGuestById(int guestId) throws ServiceException;
    
    Guest getGuestByPhone(String phone) throws ServiceException;
    
    List<Guest> getAllGuests() throws ServiceException;
    
    List<Guest> searchGuests(String keyword) throws ServiceException;
    
    boolean updateGuest(Guest guest) throws ServiceException;
}
