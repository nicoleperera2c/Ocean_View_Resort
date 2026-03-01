package com.oceanview.test.service;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.impl.AuthenticationServiceImpl;
import com.oceanview.service.interfaces.IAuthenticationService;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Complete test suite for AuthenticationService
 */
public class AuthenticationServiceTest {

    private IAuthenticationService authService;

    @Before
    public void setUp() {
        authService = new AuthenticationServiceImpl();
    }

    @Test
    public void testAuthenticateValidUser() {
        try {
            User user = authService.authenticate("admin", "admin123");
            assertNotNull("User should not be null", user);
            assertEquals("Username should match", "admin", user.getUsername());
            assertEquals("Role should be ADMIN", User.UserRole.ADMIN, user.getRole());
        } catch (ServiceException e) {
            fail("Should not throw exception for valid credentials");
        }
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateInvalidPassword() throws ServiceException {
        authService.authenticate("admin", "wrongpassword");
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateInvalidUsername() throws ServiceException {
        authService.authenticate("nonexistent", "password");
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateNullUsername() throws ServiceException {
        authService.authenticate(null, "password");
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateNullPassword() throws ServiceException {
        authService.authenticate("admin", null);
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateEmptyCredentials() throws ServiceException {
        authService.authenticate("", "");
    }

    @Test
    public void testRegisterUser() {
        try {
            User newUser = new User();
            newUser.setUsername("testuser" + System.currentTimeMillis());
            newUser.setFullName("Test User");
            newUser.setRole(User.UserRole.STAFF);
            newUser.setEmail("test@example.com");
            newUser.setPhone("0771234567");

            User registered = authService.registerUser(newUser, "testpass123");
            assertNotNull("Registered user should not be null", registered);
            assertTrue("User ID should be positive", registered.getUserId() > 0);
            assertTrue("User should be active", registered.isActive());
        } catch (ServiceException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test(expected = ServiceException.class)
    public void testRegisterUserWithShortPassword() throws ServiceException {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setFullName("Test User");
        newUser.setRole(User.UserRole.STAFF);
        newUser.setEmail("test@example.com");

        authService.registerUser(newUser, "123");
    }

    @Test(expected = ServiceException.class)
    public void testRegisterUserWithInvalidEmail() throws ServiceException {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setFullName("Test User");
        newUser.setRole(User.UserRole.STAFF);
        newUser.setEmail("invalid-email");

        authService.registerUser(newUser, "testpass123");
    }

    @Test(expected = ServiceException.class)
    public void testRegisterUserWithNullUsername() throws ServiceException {
        User newUser = new User();
        newUser.setUsername(null);
        authService.registerUser(newUser, "testpass123");
    }
}
