package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IUserDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.interfaces.IUserService;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserServiceImpl - Implementation of User Management business logic.
 * Enforces validation, hashing, and prevents Admins from disabling themselves.
 */
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private final IUserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.findAllIncludingInactive();
        } catch (DAOException e) {
            String msg = "Failed to retrieve users.";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new ServiceException(msg, e);
        }
    }

    @Override
    public User getUserById(int userId) throws ServiceException {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ServiceException("User not found.");
            }
            return user;
        } catch (DAOException e) {
            String msg = "Error retrieving user.";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void createUser(User user, String unhashedPassword) throws ServiceException {
        validateUserDetails(user);

        if (ValidationUtil.isNullOrEmpty(unhashedPassword)) {
            throw new ServiceException("Password cannot be empty.");
        }
        if (unhashedPassword.length() < 6) {
            throw new ServiceException("Password must be at least 6 characters long.");
        }

        try {
            // Uniqueness check
            if (!userDAO.isUsernameAvailable(user.getUsername())) {
                throw new ServiceException("Username '" + user.getUsername() + "' is already taken.");
            }

            // Hash the password
            user.setPasswordHash(PasswordUtil.hashPassword(unhashedPassword));
            user.setActive(true); // New users are active by default

            userDAO.createUser(user);
            LOGGER.info("Created new user account: " + user.getUsername());

        } catch (DAOException e) {
            String msg = "Database error while creating user.";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void updateUser(User user) throws ServiceException {
        validateUserDetails(user);

        try {
            // Verify user exists
            User existingUser = userDAO.findById(user.getUserId());
            if (existingUser == null) {
                throw new ServiceException("User not found for update.");
            }

            // We do not update the password or username here, only details and role.
            boolean success = userDAO.updateUser(user);
            if (!success) {
                throw new ServiceException("Update failed. No rows modified.");
            }
            LOGGER.info("Updated user account details: " + user.getUsername());

        } catch (DAOException e) {
            String msg = "Database error while updating user.";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void toggleUserStatus(int targetUserId, int currentAdminId) throws ServiceException {
        if (targetUserId == currentAdminId) {
            throw new ServiceException("Action Denied: You cannot suspend your own admin account.");
        }

        try {
            User target = userDAO.findById(targetUserId);
            if (target == null) {
                throw new ServiceException("User not found.");
            }

            boolean newStatus = !target.isActive();
            boolean success = userDAO.updateStatus(targetUserId, newStatus);

            if (!success) {
                throw new ServiceException("Failed to update user status.");
            }
            LOGGER.info("Toggled status for user ID " + targetUserId + " to " + newStatus);

        } catch (DAOException e) {
            LOGGER.log(Level.SEVERE, "Database error toggling user status", e);
            throw new ServiceException("Error accessing user data.", e);
        }
    }

    @Override
    public void resetPassword(int targetUserId, String newPassword) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(newPassword)) {
            throw new ServiceException("New password cannot be empty.");
        }
        if (newPassword.length() < 6) {
            throw new ServiceException("New password must be at least 6 characters long.");
        }

        try {
            User target = userDAO.findById(targetUserId);
            if (target == null) {
                throw new ServiceException("User not found.");
            }

            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            boolean success = userDAO.changePassword(targetUserId, hashedPassword);

            if (!success) {
                throw new ServiceException("Failed to reset password.");
            }
            LOGGER.info("Password reset for user: " + target.getUsername());

        } catch (DAOException e) {
            LOGGER.log(Level.SEVERE, "Database error resetting password", e);
            throw new ServiceException("Error resetting password.", e);
        }
    }

    // --- DRY Helpers ---

    private void validateUserDetails(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User data is missing.");
        }
        if (ValidationUtil.isNullOrEmpty(user.getUsername())) {
            throw new ServiceException("Username is required.");
        }
        if (user.getUsername().length() < 3) {
            throw new ServiceException("Username must be at least 3 characters.");
        }
        if (ValidationUtil.isNullOrEmpty(user.getFullName())) {
            throw new ServiceException("Full Name is required.");
        }
        if (user.getRole() == null) {
            throw new ServiceException("User Role is required.");
        }
        if (!ValidationUtil.isNullOrEmpty(user.getEmail()) && !ValidationUtil.isValidEmail(user.getEmail())) {
            throw new ServiceException("Invalid email format.");
        }
        if (!ValidationUtil.isNullOrEmpty(user.getPhone()) && !ValidationUtil.isValidPhone(user.getPhone())) {
            throw new ServiceException("Invalid phone number format.");
        }
    }
}
