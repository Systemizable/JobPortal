/**
 * Tests for service classes in the Job Portal application.
 *
 * <p>This package contains test classes for the service layer components that implement
 * the business logic of the Job Portal system. These tests verify that services correctly
 * process data, interact with repositories, and apply business rules.</p>
 *
 * <p>The service tests focus on:</p>
 * <ul>
 *   <li>Business logic validation</li>
 *   <li>Correct repository interaction</li>
 *   <li>Data transformation between DTOs and entities</li>
 *   <li>Error handling and exception propagation</li>
 *   <li>Transactional behavior</li>
 * </ul>
 *
 * <p>Key test classes in this package:</p>
 * <ul>
 *   <li>{@code UserServiceTest} - Tests for user account management</li>
 *   <li>{@code AuthServiceTest} - Tests for authentication and registration logic</li>
 *   <li>{@code JobServiceTest} - Tests for job posting business logic</li>
 *   <li>{@code CandidateServiceTest} - Tests for candidate profile management</li>
 *   <li>{@code RecruiterServiceTest} - Tests for recruiter profile management</li>
 *   <li>{@code ApplicationServiceTest} - Tests for job application business logic</li>
 * </ul>
 *
 * <p>Most tests in this package use Mockito to mock dependencies, allowing the service
 * logic to be tested in isolation.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeirtest.service;