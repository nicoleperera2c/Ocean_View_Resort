package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;

import java.util.List;

/**
 * IUserService - Contract for User Management operations.
 * Allows Admins to manage staff and manager accounts.
 */
public interface IUserService {

    /**
     * Retrieves all users in the system (both active and inactive).
     */
    List<User> getAllUsers() throws ServiceException;

    /**
     * Retrieves a specific user by their ID.
     */
    User getUserById(int userId) throws ServiceException;

    /**
     * Creates a new user account (Manager or Staff).
     */
    void createUser(User user, String unhashedPassword) throws ServiceException;

    /**
     * Updates an existing user's details (Name, Email, Phone, Role).
     */
    void updateUser(User user) throws ServiceException;

    /**
     * Toggles a user's active status (suspend/reactivate).
     * 
     * @param targetUserId   The ID of the user to toggle.
     * @param currentAdminId The ID of the admin performing the action (to prevent
     *                       self-lockout).
     */
    void toggleUserStatus(int targetUserId, int currentAdminId) throws ServiceException;

    /**
     * Resets a user's password (Admin action).
     * 
     * @param targetUserId The user whose password is being reset.
     * @param newPassword  The new plain text password to hash and store.
     */
    void resetPassword(int targetUserId, String newPassword) throws ServiceException;
}
