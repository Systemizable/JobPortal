package me.josephsf.jobportaljosephsfeir.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.josephsf.jobportaljosephsfeir.model.Role;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object for user information.
 *
 * <p>This class encapsulates user account information in the Job Portal system,
 * excluding sensitive information like passwords. It is primarily used for
 * transferring user data between the service layer and the presentation layer,
 * particularly when displaying user profiles or lists of users.</p>
 *
 * <p>The class includes validation constraints to ensure data integrity when
 * updating user information:</p>
 * <ul>
 *   <li>Username must be between 3 and 20 characters</li>
 *   <li>Email must be valid and not exceed 50 characters</li>
 * </ul>
 *
 * <p>The DTO also tracks the roles assigned to the user, which determine their
 * access levels and permissions within the system.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class UserDto {
    /** MongoDB document ID */
    private String id;

    /** Username with validation constraints */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /** Email address with validation constraints */
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /** Set of roles assigned to the user (CANDIDATE, RECRUITER, ADMIN) */
    private Set<Role> roles;

    /** Timestamp when the user account was created */
    private LocalDateTime createdAt;

    /** Timestamp when the user account was last updated */
    private LocalDateTime updatedAt;

    /**
     * Default constructor for deserialization.
     */
    public UserDto() {}

    /**
     * Constructs a new UserDto with essential fields.
     *
     * @param id The MongoDB document ID
     * @param username The username
     * @param email The email address
     * @param roles The set of roles assigned to the user
     */
    public UserDto(String id, String username, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    /**
     * Gets the MongoDB document ID.
     *
     * @return The document ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the MongoDB document ID.
     *
     * @param id The document ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the set of roles.
     *
     * @return The set of roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the set of roles.
     *
     * @param roles The set of roles to set
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt The last update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}