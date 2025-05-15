package me.josephsf.jobportaljosephsfeir.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for validating tokens in requests.
 * <p>
 * This filter intercepts incoming HTTP requests to validate JWT tokens present in
 * the request headers. If a valid token is found, it sets up the Spring Security
 * context with an authenticated user. This enables stateless authentication for
 * RESTful API endpoints.
 * </p>
 *
 * <p>The filter performs the following operations:</p>
 * <ul>
 *   <li>Extracts the JWT token from the Authorization header</li>
 *   <li>Validates the token using JwtUtils</li>
 *   <li>Loads user details based on the username from the token</li>
 *   <li>Creates an authentication object and sets it in the security context</li>
 * </ul>
 *
 * <p>This class extends Spring's OncePerRequestFilter to ensure the filter is
 * executed only once per request. It is part of the security configuration and
 * is added to the filter chain to process requests before they reach the controllers.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @see me.josephsf.jobportaljosephsfeir.security.JwtUtils
 */
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    /**
     * Utility for JWT token operations.
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Service for loading user details.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Constructs a new JWT authentication filter.
     * <p>
     * This default constructor initializes the filter without specific configuration.
     * The required dependencies (jwtUtils and userDetailsService) are autowired by Spring.
     * </p>
     */
    public JwtAuthTokenFilter() {
        // Default constructor using autowired dependencies
    }

    /**
     * Processes an HTTP request to authenticate and authorize users.
     * <p>
     * This method is called for each HTTP request passing through the filter chain.
     * It examines the Authorization header for a JWT token, validates it, and if valid,
     * sets up the security context with an authenticated user.
     * </p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain for executing other filters
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt(request);

        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header.
     * <p>
     * Parses the HTTP Authorization header looking for a Bearer token.
     * The format expected is "Bearer [token]".
     * </p>
     *
     * @param request the HTTP request
     * @return the JWT token or null if not found
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}