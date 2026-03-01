package com.oceanview.test.exception;

import com.oceanview.exception.ServiceException;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ValidationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for the custom exception hierarchy.
 * Tests message propagation, cause chaining, and inheritance.
 * Demonstrates the Inheritance OOP principle in the exception layer.
 */
public class ExceptionTest {

    // ==================== ServiceException ====================

    @Test
    public void testServiceExceptionMessage() {
        ServiceException ex = new ServiceException("Service error occurred");
        assertEquals("Service error occurred", ex.getMessage());
    }

    @Test
    public void testServiceExceptionWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        ServiceException ex = new ServiceException("Service error", cause);

        assertEquals("Service error", ex.getMessage());
        assertSame("Cause should be preserved", cause, ex.getCause());
    }

    @Test
    public void testServiceExceptionExtendsException() {
        ServiceException ex = new ServiceException("test");
        assertTrue("ServiceException should extend Exception",
                ex instanceof Exception);
    }

    @Test
    public void testServiceExceptionIsChecked() {
        ServiceException ex = new ServiceException("test");
        assertTrue("ServiceException should be a checked exception",
                ex instanceof Exception);
        assertFalse("ServiceException should NOT be a RuntimeException",
                RuntimeException.class.isInstance(ex));
    }

    // ==================== DAOException ====================

    @Test
    public void testDAOExceptionMessage() {
        DAOException ex = new DAOException("Database error");
        assertEquals("Database error", ex.getMessage());
    }

    @Test
    public void testDAOExceptionWithCause() {
        Exception cause = new Exception("SQL syntax error");
        DAOException ex = new DAOException("DAO error", cause);

        assertEquals("DAO error", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testDAOExceptionWithCauseOnly() {
        RuntimeException cause = new RuntimeException("connection timeout");
        DAOException ex = new DAOException(cause);

        assertSame(cause, ex.getCause());
    }

    @Test
    public void testDAOExceptionExtendsException() {
        DAOException ex = new DAOException("test");
        assertTrue(ex instanceof Exception);
        assertFalse(RuntimeException.class.isInstance(ex));
    }

    // ==================== ValidationException ====================

    @Test
    public void testValidationExceptionMessage() {
        ValidationException ex = new ValidationException("Invalid email format");
        assertEquals("Invalid email format", ex.getMessage());
    }

    @Test
    public void testValidationExceptionWithCause() {
        NumberFormatException cause = new NumberFormatException("not a number");
        ValidationException ex = new ValidationException("Validation failed", cause);

        assertEquals("Validation failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testValidationExceptionExtendsException() {
        ValidationException ex = new ValidationException("test");
        assertTrue(ex instanceof Exception);
        assertFalse(RuntimeException.class.isInstance(ex));
    }

    // ==================== Exception Chaining Pattern ====================

    @Test
    public void testExceptionChainingDAOToService() {
        // Simulates how the service layer wraps DAOException
        DAOException daoEx = new DAOException("SQL error: table not found");
        ServiceException serviceEx = new ServiceException(
                "Error creating reservation: " + daoEx.getMessage(), daoEx);

        assertTrue("Service message should contain DAO message",
                serviceEx.getMessage().contains("SQL error: table not found"));
        assertTrue("Cause should be DAOException",
                serviceEx.getCause() instanceof DAOException);
    }

    @Test
    public void testThreeLayerExceptionChaining() {
        // Root cause at JDBC level
        Exception sqlException = new Exception("Connection refused");
        // Wrapped by DAO layer
        DAOException daoEx = new DAOException("Database read failed", sqlException);
        // Wrapped by Service layer
        ServiceException serviceEx = new ServiceException("Cannot load room", daoEx);

        assertEquals("Cannot load room", serviceEx.getMessage());
        assertTrue(serviceEx.getCause() instanceof DAOException);
        assertNotNull("Root cause should be preserved",
                serviceEx.getCause().getCause());
    }

    // ==================== All Exceptions Are Independent ====================

    @Test
    public void testExceptionsAreIndependentTypes() {
        ServiceException se = new ServiceException("s");
        DAOException de = new DAOException("d");
        ValidationException ve = new ValidationException("v");

        // They should have distinct class types
        assertNotEquals(se.getClass(), de.getClass());
        assertNotEquals(de.getClass(), ve.getClass());
    }
}
