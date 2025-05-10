package me.josephsf.jobportaljosephsfeir.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateUtil {

    // Prevent instantiation
    private DateUtil() {
        throw new AssertionError("DateUtil class should not be instantiated");
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);

    /**
     * Format LocalDate to string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Parse string to LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Parse string to LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Get current date and time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculate years between two dates
     */
    public static long yearsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

    /**
     * Check if date is in the past
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(getCurrentDate());
    }

    /**
     * Check if date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(getCurrentDate());
    }

    /**
     * Add days to date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * Add months to date
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * Add years to date
     */
    public static LocalDate addYears(LocalDate date, long years) {
        if (date == null) {
            return null;
        }
        return date.plusYears(years);
    }

    /**
     * Get date N days ago
     */
    public static LocalDate getDaysAgo(int days) {
        return getCurrentDate().minusDays(days);
    }

    /**
     * Get date time N days ago
     */
    public static LocalDateTime getDateTimeAgo(int days) {
        return getCurrentDateTime().minusDays(days);
    }
}