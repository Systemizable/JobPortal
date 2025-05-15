package me.josephsf.jobportaljosephsfeir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to access a resource without proper authentication
 * or with insufficient permissions.
 *
 * <p>This exception is automatically mapped to an HTTP 401 UNAUTHORIZED response status
 * by Spring using the {@link ResponseStatus} annotation. It's used throughout the
 * Job Portal application when authentication fails or when a user tries to perform
 * an action they are not authorized to perform.</p>
 *
 * <p>Examples of when this exception might be thrown:</p>
 * <ul>
 *   <li>User attempts to access a protected endpoint without a valid JWT token</li>
 *   <li>The JWT token has expired or is invalid</li>
 *   <li>A candidate tries to access recruiter-only endpoints</li>
 *   <li>A recruiter tries to access another recruiter's job postings</li>
 * </ul>
 *
 * <p>Note: This differs from AccessDeniedException which results in a 403 FORBIDDEN response
 * when a user is authenticated but explicitly not allowed to access a resource.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new unauthorized exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new unauthorized exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}