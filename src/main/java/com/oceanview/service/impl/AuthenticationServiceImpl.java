package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IUserDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.interfaces.IAuthenticationService;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Authentication Service Implementation
 * Handles user login, registration, password management
 * Supports legacy MD5 password verification for existing DB users
 */
public class AuthenticationServiceImpl implements IAuthenticationService {

    private IUserDAO userDAO;

    public AuthenticationServiceImpl() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
    }

    @Override
    public User authenticate(String username, String password) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)) {
            throw new ServiceException("Username and password are required");
        }

        try {
            User user = userDAO.findByUsername(username);

            if (user == null) {
                throw new ServiceException("Invalid username or password");
            }

            if (!user.isActive()) {
                throw new ServiceException("Account is deactivated. Contact administrator");
            }

            // Try SHA-256 hashed password first (new format)
            boolean passwordValid = false;
            try {
                passwordValid = PasswordUtil.verifyPassword(password, user.getPasswordHash());
            } catch (Exception e) {
                // Not a SHA-256 hash, try legacy MD5
            }

            // Fallback: try legacy MD5 hash (for existing DB users)
            if (!passwordValid) {
                passwordValid = verifyMD5Password(password, user.getPasswordHash());
            }

            if (!passwordValid) {
                throw new ServiceException("Invalid username or password");
            }

            // Update last login timestamp
            userDAO.updateLastLogin(user.getUserId());

            return user;

        } catch (DAOException e) {
            throw new ServiceException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    public User registerUser(User user, String password) throws ServiceException {
        // Validate input
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (ValidationUtil.isNullOrEmpty(user.getUsername())) {
            throw new ServiceException("Username is required");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new ServiceException("Password must be at least 6 characters");
        }
        if (!ValidationUtil.isNullOrEmpty(user.getEmail()) && !ValidationUtil.isValidEmail(user.getEmail())) {
            throw new ServiceException("Invalid email address format");
        }

        try {
            // Check if username already exists
            if (!userDAO.isUsernameAvailable(user.getUsername())) {
                throw new ServiceException("Username '" + user.getUsername() + "' is already taken");
            }

            // Hash password using SHA-256
            user.setPasswordHash(PasswordUtil.hashPassword(password));
            user.setActive(true);

            if (user.getRole() == null) {
                user.setRole(User.UserRole.STAFF);
            }

            int userId = userDAO.createUser(user);
            user.setUserId(userId);

            return user;

        } catch (DAOException e) {
            throw new ServiceException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws ServiceException {
        if (ValidationUtil.isNullOrEmpty(oldPassword) || ValidationUtil.isNullOrEmpty(newPassword)) {
            throw new ServiceException("Old and new passwords are required");
        }
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new ServiceException("New password must be at least 6 characters");
        }

        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ServiceException("User not found");
            }

            // Verify old password
            boolean oldPasswordValid = false;
            try {
                oldPasswordValid = PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash());
            } catch (Exception e) {
                oldPasswordValid = verifyMD5Password(oldPassword, user.getPasswordHash());
            }

            if (!oldPasswordValid) {
                throw new ServiceException("Current password is incorrect");
            }

            // Hash and set new password
            String newHash = PasswordUtil.hashPassword(newPassword);
            return userDAO.changePassword(userId, newHash);

        } catch (DAOException e) {
            throw new ServiceException("Password change failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout(int userId) throws ServiceException {
        // Session invalidation is handled at the servlet level
        // This method can be extended for audit logging
    }

    /**
     * Legacy MD5 password verification for existing database users
     * The DB stores passwords as MD5 hashes (e.g., 'admin123' →
     * 'e10adc3949ba59abbe56e057f20f883e')
     */
    private boolean verifyMD5Password(String password, String storedHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().equals(storedHash);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}