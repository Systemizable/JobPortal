package me.josephsf.jobportaljosephsfeir.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date and time operations in the Job Portal system.
 *
 * <p>This class provides static methods for common date-related operations such as
 * formatting, parsing, comparing, and manipulating dates and times. It uses Java's
 * modern date-time API (java.time package) for all operations.</p>
 *
 * <p>The class is final with a private constructor to prevent instantiation,
 * as it only contains static utility methods.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
public final class DateUtil {

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws AssertionError if an attempt is made to instantiate this class
     */
    private DateUtil() {
        throw new AssertionError("DateUtil class should not be instantiated");
    }

    /** Formatter for dates using the pattern defined in Constants.DATE_FORMAT */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /** Formatter for date-times using the pattern defined in Constants.DATE_TIME_FORMAT */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);

    /**
     * Formats a LocalDate to a string using the standard date format.
     *
     * @param date The LocalDate to format
     * @return The formatted date string, or null if the input is null
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats a LocalDateTime to a string using the standard date-time format.
     *
     * @param dateTime The LocalDateTime to format
     * @return The formatted date-time string, or null if the input is null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Parses a string into a LocalDate using the standard date format.
     *
     * @param dateString The string to parse
     * @return The parsed LocalDate, or null if the input is null or empty
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Parses a string into a LocalDateTime using the standard date-time format.
     *
     * @param dateTimeString The string to parse
     * @return The parsed LocalDateTime, or null if the input is null or empty
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    /**
     * Gets the current date.
     *
     * @return The current date as a LocalDate
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Gets the current date and time.
     *
     * @return The current date and time as a LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Calculates the number of days between two dates.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return The number of days between the dates, or 0 if either date is null
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculates the number of years between two dates.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return The number of years between the dates, or 0 if either date is null
     */
    public static long yearsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

    /**
     * Checks if a date is in the past.
     *
     * @param date The date to check
     * @return true if the date is in the past, false if it's today, in the future, or null
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(getCurrentDate());
    }

    /**
     * Checks if a date is in the future.
     *
     * @param date The date to check
     * @return true if the date is in the future, false if it's today, in the past, or null
     */
    public static boolean isFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(getCurrentDate());
    }

    /**
     * Adds a specified number of days to a date.
     *
     * @param date The base date
     * @param days The number of days to add
     * @return The resulting date, or null if the input date is null
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * Adds a specified number of months to a date.
     *
     * @param date The base date
     * @param months The number of months to add
     * @return The resulting date, or null if the input date is null
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * Adds a specified number of years to a date.
     *
     * @param date The base date
     * @param years The number of years to add
     * @return The resulting date, or null if the input date is null
     */
    public static LocalDate addYears(LocalDate date, long years) {
        if (date == null) {
            return null;
        }
        return date.plusYears(years);
    }

    /**
     * Gets a date that was a specified number of days ago.
     *
     * @param days The number of days ago
     * @return The date that occurred the specified number of days before the current date
     */
    public static LocalDate getDaysAgo(int days) {
        return getCurrentDate().minusDays(days);
    }

    /**
     * Gets a date-time that was a specified number of days ago.
     *
     * @param days The number of days ago
     * @return The date-time that occurred the specified number of days before the current date-time
     */
    public static LocalDateTime getDateTimeAgo(int days) {
        return getCurrentDateTime().minusDays(days);
    }
}