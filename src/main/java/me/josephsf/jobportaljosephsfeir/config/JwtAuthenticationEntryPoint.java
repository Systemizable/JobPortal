package me.josephsf.jobportaljosephsfeir.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication entry point for handling unauthorized access attempts in the Job Portal application.
 * <p>
 * This component implements Spring Security's {@link AuthenticationEntryPoint} interface to
 * provide a custom response when an unauthenticated user attempts to access a protected resource.
 * Instead of redirecting to a login page, it returns a JSON response with error details.
 * </p>
 * <p>
 * When authentication fails or a user attempts to access a secured endpoint without proper
 * authentication, this entry point is triggered to handle the exception and format the response.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Constructs a new JWT authentication entry point.
     * <p>
     * This default constructor initializes the entry point with no specific
     * configuration. The entry point will format unauthorized errors as JSON
     * responses with HTTP 401 status code.
     * </p>
     */
    public JwtAuthenticationEntryPoint() {
        // Default constructor
    }

    /**
     * Handles an authentication failure by returning a structured JSON error response.
     * <p>
     * This method is called by Spring Security when an {@link AuthenticationException} is thrown
     * during the authentication process or when an unauthenticated user attempts to access a
     * secured resource. It creates a JSON response containing error details and sends it to the client.
     * </p>
     *
     * @param request The HTTP request that resulted in an authentication exception
     * @param response The HTTP response to be modified with error details
     * @param authException The authentication exception that triggered this method
     * @throws IOException If an I/O error occurs while writing the response
     * @throws ServletException If a servlet-related error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getRequestURI());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}