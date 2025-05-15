package me.josephsf.jobportaljosephsfeir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found in the database or system.
 *
 * <p>This exception is automatically mapped to an HTTP 404 NOT_FOUND response status
 * by Spring using the {@link ResponseStatus} annotation. It's used throughout the
 * Job Portal application when entities like jobs, candidates, recruiters, or applications
 * cannot be found by their ID or other identifying attributes.</p>
 *
 * <p>Examples of when this exception might be thrown:</p>
 * <ul>
 *   <li>User tries to view a job posting that has been deleted or doesn't exist</li>
 *   <li>Candidate tries to access a non-existent application</li>
 *   <li>Recruiter tries to update a profile that has been removed</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new resource not found exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new resource not found exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new resource not found exception with a formatted message containing
     * the resource name, field name, and field value that caused the exception.
     *
     * @param resourceName the name of the resource type that was not found (e.g., "Job", "Candidate")
     * @param fieldName the name of the field used to search for the resource (e.g., "id", "userId")
     * @param fieldValue the value of the field that was used in the search
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}