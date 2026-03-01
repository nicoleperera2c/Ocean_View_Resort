package com.oceanview.test.util;

import com.oceanview.util.ValidationUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Comprehensive JUnit tests for ValidationUtil.
 * Tests email, phone, name, username, password validation,
 * date range validation, XSS sanitization, and utility methods.
 */
public class ValidationUtilTest {

    // Email Validation 

    @Test
    public void testValidEmailStandard() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
    }

    @Test
    public void testValidEmailSubdomain() {
        assertTrue(ValidationUtil.isValidEmail("user.name@domain.co.uk"));
    }

    @Test
    public void testValidEmailPlusTag() {
        assertTrue(ValidationUtil.isValidEmail("user+tag@example.com"));
    }

    @Test
    public void testValidEmailWithNumbers() {
        assertTrue(ValidationUtil.isValidEmail("user123@test456.com"));
    }

    @Test
    public void testInvalidEmailNull() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    public void testInvalidEmailEmpty() {
        assertFalse(ValidationUtil.isValidEmail(""));
    }

    @Test
    public void testInvalidEmailWhitespace() {
        assertFalse(ValidationUtil.isValidEmail("   "));
    }

    @Test
    public void testInvalidEmailNoAt() {
        assertFalse(ValidationUtil.isValidEmail("invalid"));
    }

    @Test
    public void testInvalidEmailNoDomain() {
        assertFalse(ValidationUtil.isValidEmail("user@"));
    }

    @Test
    public void testInvalidEmailNoUser() {
        assertFalse(ValidationUtil.isValidEmail("@domain.com"));
    }

    @Test
    public void testInvalidEmailDoubleDot() {
        assertFalse(ValidationUtil.isValidEmail("user@domain..com"));
    }

    // ==================== Phone Validation ====================

    @Test
    public void testValidPhone10Digits() {
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
    }

    @Test
    public void testValidPhoneSriLankan() {
        assertTrue(ValidationUtil.isValidPhone("0771234567"));
    }

    @Test
    public void testInvalidPhoneNull() {
        assertFalse(ValidationUtil.isValidPhone(null));
    }

    @Test
    public void testInvalidPhoneEmpty() {
        assertFalse(ValidationUtil.isValidPhone(""));
    }

    @Test
    public void testInvalidPhoneTooShort() {
        assertFalse(ValidationUtil.isValidPhone("12345"));
    }

    @Test
    public void testInvalidPhoneTooLong() {
        assertFalse(ValidationUtil.isValidPhone("12345678901"));
    }

    @Test
    public void testInvalidPhoneWithLetters() {
        assertFalse(ValidationUtil.isValidPhone("abc1234567"));
    }

    @Test
    public void testInvalidPhoneWithSpaces() {
        assertFalse(ValidationUtil.isValidPhone("077 123 456"));
    }

    @Test
    public void testInvalidPhoneWithDashes() {
        assertFalse(ValidationUtil.isValidPhone("077-123-456"));
    }

    // ==================== Name Validation ====================

    @Test
    public void testValidNameSimple() {
        assertTrue(ValidationUtil.isValidName("John"));
    }

    @Test
    public void testValidNameWithSpace() {
        assertTrue(ValidationUtil.isValidName("Mary Jane"));
    }

    @Test
    public void testValidNameWithApostrophe() {
        assertTrue(ValidationUtil.isValidName("O'Brien"));
    }

    @Test
    public void testValidNameWithHyphen() {
        assertTrue(ValidationUtil.isValidName("Anne-Marie"));
    }

    @Test
    public void testInvalidNameNull() {
        assertFalse(ValidationUtil.isValidName(null));
    }

    @Test
    public void testInvalidNameEmpty() {
        assertFalse(ValidationUtil.isValidName(""));
    }

    @Test
    public void testInvalidNameTooShort() {
        assertFalse(ValidationUtil.isValidName("A"));
    }

    @Test
    public void testValidNameExactly2Chars() {
        assertTrue("2-char name should be valid", ValidationUtil.isValidName("Jo"));
    }

    @Test
    public void testInvalidNameWithNumbers() {
        assertFalse(ValidationUtil.isValidName("Name123"));
    }

    @Test
    public void testInvalidNameWithSpecialChars() {
        assertFalse(ValidationUtil.isValidName("Name@!#"));
    }

    // Username Validation 

    @Test
    public void testValidUsername() {
        assertTrue(ValidationUtil.isValidUsername("admin"));
    }

    @Test
    public void testValidUsernameWithUnderscore() {
        assertTrue(ValidationUtil.isValidUsername("user_123"));
    }

    @Test
    public void testValidUsernameExactly3Chars() {
        assertTrue("3-char username should be valid", ValidationUtil.isValidUsername("abc"));
    }

    @Test
    public void testInvalidUsernameTooShort() {
        assertFalse(ValidationUtil.isValidUsername("ab"));
    }

    @Test
    public void testInvalidUsernameWithSpace() {
        assertFalse(ValidationUtil.isValidUsername("user name"));
    }

    @Test
    public void testInvalidUsernameNull() {
        assertFalse(ValidationUtil.isValidUsername(null));
    }

    @Test
    public void testInvalidUsernameWithSpecialChars() {
        assertFalse(ValidationUtil.isValidUsername("user@name"));
    }

    // Password Validation 

    @Test
    public void testValidPasswordExactly6Chars() {
        assertTrue("6-char password should be valid",
                ValidationUtil.isValidPassword("123456"));
    }

    @Test
    public void testValidPasswordLong() {
        assertTrue(ValidationUtil.isValidPassword("securePassword123!"));
    }

    @Test
    public void testInvalidPasswordNull() {
        assertFalse(ValidationUtil.isValidPassword(null));
    }

    @Test
    public void testInvalidPasswordEmpty() {
        assertFalse(ValidationUtil.isValidPassword(""));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        assertFalse(ValidationUtil.isValidPassword("12345"));
    }

    // Date Range Validation 

    @Test
    public void testValidDateRangeTodayToTomorrow() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        assertTrue(ValidationUtil.isValidDateRange(today, tomorrow));
    }

    @Test
    public void testValidDateRangeFuture() {
        LocalDate future1 = LocalDate.now().plusDays(10);
        LocalDate future2 = LocalDate.now().plusDays(15);
        assertTrue(ValidationUtil.isValidDateRange(future1, future2));
    }

    @Test
    public void testInvalidDateRangeNullCheckIn() {
        assertFalse(ValidationUtil.isValidDateRange(null, LocalDate.now()));
    }

    @Test
    public void testInvalidDateRangeNullCheckOut() {
        assertFalse(ValidationUtil.isValidDateRange(LocalDate.now(), null));
    }

    @Test
    public void testInvalidDateRangeBothNull() {
        assertFalse(ValidationUtil.isValidDateRange(null, null));
    }

    @Test
    public void testInvalidDateRangePastCheckIn() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertFalse(ValidationUtil.isValidDateRange(yesterday, tomorrow));
    }

    @Test
    public void testInvalidDateRangeSameDay() {
        LocalDate today = LocalDate.now();
        assertFalse("Same-day check-in/out should be invalid",
                ValidationUtil.isValidDateRange(today, today));
    }

    // Sanitize Input 

    @Test
    public void testSanitizeInputNull() {
        assertEquals("", ValidationUtil.sanitizeInput(null));
    }

    @Test
    public void testSanitizeInputTrimsWhitespace() {
        assertEquals("hello", ValidationUtil.sanitizeInput("  hello  "));
    }

    @Test
    public void testSanitizeInputXssScript() {
        assertEquals("&lt;script&gt;alert(1)&lt;/script&gt;",
                ValidationUtil.sanitizeInput("<script>alert(1)</script>"));
    }

    @Test
    public void testSanitizeInputAmpersand() {
        assertEquals("test &amp; value", ValidationUtil.sanitizeInput("test & value"));
    }

    @Test
    public void testSanitizeInputDoubleQuotes() {
        assertEquals("say &quot;hello&quot;",
                ValidationUtil.sanitizeInput("say \"hello\""));
    }

    @Test
    public void testSanitizeInputSingleQuotes() {
        assertEquals("it&#x27;s fine",
                ValidationUtil.sanitizeInput("it's fine"));
    }

    @Test
    public void testSanitizeInputCleanString() {
        assertEquals("clean text", ValidationUtil.sanitizeInput("clean text"));
    }

    // Null/Empty Check 

    @Test
    public void testIsNullOrEmptyNull() {
        assertTrue(ValidationUtil.isNullOrEmpty(null));
    }

    @Test
    public void testIsNullOrEmptyEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(""));
    }

    @Test
    public void testIsNullOrEmptyWhitespace() {
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
    }

    @Test
    public void testIsNullOrEmptyValidString() {
        assertFalse(ValidationUtil.isNullOrEmpty("hello"));
    }

    // Utility Methods 

    @Test
    public void testParseIntSafeValidNumber() {
        assertEquals(42, ValidationUtil.parseIntSafe("42", 0));
    }

    @Test
    public void testParseIntSafeNegativeNumber() {
        assertEquals(-5, ValidationUtil.parseIntSafe("-5", 0));
    }

    @Test
    public void testParseIntSafeInvalidString() {
        assertEquals(0, ValidationUtil.parseIntSafe("invalid", 0));
    }

    @Test
    public void testParseIntSafeNull() {
        assertEquals(-1, ValidationUtil.parseIntSafe(null, -1));
    }

    @Test
    public void testParseIntSafeEmpty() {
        assertEquals(99, ValidationUtil.parseIntSafe("", 99));
    }

    @Test
    public void testParseIntSafeWithSpaces() {
        assertEquals(42, ValidationUtil.parseIntSafe("  42  ", 0));
    }

    @Test
    public void testIsPositiveIntPositive() {
        assertTrue(ValidationUtil.isPositiveInt(1));
        assertTrue(ValidationUtil.isPositiveInt(100));
        assertTrue(ValidationUtil.isPositiveInt(Integer.MAX_VALUE));
    }

    @Test
    public void testIsPositiveIntZeroAndNegative() {
        assertFalse(ValidationUtil.isPositiveInt(0));
        assertFalse(ValidationUtil.isPositiveInt(-1));
        assertFalse(ValidationUtil.isPositiveInt(Integer.MIN_VALUE));
    }
}
