/**
 * Provides security configuration and authentication components for the Job Portal application.
 *
 * <p>This package contains classes that implement authentication and authorization mechanisms
 * using Spring Security and JWT (JSON Web Tokens). It follows a token-based authentication approach
 * where users are issued JWT tokens upon successful authentication, which they include in
 * subsequent requests to access protected resources.</p>
 *
 * <p>The key components in this package include:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.security.JwtUtils} -
 *       Utility class for creating, validating, and parsing JWT tokens</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter} -
 *       Filter that intercepts requests to validate JWT tokens and establish security context</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl} -
 *       Implementation of Spring Security's UserDetailsService for loading user data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.security.UserPrincipal} -
 *       Implementation of Spring Security's UserDetails interface representing authenticated users</li>
 * </ul>
 *
 * <p>The security flow is as follows:</p>
 * <ol>
 *   <li>A user authenticates by sending credentials to the authentication endpoint</li>
 *   <li>Upon successful authentication, a JWT token is generated and returned to the client</li>
 *   <li>The client includes this token in the Authorization header of subsequent requests</li>
 *   <li>The JwtAuthTokenFilter validates the token and sets up the security context</li>
 *   <li>Protected endpoints use method security annotations to enforce authorization</li>
 * </ol>
 *
 * <p>This package also works with the security configuration in the config package,
 * particularly with {@link me.josephsf.jobportaljosephsfeir.config.SecurityConfig}, which sets up
 * the security filter chain and defines security rules for different URL patterns.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir.security;