package com.oceanview.test.dao;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dao.interfaces.IGuestDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.model.Guest;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Complete test suite for GuestDAO
 */
public class GuestDAOTest {

    private static IGuestDAO guestDAO;
    private Guest testGuest;

    @BeforeClass
    public static void setUpClass() {
        guestDAO = new GuestDAOImpl();
    }

    @Before
    public void setUp() {
        testGuest = new Guest();
        testGuest.setFirstName("John");
        testGuest.setLastName("Doe");
        testGuest.setPhone("077" + System.currentTimeMillis() % 10000000);
        testGuest.setEmail("john.doe@example.com");
        testGuest.setAddress("123 Test Street");
        testGuest.setCity("Colombo");
        testGuest.setCountry("Sri Lanka");
        testGuest.setIdType("NIC");
        testGuest.setIdNumber("123456789V");
    }

    @Test
    public void testCreateGuest() throws DAOException {
        int guestId = guestDAO.createGuest(testGuest);
        assertTrue("Guest ID should be positive", guestId > 0);
        testGuest.setGuestId(guestId);
    }

    @Test
    public void testFindById() throws DAOException {
        int guestId = guestDAO.createGuest(testGuest);

        Guest found = guestDAO.findById(guestId);
        assertNotNull("Guest should be found", found);
        assertEquals("Guest IDs should match", guestId, found.getGuestId());
        assertEquals("Names should match",
                testGuest.getFullName(), found.getFullName());
    }

    @Test
    public void testFindByPhone() throws DAOException {
        int guestId = guestDAO.createGuest(testGuest);

        Guest found = guestDAO.findByPhone(testGuest.getPhone());
        assertNotNull("Guest should be found by phone", found);
        assertEquals("Phone numbers should match",
                testGuest.getPhone(), found.getPhone());
    }

    @Test
    public void testUpdateGuest() throws DAOException {
        int guestId = guestDAO.createGuest(testGuest);
        testGuest.setGuestId(guestId);

        testGuest.setFirstName("Jane");
        testGuest.setEmail("jane.doe@example.com");

        boolean updated = guestDAO.updateGuest(testGuest);
        assertTrue("Update should succeed", updated);

        Guest found = guestDAO.findById(guestId);
        assertEquals("First name should be updated", "Jane", found.getFirstName());
        assertEquals("Email should be updated",
                "jane.doe@example.com", found.getEmail());
    }

    @Test
    public void testFindAll() throws DAOException {
        guestDAO.createGuest(testGuest);

        List<Guest> guests = guestDAO.findAll();
        assertNotNull("Guest list should not be null", guests);
        assertTrue("Should have at least one guest", guests.size() > 0);
    }

    @Test
    public void testSearchGuests() throws DAOException {
        int guestId = guestDAO.createGuest(testGuest);

        List<Guest> results = guestDAO.searchGuests("John");
        assertNotNull("Search results should not be null", results);
        assertTrue("Should find the test guest", results.size() > 0);

        boolean found = false;
        for (Guest g : results) {
            if (g.getGuestId() == guestId) {
                found = true;
                break;
            }
        }
        assertTrue("Test guest should be in results", found);
    }

    @Test
    public void testGuestExists() throws DAOException {
        assertFalse("New phone should not exist",
                guestDAO.guestExists(testGuest.getPhone()));

        guestDAO.createGuest(testGuest);

        assertTrue("Created guest phone should exist",
                guestDAO.guestExists(testGuest.getPhone()));
    }

    @Test
    public void testSearchByPhone() throws DAOException {
        guestDAO.createGuest(testGuest);

        List<Guest> results = guestDAO.searchGuests(testGuest.getPhone());
        assertNotNull("Search results should not be null", results);
        assertTrue("Should find guest by phone", results.size() > 0);
    }

    @Test
    public void testSearchByEmail() throws DAOException {
        guestDAO.createGuest(testGuest);

        List<Guest> results = guestDAO.searchGuests("john.doe");
        assertNotNull("Search results should not be null", results);
        assertTrue("Should find guest by email", results.size() > 0);
    }

    @After
    public void tearDown() {
        testGuest = null;
    }

    @AfterClass
    public static void tearDownClass() {
        guestDAO = null;
    }
}
