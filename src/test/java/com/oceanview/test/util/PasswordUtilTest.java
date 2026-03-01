package com.oceanview.test.util;

import com.oceanview.util.PasswordUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for PasswordUtil.
 * Tests SHA-256 password hashing and verification cycle,
 * salt randomness, wrong password rejection, and edge cases.
 */
public class PasswordUtilTest {

    // Hash Generation 

    @Test
    public void testHashPasswordReturnsNonNull() {
        String hash = PasswordUtil.hashPassword("testPassword");
        assertNotNull("Hash should not be null", hash);
    }

    @Test
    public void testHashPasswordReturnsNonEmpty() {
        String hash = PasswordUtil.hashPassword("testPassword");
        assertFalse("Hash should not be empty", hash.isEmpty());
    }

    @Test
    public void testHashPasswordDifferentEachTime() {
        // Due to random salt, same password should produce different hashes
        String hash1 = PasswordUtil.hashPassword("samePassword");
        String hash2 = PasswordUtil.hashPassword("samePassword");
        assertNotEquals("Same password should produce different hashes (salt randomness)",
                hash1, hash2);
    }

    @Test
    public void testDifferentPasswordsDifferentHashes() {
        String hash1 = PasswordUtil.hashPassword("password1");
        String hash2 = PasswordUtil.hashPassword("password2");
        assertNotEquals("Different passwords should produce different hashes",
                hash1, hash2);
    }

    // Verification

    @Test
    public void testVerifyCorrectPassword() {
        String password = "mySecurePass123";
        String hash = PasswordUtil.hashPassword(password);
        assertTrue("Correct password should verify successfully",
                PasswordUtil.verifyPassword(password, hash));
    }

    @Test
    public void testVerifyWrongPassword() {
        String hash = PasswordUtil.hashPassword("correctPassword");
        assertFalse("Wrong password should fail verification",
                PasswordUtil.verifyPassword("wrongPassword", hash));
    }

    @Test
    public void testVerifyCaseSensitive() {
        String hash = PasswordUtil.hashPassword("Password");
        assertFalse("Verification should be case-sensitive",
                PasswordUtil.verifyPassword("password", hash));
    }

    @Test
    public void testVerifyEmptyPasswordAgainstHash() {
        String hash = PasswordUtil.hashPassword("realPassword");
        assertFalse("Empty password should fail against a real hash",
                PasswordUtil.verifyPassword("", hash));
    }

    @Test
    public void testVerifyCorruptedHash() {
        assertFalse("Corrupted hash should fail verification",
                PasswordUtil.verifyPassword("password", "not-a-valid-base64-hash!!!"));
    }

    @Test
    public void testVerifyEmptyHash() {
        assertFalse("Empty hash should fail verification",
                PasswordUtil.verifyPassword("password", ""));
    }

    // Hash-Verify Roundtrip 

    @Test
    public void testHashVerifyRoundtripMultipleTimes() {
        String[] passwords = { "admin123", "P@ssw0rd!", "simple", "123456", "long_password_with_special_chars_!@#" };

        for (String password : passwords) {
            String hash = PasswordUtil.hashPassword(password);
            assertTrue("Roundtrip should work for: " + password,
                    PasswordUtil.verifyPassword(password, hash));
        }
    }

    @Test
    public void testHashProducesBase64Output() {
        String hash = PasswordUtil.hashPassword("testPassword");
        // Base64 characters: A-Z, a-z, 0-9, +, /, =
        assertTrue("Hash should be valid Base64",
                hash.matches("^[A-Za-z0-9+/=]+$"));
    }
}
