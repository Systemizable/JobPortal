package me.josephsf.jobportaljosephsfeir.config;

import me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter;
import me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Job Portal application.
 * <p>
 * This class configures Spring Security settings including authentication,
 * authorization, JWT token filtering, password encoding, and endpoint access rules.
 * It establishes a stateless authentication system using JWT tokens and defines
 * which API endpoints are public and which require authentication.
 * </p>
 * <p>
 * The configuration enables method-level security annotations, disables CSRF
 * protection (as we use JWT tokens), and sets up the security filter chain.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * Constructs a new SecurityConfig with required dependencies.
     *
     * @param userDetailsService The service for loading user-specific data
     * @param unauthorizedHandler The entry point for handling unauthorized access attempts
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    /**
     * Creates a JWT authentication token filter bean.
     * <p>
     * This filter intercepts and processes JWT tokens from incoming requests,
     * validating them and setting up the security context if the token is valid.
     * </p>
     *
     * @return A new JWT authentication token filter
     */
    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    /**
     * Creates a password encoder bean for securely hashing passwords.
     * <p>
     * This encoder is used for both validating existing passwords during
     * authentication and encoding new passwords during user registration.
     * </p>
     *
     * @return A BCrypt password encoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an authentication provider bean that uses the UserDetailsService
     * and password encoder for authenticating users.
     *
     * @return The configured authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an authentication manager bean from the authentication configuration.
     *
     * @param authConfig The authentication configuration
     * @return The authentication manager
     * @throws Exception If an error occurs when creating the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain with authorization rules.
     * <p>
     * This method defines which endpoints are accessible without authentication
     * and which require authentication. It configures JWT-based stateless authentication
     * and sets up exception handling for unauthorized access.
     * </p>
     *
     * @param http The HTTP security configuration object
     * @return The configured security filter chain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Static resources
                        .requestMatchers("/", "/index.html", "/candidate-dashboard.html", "/recruiter-dashboard.html").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // API endpoints open to all (only GET requests to /api/jobs)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()  // allow GETs only

                        // Health check endpoint
                        .requestMatchers("/actuator/health").permitAll()

                        // Error page
                        .requestMatchers("/error").permitAll()

                        // All other requests need authentication
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}