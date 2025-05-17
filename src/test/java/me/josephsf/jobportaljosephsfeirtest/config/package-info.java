/**
 * Tests for configuration classes in the Job Portal application.
 *
 * <p>This package contains test classes for the configuration components that set up
 * the infrastructure of the Job Portal system. These tests verify that configuration
 * classes correctly initialize the application.</p>
 *
 * <p>The configuration tests focus on:</p>
 * <ul>
 *   <li>MongoDB configuration and connection</li>
 *   <li>Security configuration and filter chain setup</li>
 *   <li>Bean initialization and dependency injection</li>
 *   <li>Property loading and environment setup</li>
 *   <li>Cross-origin resource sharing (CORS) configuration</li>
 * </ul>
 *
 * <p>Key test classes in this package:</p>
 * <ul>
 *   <li>{@code CustomMongoConfigTest} - Tests for MongoDB configuration</li>
 *   <li>{@code SecurityConfigTest} - Tests for security configuration</li>
 *   <li>{@code JwtAuthenticationEntryPointTest} - Tests for authentication entry point</li>
 * </ul>
 *
 * <p>These tests ensure that the application initializes correctly with the appropriate
 * configuration settings and infrastructure components.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeirtest.config;