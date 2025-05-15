/**
 * Provides utility classes for the Job Portal application.
 *
 * <p>This package contains utility classes that provide helper methods and constants
 * used throughout the application. These utilities encapsulate common functionality
 * related to string manipulation, date operations, and application-wide constants.</p>
 *
 * <p>Key components in this package:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.util.Constants} -
 *       Defines application-wide constants, including enum-like values for status codes,
 *       roles, error messages, and configuration values</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.util.DateUtil} -
 *       Provides helper methods for date formatting, parsing, comparison, and manipulation</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.util.StringUtil} -
 *       Offers string validation, formatting, and transformation utilities</li>
 * </ul>
 *
 * <p>These utility classes follow these design principles:</p>
 * <ul>
 *   <li>All classes are final with private constructors to prevent instantiation</li>
 *   <li>All methods are static and do not maintain state</li>
 *   <li>Null-safety is ensured throughout with appropriate null checks</li>
 *   <li>Methods are designed to be reusable across different parts of the application</li>
 *   <li>Consistent naming conventions are used for similar types of operations</li>
 * </ul>
 *
 * <p>Example usage of DateUtil:</p>
 * <pre>
 * {@code
 * // Format a date for display
 * LocalDate applicationDate = job.getPostedDate().toLocalDate();
 * String formattedDate = DateUtil.formatDate(applicationDate);
 *
 * // Check if an application deadline has passed
 * if (DateUtil.isPast(job.getDeadline())) {
 *     throw new BadRequestException("Job application deadline has passed");
 * }
 * }
 * </pre>
 *
 * <p>Example usage of StringUtil:</p>
 * <pre>
 * {@code
 * // Validate an email address
 * if (!StringUtil.isValidEmail(userDto.getEmail())) {
 *     throw new BadRequestException("Invalid email format");
 * }
 *
 * // Create a URL-friendly slug for a job title
 * String jobSlug = StringUtil.slugify(jobDto.getTitle());
 * }
 * </pre>
 *
 * <p>Example usage of Constants:</p>
 * <pre>
 * {@code
 * // Check application status
 * if (application.getStatus().equals(Constants.APPLICATION_STATUS_ACCEPTED)) {
 *     // Send congratulatory email to candidate
 * }
 *
 * // Use standard error message
 * throw new ResourceNotFoundException(Constants.JOB_NOT_FOUND);
 * }
 * </pre>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir.util;