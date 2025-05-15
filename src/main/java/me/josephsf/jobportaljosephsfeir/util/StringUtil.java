package me.josephsf.jobportaljosephsfeir.util;

import java.util.regex.Pattern;

/**
 * Utility class for string manipulation and validation in the Job Portal system.
 *
 * <p>This class provides static methods for common string operations such as
 * validation, formatting, transformation, and checking. It includes utilities for
 * validating emails, phone numbers, and URLs, as well as various string manipulation
 * methods.</p>
 *
 * <p>The class is final with a private constructor to prevent instantiation,
 * as it only contains static utility methods.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
public final class StringUtil {

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws AssertionError if an attempt is made to instantiate this class
     */
    private StringUtil() {
        throw new AssertionError("StringUtil class should not be instantiated");
    }

    /**
     * Regular expression pattern for validating email addresses.
     * Follows standard email format rules.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    /**
     * Regular expression pattern for validating phone numbers.
     * Supports various international formats.
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?\\d{3}[)]?[-\\s\\.]?\\d{3}[-\\s\\.]?\\d{4,6}$"
    );

    /**
     * Regular expression pattern for validating LinkedIn URLs.
     * Supports various LinkedIn profile URL formats.
     */
    private static final Pattern LINKEDIN_PATTERN = Pattern.compile(
            "^(https?://)?([\\w]+\\.)?linkedin\\.com/(pub|in|profile)/([\\w-]+)(/?)$"
    );

    /**
     * Checks if a string is null or empty (contains only whitespace).
     *
     * @param str The string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param str The string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Validates if a string is a properly formatted email address.
     *
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a string is a properly formatted phone number.
     *
     * @param phoneNumber The phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Validates if a string is a properly formatted LinkedIn URL.
     *
     * @param url The LinkedIn URL to validate
     * @return true if the URL is a valid LinkedIn profile URL, false otherwise
     */
    public static boolean isValidLinkedInUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return LINKEDIN_PATTERN.matcher(url).matches();
    }

    /**
     * Truncates a string to a specified maximum length, adding an ellipsis if truncated.
     *
     * @param str The string to truncate
     * @param maxLength The maximum length for the string
     * @return The truncated string, or the original if it was already shorter than maxLength
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Converts a string to title case (first letter of each word capitalized).
     *
     * @param str The string to convert
     * @return The string in title case
     */
    public static String toTitleCase(String str) {
        if (isEmpty(str)) {
            return str;
        }

        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }

        return result.toString();
    }

    /**
     * Cleans and normalizes a string by trimming and reducing multiple spaces to single spaces.
     *
     * @param str The string to clean
     * @return The cleaned string
     */
    public static String clean(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.trim().replaceAll("\\s+", " ");
    }

    /**
     * Generates a URL-friendly slug from a string.
     * Converts to lowercase, replaces non-alphanumeric characters with hyphens,
     * and removes leading and trailing hyphens.
     *
     * @param str The string to convert to a slug
     * @return The slugified string
     */
    public static String slugify(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Masks an email address for privacy by showing only the first and last characters
     * of the local part (before the @ symbol).
     *
     * @param email The email address to mask
     * @return The masked email address
     */
    public static String maskEmail(String email) {
        if (isEmpty(email)) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return email;
        }

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (local.length() <= 3) {
            return local.charAt(0) + "****" + domain;
        }

        return local.charAt(0) + "*".repeat(local.length() - 2) + local.charAt(local.length() - 1) + domain;
    }

    /**
     * Extracts the first name from a full name string.
     *
     * @param fullName The full name
     * @return The first name, or an empty string if the input is null or empty
     */
    public static String extractFirstName(String fullName) {
        if (isEmpty(fullName)) {
            return "";
        }

        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }

    /**
     * Extracts the last name from a full name string.
     *
     * @param fullName The full name
     * @return The last name, or an empty string if the input is null, empty, or contains only one word
     */
    public static String extractLastName(String fullName) {
        if (isEmpty(fullName)) {
            return "";
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return "";
    }
}