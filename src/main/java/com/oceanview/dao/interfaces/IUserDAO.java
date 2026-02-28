package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.User;
import java.util.List;

public interface IUserDAO {

    int createUser(User user) throws DAOException;

    User findById(int userId) throws DAOException;

    User findByUsername(String username) throws DAOException;

    List<User> findAll() throws DAOException;

    boolean updateUser(User user) throws DAOException;

    boolean deleteUser(int userId) throws DAOException;

    boolean isUsernameAvailable(String username) throws DAOException;

    boolean updateLastLogin(int userId) throws DAOException;

    boolean changePassword(int userId, String newPasswordHash) throws DAOException;

    /**
     * Retrieves all users including deactivated ones (for Admin panel).
     */
    List<User> findAllIncludingInactive() throws DAOException;

    /**
     * Toggles a user's active status.
     */
    boolean updateStatus(int userId, boolean active) throws DAOException;
}
