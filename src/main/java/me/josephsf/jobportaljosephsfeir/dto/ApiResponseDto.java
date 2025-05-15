package me.josephsf.jobportaljosephsfeir.dto;

/**
 * Data Transfer Object (DTO) for API response messages in the Job Portal application.
 * <p>
 * This class provides a standardized structure for API responses, particularly for
 * operations that don't return entity data but need to communicate success status
 * and descriptive messages to the client. It is commonly used for operations like
 * user registration, profile deletion, and other actions where the primary response
 * is a success/failure indication with an explanatory message.
 * </p>
 * <p>
 * The class contains two main fields:
 * </p>
 * <ul>
 *   <li>success - A boolean indicating whether the operation succeeded (true) or failed (false)</li>
 *   <li>message - A string providing details about the result of the operation</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class ApiResponseDto {
    private Boolean success;
    private String message;

    /**
     * Constructs a new API response with the specified success status and message.
     *
     * @param success Boolean indicating if the operation was successful
     * @param message Descriptive message providing details about the operation result
     */
    public ApiResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Gets the success status of the operation.
     *
     * @return true if the operation was successful, false otherwise
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * Sets the success status of the operation.
     *
     * @param success true if the operation was successful, false otherwise
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * Gets the descriptive message about the operation result.
     *
     * @return A string message providing details about the result
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the descriptive message about the operation result.
     *
     * @param message A string message providing details about the result
     */
    public void setMessage(String message) {
        this.message = message;
    }
}