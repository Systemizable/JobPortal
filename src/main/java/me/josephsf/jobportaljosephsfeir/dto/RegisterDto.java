package me.josephsf.jobportaljosephsfeir.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Data Transfer Object for user registration.
 *
 * <p>This class encapsulates the information required for registering a new user
 * in the Job Portal system. It includes fields for username, email, password,
 * and the user's requested role(s). Validation constraints ensure data integrity
 * during the registration process.</p>
 *
 * <p>The validation constraints include:</p>
 * <ul>
 *   <li>Username must be between 3 and 20 characters</li>
 *   <li>Email must be valid and not exceed 50 characters</li>
 *   <li>Password must be between 6 and 40 characters</li>
 * </ul>
 *
 * <p>If no role is specified during registration, the system defaults the user to
 * the CANDIDATE role.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class RegisterDto {
    /** Username for the new account, with validation constraints */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /** Email address for the new account, with validation constraints */
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /** Password for the new account, with validation constraints */
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    /**
     * Set of roles for the new account.
     * Valid values are "candidate", "recruiter", and "admin".
     * If null or empty, defaults to "candidate".
     */
    private Set<String> role;

    /**
     * Default constructor for deserialization.
     */
    public RegisterDto() {}

    /**
     * Constructs a new RegisterDto with all required fields.
     *
     * @param username The username for the new account
     * @param email The email address for the new account
     * @param password The password for the new account
     * @param role The set of roles for the new account
     */
    public RegisterDto(String username, String email, String password, Set<String> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
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
     * Gets the password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the set of roles.
     *
     * @return The set of roles
     */
    public Set<String> getRole() {
        return role;
    }

    /**
     * Sets the set of roles.
     *
     * @param role The set of roles to set
     */
    public void setRole(Set<String> role) {
        this.role = role;
    }
}