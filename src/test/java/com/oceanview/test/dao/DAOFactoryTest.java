package com.oceanview.test.dao;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for DAOFactory.
 * Tests the Singleton design pattern implementation and
 * the Factory pattern for creating DAO instances.
 */
public class DAOFactoryTest {

    // ==================== Singleton Pattern ====================

    @Test
    public void testGetInstanceReturnsNonNull() {
        DAOFactory factory = DAOFactory.getInstance();
        assertNotNull("Factory instance should not be null", factory);
    }

    @Test
    public void testGetInstanceReturnsSameInstance() {
        DAOFactory instance1 = DAOFactory.getInstance();
        DAOFactory instance2 = DAOFactory.getInstance();
        assertSame("Singleton should return the same instance",
                instance1, instance2);
    }

    @Test
    public void testGetInstanceConsistentAcrossMultipleCalls() {
        DAOFactory first = DAOFactory.getInstance();
        for (int i = 0; i < 10; i++) {
            assertSame("Every call should return same Singleton instance",
                    first, DAOFactory.getInstance());
        }
    }

    // ==================== Factory Methods ====================

    @Test
    public void testGetUserDAOReturnsNonNull() {
        IUserDAO dao = DAOFactory.getInstance().getUserDAO();
        assertNotNull("getUserDAO() should return non-null", dao);
    }

    @Test
    public void testGetGuestDAOReturnsNonNull() {
        IGuestDAO dao = DAOFactory.getInstance().getGuestDAO();
        assertNotNull("getGuestDAO() should return non-null", dao);
    }

    @Test
    public void testGetRoomDAOReturnsNonNull() {
        IRoomDAO dao = DAOFactory.getInstance().getRoomDAO();
        assertNotNull("getRoomDAO() should return non-null", dao);
    }

    @Test
    public void testGetReservationDAOReturnsNonNull() {
        IReservationDAO dao = DAOFactory.getInstance().getReservationDAO();
        assertNotNull("getReservationDAO() should return non-null", dao);
    }

    @Test
    public void testGetPaymentDAOReturnsNonNull() {
        IPaymentDAO dao = DAOFactory.getInstance().getPaymentDAO();
        assertNotNull("getPaymentDAO() should return non-null", dao);
    }

    // ==================== Factory Returns Correct Interface Types
    // ====================

    @Test
    public void testGetUserDAOReturnsCorrectType() {
        Object dao = DAOFactory.getInstance().getUserDAO();
        assertTrue("Should implement IUserDAO",
                dao instanceof IUserDAO);
    }

    @Test
    public void testGetGuestDAOReturnsCorrectType() {
        Object dao = DAOFactory.getInstance().getGuestDAO();
        assertTrue("Should implement IGuestDAO",
                dao instanceof IGuestDAO);
    }

    @Test
    public void testGetRoomDAOReturnsCorrectType() {
        Object dao = DAOFactory.getInstance().getRoomDAO();
        assertTrue("Should implement IRoomDAO",
                dao instanceof IRoomDAO);
    }

    @Test
    public void testGetReservationDAOReturnsCorrectType() {
        Object dao = DAOFactory.getInstance().getReservationDAO();
        assertTrue("Should implement IReservationDAO",
                dao instanceof IReservationDAO);
    }

    @Test
    public void testGetPaymentDAOReturnsCorrectType() {
        Object dao = DAOFactory.getInstance().getPaymentDAO();
        assertTrue("Should implement IPaymentDAO",
                dao instanceof IPaymentDAO);
    }

    // ==================== Factory Creates New Instances Each Time
    // ====================

    @Test
    public void testFactoryCreatesNewDAOInstances() {
        IUserDAO dao1 = DAOFactory.getInstance().getUserDAO();
        IUserDAO dao2 = DAOFactory.getInstance().getUserDAO();
        assertNotSame("Factory should create new DAO instances each call",
                dao1, dao2);
    }
}
