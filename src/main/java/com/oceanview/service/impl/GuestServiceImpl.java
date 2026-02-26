package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IGuestDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Guest;
import com.oceanview.service.interfaces.IGuestService;
import com.oceanview.util.ValidationUtil;

import java.util.List;

/**
 * Guest Service Implementation
 * Handles business logic for guest management
 */
public class GuestServiceImpl implements IGuestService {

    private IGuestDAO guestDAO;

    public GuestServiceImpl() {
        this.guestDAO = DAOFactory.getInstance().getGuestDAO();
    }

    @Override
    public Guest createGuest(Guest guest) throws ServiceException {
        validateGuest(guest);

        try {
            // Check for duplicate phone number
            if (guestDAO.guestExists(guest.getPhone())) {
                throw new ServiceException("A guest with phone number " + guest.getPhone() + " already exists");
            }
            // Try to create the guest
            guest.setGuestId(guestDAO.createGuest(guest));
            return guest;

        } catch (DAOException e) {
            throw new ServiceException("Error creating guest: " + e.getMessage(), e);
        }
    }

    @Override
    public Guest getGuestById(int guestId) throws ServiceException {
        if (guestId <= 0) {
            throw new ServiceException("Valid guest ID is required");
        }

        try {
            Guest guest = guestDAO.findById(guestId);
            if (guest == null) {
                throw new ServiceException("Guest not found with ID: " + guestId);
            }
            return guest;
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving guest: " + e.getMessage(), e);
        }
    }

    @Override
    public Guest getGuestByPhone(String phone) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(phone)) {
            throw new ServiceException("Phone number is required");
        }

        try {
            return guestDAO.findByPhone(phone);
        } catch (DAOException e) {
            throw new ServiceException("Error finding guest by phone: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Guest> getAllGuests() throws ServiceException {
        try {
            return guestDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Error retrieving guests: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Guest> searchGuests(String keyword) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(keyword)) {
            throw new ServiceException("Search keyword is required");
        }

        try {
            return guestDAO.searchGuests(keyword);
        } catch (DAOException e) {
            throw new ServiceException("Error searching guests: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateGuest(Guest guest) throws ServiceException {
        if (guest == null || guest.getGuestId() <= 0) {
            throw new ServiceException("Valid guest is required for update");
        }
        validateGuest(guest);

        try {
            return guestDAO.updateGuest(guest);
        } catch (DAOException e) {
            throw new ServiceException("Error updating guest: " + e.getMessage(), e);
        }
    }

    /**
     * Validate guest data
     */
    private void validateGuest(Guest guest) throws ServiceException {
        if (guest == null) {
            throw new ServiceException("Guest cannot be null");
        }
        if (ValidationUtil.isNullOrEmpty(guest.getFirstName())) {
            throw new ServiceException("First name is required");
        }
        if (!ValidationUtil.isValidName(guest.getFirstName())) {
            throw new ServiceException("Invalid first name format");
        }
        if (ValidationUtil.isNullOrEmpty(guest.getLastName())) {
            throw new ServiceException("Last name is required");
        }
        if (!ValidationUtil.isValidName(guest.getLastName())) {
            throw new ServiceException("Invalid last name format");
        }
        if (ValidationUtil.isNullOrEmpty(guest.getPhone())) {
            throw new ServiceException("Phone number is required");
        }
        if (!ValidationUtil.isValidPhone(guest.getPhone())) {
            throw new ServiceException("Invalid phone number format (must be 10 digits)");
        }
        if (!ValidationUtil.isNullOrEmpty(guest.getEmail()) && !ValidationUtil.isValidEmail(guest.getEmail())) {
            throw new ServiceException("Invalid email address format");
        }
    }
}
