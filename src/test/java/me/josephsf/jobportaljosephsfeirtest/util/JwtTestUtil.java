package me.josephsf.jobportaljosephsfeirtest.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating JWT tokens for testing purposes.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
public class JwtTestUtil {

    private static final String SECRET_KEY = "testSecretKeyForUnitTestingPurposesOnlyDoNotUseInProduction";
    private static final int EXPIRATION_MS = 3600000; // 1 hour

    /**
     * Generates a test JWT token for the specified username and roles.
     *
     * @param username the username for the token
     * @param roles the roles to include in the token
     * @return a JWT token string
     */
    public static String generateToken(String username, String... roles) {
        // Create authorities
        List<GrantedAuthority> authorities = List.of(roles).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // Create user and authentication
        User user = new User(username, "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        // Generate token
        return createToken(authentication);
    }

    /**
     * Creates a JWT token from an authentication object.
     *
     * @param authentication the authentication object
     * @return the JWT token string
     */
    private static String createToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
