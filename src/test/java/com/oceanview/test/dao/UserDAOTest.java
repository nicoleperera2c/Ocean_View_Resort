package com.oceanview.test.dao;

import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dao.interfaces.IUserDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Complete test suite for UserDAO
 */
public class UserDAOTest {

    private static IUserDAO userDAO;
    private User testUser;

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
    }

    @Before
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser" + System.currentTimeMillis());
        testUser.setPasswordHash(PasswordUtil.hashPassword("testpass123"));
        testUser.setFullName("Test User");
        testUser.setRole(User.UserRole.STAFF);
        testUser.setEmail("test@example.com");
        testUser.setPhone("0771234567");
    }

    @Test
    public void testCreateUser() throws DAOException {
        int userId = userDAO.createUser(testUser);
        assertTrue("User ID should be positive", userId > 0);
        testUser.setUserId(userId);

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testFindByUsername() throws DAOException {
        // First create user
        int userId = userDAO.createUser(testUser);
        testUser.setUserId(userId);

        // Then find by username
        User found = userDAO.findByUsername(testUser.getUsername());
        assertNotNull("User should be found", found);
        assertEquals("Usernames should match",
                testUser.getUsername(), found.getUsername());
        assertEquals("Full names should match",
                testUser.getFullName(), found.getFullName());

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testFindById() throws DAOException {
        int userId = userDAO.createUser(testUser);

        User found = userDAO.findById(userId);
        assertNotNull("User should be found", found);
        assertEquals("User IDs should match", userId, found.getUserId());

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testUpdateUser() throws DAOException {
        int userId = userDAO.createUser(testUser);
        testUser.setUserId(userId);

        testUser.setFullName("Updated Name");
        testUser.setEmail("updated@example.com");

        boolean updated = userDAO.updateUser(testUser);
        assertTrue("Update should succeed", updated);

        User found = userDAO.findById(userId);
        assertEquals("Name should be updated",
                "Updated Name", found.getFullName());
        assertEquals("Email should be updated",
                "updated@example.com", found.getEmail());

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testDeleteUser() throws DAOException {
        int userId = userDAO.createUser(testUser);

        boolean deleted = userDAO.deleteUser(userId);
        assertTrue("Delete should succeed", deleted);

        User found = userDAO.findById(userId);
        if (found != null) {
            assertFalse("User should be inactive", found.isActive());
        }
    }

    @Test
    public void testFindAllUsers() throws DAOException {
        List<User> users = userDAO.findAll();
        assertNotNull("User list should not be null", users);
        assertTrue("Should have at least admin user", users.size() > 0);
    }

    @Test
    public void testIsUsernameAvailable() throws DAOException {
        String uniqueUsername = "unique" + System.currentTimeMillis();
        assertTrue("New username should be available",
                userDAO.isUsernameAvailable(uniqueUsername));

        int userId = userDAO.createUser(testUser);
        assertFalse("Existing username should not be available",
                userDAO.isUsernameAvailable(testUser.getUsername()));

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testUpdateLastLogin() throws DAOException {
        int userId = userDAO.createUser(testUser);

        boolean updated = userDAO.updateLastLogin(userId);
        assertTrue("Last login update should succeed", updated);

        User found = userDAO.findById(userId);
        assertNotNull("Last login should be set", found.getLastLogin());

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    public void testChangePassword() throws DAOException {
        int userId = userDAO.createUser(testUser);

        String newPasswordHash = PasswordUtil.hashPassword("newpass123");
        boolean changed = userDAO.changePassword(userId, newPasswordHash);
        assertTrue("Password change should succeed", changed);

        User found = userDAO.findById(userId);
        assertEquals("Password hash should be updated",
                newPasswordHash, found.getPasswordHash());

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test(expected = DAOException.class)
    public void testCreateUserWithDuplicateUsername() throws DAOException {
        int userId = userDAO.createUser(testUser);

        try {
            // Try to create another user with same username
            User duplicate = new User();
            duplicate.setUsername(testUser.getUsername());
            duplicate.setPasswordHash(PasswordUtil.hashPassword("pass"));
            duplicate.setFullName("Duplicate");
            duplicate.setRole(User.UserRole.STAFF);
            duplicate.setEmail("dup@example.com");

            userDAO.createUser(duplicate);
        } finally {
            // Cleanup
            userDAO.deleteUser(userId);
        }
    }

    @After
    public void tearDown() {
        testUser = null;
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
    }
}
