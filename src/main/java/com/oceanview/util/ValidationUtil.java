package com.oceanview.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Comprehensive validation utility class
 * Provides input validation and sanitization methods used across the
 * application
 * Implements XSS prevention, format validation, and business rule validation
 */
public class ValidationUtil {

    /** Email regex pattern - RFC 5322 compliant simplified version */
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    /** Phone pattern - exactly 10 digits */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    /** Name pattern - letters, spaces, hyphens, apostrophes, 2-50 chars */
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z\\s'\\-]{1,49}$");

    /** Username pattern - alphanumeric + underscore, 3-50 chars */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");

    private ValidationUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Check if a string is null, empty, or whitespace-only
     * 
     * @param str the string to check
     * @return true if null, empty, or whitespace
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validate email format using regex pattern matching
     * 
     * @param email the email address to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number - must be exactly 10 digits
     * 
     * @param phone the phone number to validate
     * @return true if valid 10-digit phone
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validate a person's name - letters, spaces, 2-50 chars
     * 
     * @param name the name to validate
     * @return true if valid name format
     */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            return false;
        }
        return NAME_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Validate username format
     * 
     * @param username the username to validate
     * @return true if valid username
     */
    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validate password strength - minimum 6 chars
     * 
     * @param password the password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }

    /**
     * Validate date range for reservations
     * Check-in must be today or in future, check-out must be after check-in
     * 
     * @param checkIn  check-in date
     * @param checkOut check-out date
     * @return true if valid date range
     */
    public static boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        // Check-in must not be in the past
        if (checkIn.isBefore(today)) {
            return false;
        }
        // Check-out must be strictly after check-in
        if (!checkOut.isAfter(checkIn)) {
            return false;
        }
        return true;
    }

    /**
     * Sanitize user input to prevent XSS attacks
     * Escapes HTML special characters and trims whitespace
     * 
     * @param input the raw user input
     * @return sanitized string safe for HTML output
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        String trimmed = input.trim();
        // Escape HTML entities to prevent XSS
        trimmed = trimmed.replace("&", "&amp;");
        trimmed = trimmed.replace("<", "&lt;");
        trimmed = trimmed.replace(">", "&gt;");
        trimmed = trimmed.replace("\"", "&quot;");
        trimmed = trimmed.replace("'", "&#x27;");
        return trimmed;
    }

    /**
     * Validate positive integer
     * 
     * @param value the integer to check
     * @return true if value is positive
     */
    public static boolean isPositiveInt(int value) {
        return value > 0;
    }

    /**
     * Safely parse an integer from string
     * 
     * @param str          the string to parse
     * @param defaultValue fallback value if parsing fails
     * @return parsed integer or default value
     */
    public static int parseIntSafe(String str, int defaultValue) {
        if (isNullOrEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
