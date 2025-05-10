package me.josephsf.jobportaljosephsfeir.util;

import java.util.regex.Pattern;

public final class StringUtil {

    // Prevent instantiation
    private StringUtil() {
        throw new AssertionError("StringUtil class should not be instantiated");
    }

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Phone number pattern (supports various formats)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?\\d{3}[)]?[-\\s\\.]?\\d{3}[-\\s\\.]?\\d{4,6}$"
    );

    // LinkedIn URL pattern
    private static final Pattern LINKEDIN_PATTERN = Pattern.compile(
            "^(https?://)?([\\w]+\\.)?linkedin\\.com/(pub|in|profile)/([\\w-]+)(/?)$"
    );

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Validate LinkedIn URL
     */
    public static boolean isValidLinkedInUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return LINKEDIN_PATTERN.matcher(url).matches();
    }

    /**
     * Truncate string to specified length
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
     * Capitalize first letter of each word
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
     * Clean and normalize string
     */
    public static String clean(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.trim().replaceAll("\\s+", " ");
    }

    /**
     * Generate a slug from string (for URLs)
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
     * Mask email for privacy
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
     * Extract first name from full name
     */
    public static String extractFirstName(String fullName) {
        if (isEmpty(fullName)) {
            return "";
        }

        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }

    /**
     * Extract last name from full name
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