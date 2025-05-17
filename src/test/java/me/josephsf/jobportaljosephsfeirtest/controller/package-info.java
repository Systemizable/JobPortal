/**
 * Tests for REST controllers in the Job Portal application.
 *
 * <p>This package contains test classes for the REST controllers that handle HTTP requests
 * in the Job Portal system. These tests verify that the controllers correctly process
 * requests, interact with services, and return appropriate responses.</p>
 *
 * <p>The controller tests use Spring's MockMvc to simulate HTTP requests and verify:</p>
 * <ul>
 *   <li>Request mapping and URL handling</li>
 *   <li>Request parameter and body validation</li>
 *   <li>Response status codes and content</li>
 *   <li>Authentication and authorization requirements</li>
 *   <li>Exception handling and error responses</li>
 * </ul>
 *
 * <p>Key test classes in this package:</p>
 * <ul>
 *   <li>{@code AuthControllerTest} - Tests for authentication endpoints</li>
 *   <li>{@code JobControllerTest} - Tests for job posting management</li>
 *   <li>{@code CandidateControllerTest} - Tests for candidate profile operations</li>
 *   <li>{@code RecruiterControllerTest} - Tests for recruiter profile operations</li>
 *   <li>{@code ApplicationControllerTest} - Tests for job application processes</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeirtest.controller;