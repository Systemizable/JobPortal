/**
 * Provides exception handling for the Job Portal application.
 *
 * <p>This package contains custom exception classes and a global exception handler
 * that provide a structured approach to handling errors throughout the application.
 * The exceptions defined here are used to create consistent, informative error responses
 * for the REST API endpoints.</p>
 *
 * <p>Key components in this package:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.exception.ResourceNotFoundException} -
 *       Thrown when a requested resource (job, user, application, etc.) cannot be found</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.exception.BadRequestException} -
 *       Thrown when a client sends an invalid or malformed request</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.exception.UnauthorizedException} -
 *       Thrown when authentication fails or a user lacks permission for an action</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.exception.GlobalExceptionHandler} -
 *       Central exception handler that converts exceptions to appropriate HTTP responses</li>
 * </ul>
 *
 * <p>These exception classes are used throughout the service layer and controllers
 * to provide clear, consistent error handling. The GlobalExceptionHandler ensures
 * that all exceptions (including system exceptions) are translated into well-formed
 * JSON responses with appropriate HTTP status codes.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * public Job getJobById(String id) {
 *     return jobRepository.findById(id)
 *         .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
 * }
 * </pre>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir.exception;