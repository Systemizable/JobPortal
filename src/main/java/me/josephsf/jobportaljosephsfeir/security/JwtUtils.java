package me.josephsf.jobportaljosephsfeir.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * <p>
 * This component provides functionality for JWT token generation, validation, and parsing.
 * It uses the JJWT library to create and verify JSON Web Tokens used for authentication
 * and authorization throughout the application. The tokens are signed with HMAC-SHA512
 * for security.
 * </p>
 *
 * <p>Key capabilities include:</p>
 * <ul>
 *   <li>Token generation for authenticated users</li>
 *   <li>Token validation to verify integrity and expiration</li>
 *   <li>Extraction of username from tokens for authentication</li>
 *   <li>Configurable secret key and token expiration time</li>
 * </ul>
 *
 * <p>This class is configured via properties that can be set in application.properties:</p>
 * <ul>
 *   <li>jobportal.app.jwtSecret - The secret key used for signing tokens</li>
 *   <li>jobportal.app.jwtExpirationMs - The token validity period in milliseconds</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Component
public class JwtUtils {

    /**
     * Secret key used for signing JWT tokens.
     * Value is loaded from application properties with a default fallback.
     */
    @Value("${jobportal.app.jwtSecret:mySecretKey123456789012345678901234567890123456789012345678901234567890}")
    private String jwtSecret;

    /**
     * Expiration time for JWT tokens in milliseconds.
     * Default is 24 hours (86,400,000 ms) if not specified in application properties.
     */
    @Value("${jobportal.app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs;

    /**
     * Constructs a new JwtUtils instance.
     * <p>
     * Default constructor that allows Spring to initialize this component
     * and inject the necessary property values from the application configuration.
     * </p>
     */
    public JwtUtils() {
        // Default constructor for Spring to initialize and inject properties
    }

    /**
     * Generates a JWT token for a user.
     * <p>
     * Creates a new signed token containing the username as subject,
     * issue time, and expiration time. The token is signed with HMAC-SHA512
     * using the configured secret key.
     * </p>
     *
     * @param username the username to include in the token
     * @return the JWT token string
     */
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Creates a signing key from the JWT secret.
     * <p>
     * This method converts the string secret key into a cryptographic key
     * suitable for HMAC-SHA algorithms.
     * </p>
     *
     * @return the Key object for token signing and verification
     */
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Extracts the username from a JWT token.
     * <p>
     * Parses the token, verifies its signature using the signing key,
     * and extracts the subject claim which contains the username.
     * </p>
     *
     * @param token the JWT token string
     * @return the username from the token
     * @throws JwtException if the token is invalid or cannot be parsed
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token.
     * <p>
     * Checks if the token is well-formed, has a valid signature,
     * and has not expired. Logs specific errors to help with debugging.
     * </p>
     *
     * @param authToken the JWT token string to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}