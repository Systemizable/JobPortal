package me.josephsf.jobportaljosephsfeir.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user login credentials.
 *
 * <p>This class encapsulates the information required for authenticating a user
 * in the Job Portal system. It captures the username and password provided
 * during the login process and includes validation constraints to ensure
 * required fields are not empty.</p>
 *
 * <p>The validation constraints ensure that both username and password are provided
 * before the login attempt is processed by the authentication system.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class LoginDto {
    /** Username for authentication, cannot be blank */
    @NotBlank(message = "Username is required")
    private String username;

    /** Password for authentication, cannot be blank */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Default constructor for deserialization.
     */
    public LoginDto() {}

    /**
     * Constructs a new LoginDto with username and password.
     *
     * @param username The username for authentication
     * @param password The password for authentication
     */
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
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
}