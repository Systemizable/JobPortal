package me.josephsf.jobportaljosephsfeir.exception;

import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that provides centralized exception handling across all
 * controllers in the Job Portal application.
 *
 * <p>This class uses the {@link RestControllerAdvice} annotation to capture exceptions
 * thrown from any controller in the application and convert them into standardized
 * HTTP responses with appropriate status codes and structured error messages.</p>
 *
 * <p>The handler manages various types of exceptions:</p>
 * <ul>
 *   <li>Custom application exceptions like {@link ResourceNotFoundException}</li>
 *   <li>Security-related exceptions from Spring Security</li>
 *   <li>Validation failures from Bean Validation</li>
 *   <li>Generic and unexpected exceptions</li>
 * </ul>
 *
 * <p>Each exception type is mapped to an appropriate HTTP status code and
 * formatted into a consistent error response structure to provide clear
 * error information to API clients.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Constructs a new GlobalExceptionHandler instance.
     * <p>
     * This default constructor initializes the exception handler with no specific
     * configuration as it relies on Spring's annotation-based exception handling.
     * </p>
     */
    public GlobalExceptionHandler() {
        // Default constructor for Spring's annotation-based exception handling
    }

    /**
     * Handles ResourceNotFoundException, returning a 404 NOT_FOUND response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 404 status code
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException, returning a 400 BAD_REQUEST response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 400 status code
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UnauthorizedException, returning a 401 UNAUTHORIZED response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 401 status code
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles Spring Security's AccessDeniedException, returning a 403 FORBIDDEN response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 403 status code
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                "You don't have permission to access this resource",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles Spring Security's BadCredentialsException, returning a 401 UNAUTHORIZED response.
     * Used for invalid login attempts.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing error details and 401 status code
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDto(false, "Invalid username or password"));
    }

    /**
     * Handles validation failures from @Valid annotations on request bodies,
     * returning a 400 BAD_REQUEST response with details of each validation failure.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing validation error details and 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Invalid input");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for any unhandled exceptions, returning a 500 INTERNAL_SERVER_ERROR response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 500 status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles IllegalArgumentException, returning a 400 BAD_REQUEST response.
     *
     * @param ex the exception that was thrown
     * @param request the current request
     * @return a ResponseEntity containing error details and 400 status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Argument",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Standardized error response structure used across all exception handlers.
     * Contains timestamp, HTTP status, error type, detailed message, and request path.
     */
    static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        /**
         * Constructs a new standardized error response.
         *
         * @param status the HTTP status code
         * @param error the general error type
         * @param message the detailed error message
         * @param path the request path that triggered the error
         */
        public ErrorResponse(int status, String error, String message, String path) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path.replace("uri=", "");
        }

        /**
         * Gets the timestamp when the error occurred.
         * @return the timestamp
         */
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        /**
         * Gets the HTTP status code.
         * @return the status code
         */
        public int getStatus() {
            return status;
        }

        /**
         * Gets the general error type.
         * @return the error type
         */
        public String getError() {
            return error;
        }

        /**
         * Gets the detailed error message.
         * @return the error message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Gets the request path that triggered the error.
         * @return the request path
         */
        public String getPath() {
            return path;
        }
    }
}