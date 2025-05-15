package me.josephsf.jobportaljosephsfeir.dto;

import java.util.List;

/**
 * Data Transfer Object for JWT authentication response.
 *
 * <p>This class encapsulates the information returned to the client after
 * successful authentication. It includes the JWT token, user identification
 * details, and assigned roles.</p>
 *
 * <p>The response includes:</p>
 * <ul>
 *   <li>JWT access token for authenticating subsequent requests</li>
 *   <li>Token type (always "Bearer")</li>
 *   <li>User ID from the database</li>
 *   <li>Username used for authentication</li>
 *   <li>User email</li>
 *   <li>List of user roles</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class JwtResponseDto {
    /** JWT access token */
    private String token;

    /** Token type, always "Bearer" */
    private String type = "Bearer";

    /** MongoDB user ID */
    private String id;

    /** Username used for authentication */
    private String username;

    /** User email address */
    private String email;

    /** List of user roles (e.g., CANDIDATE, RECRUITER, ADMIN) */
    private List<String> roles;

    /**
     * Constructs a new JwtResponseDto with all required fields.
     *
     * @param accessToken JWT access token
     * @param id MongoDB user ID
     * @param username Username used for authentication
     * @param email User email address
     * @param roles List of user roles
     */
    public JwtResponseDto(String accessToken, String id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    /**
     * Gets the JWT access token.
     *
     * @return The JWT access token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * Sets the JWT access token.
     *
     * @param accessToken The JWT access token
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     * Gets the token type, always "Bearer".
     *
     * @return The token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * Sets the token type.
     *
     * @param tokenType The token type
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     * Gets the MongoDB user ID.
     *
     * @return The user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the MongoDB user ID.
     *
     * @param id The user ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the user email address.
     *
     * @return The user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email address.
     *
     * @param email The user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the username used for authentication.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the list of user roles.
     *
     * @return List of roles as strings
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Sets the list of user roles.
     *
     * @param roles List of roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}