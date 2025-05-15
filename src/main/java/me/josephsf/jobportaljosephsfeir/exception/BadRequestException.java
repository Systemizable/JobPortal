package me.josephsf.jobportaljosephsfeir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a client sends a request with invalid parameters or data.
 *
 * <p>This exception is automatically mapped to an HTTP 400 BAD_REQUEST response status
 * by Spring using the {@link ResponseStatus} annotation. It's used throughout the
 * Job Portal application when validation fails or when the client provides inappropriate
 * values that cannot be processed by the application.</p>
 *
 * <p>Examples of when this exception might be thrown:</p>
 * <ul>
 *   <li>User tries to register with an email that's already in use</li>
 *   <li>Candidate applies to a job they've already applied to</li>
 *   <li>Recruiter creates a job with invalid salary range</li>
 *   <li>User submits data that fails validation rules</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new bad request exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new bad request exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}