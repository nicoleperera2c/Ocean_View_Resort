package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;

public interface IAuthenticationService {
    
    User authenticate(String username, String password) throws ServiceException;
    
    User registerUser(User user, String password) throws ServiceException;
    
    boolean changePassword(int userId, String oldPassword, String newPassword) throws ServiceException;
    
    void logout(int userId) throws ServiceException;
}
