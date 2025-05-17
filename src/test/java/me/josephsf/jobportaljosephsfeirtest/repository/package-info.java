/**
 * Tests for repository interfaces in the Job Portal application.
 *
 * <p>This package contains test classes for the repository interfaces that handle
 * data access operations in the Job Portal system. These tests verify that repositories
 * correctly interact with the MongoDB database and return appropriate data.</p>
 *
 * <p>The repository tests focus on:</p>
 * <ul>
 *   <li>CRUD operations (Create, Read, Update, Delete)</li>
 *   <li>Custom query methods</li>
 *   <li>Query parameters and sorting</li>
 *   <li>Pagination functionality</li>
 *   <li>Data filtering and searching</li>
 * </ul>
 *
 * <p>Key test classes in this package:</p>
 * <ul>
 *   <li>{@code UserRepositoryTest} - Tests for user data access</li>
 *   <li>{@code JobRepositoryTest} - Tests for job posting data access</li>
 *   <li>{@code CandidateRepositoryTest} - Tests for candidate profile data access</li>
 *   <li>{@code RecruiterRepositoryTest} - Tests for recruiter profile data access</li>
 *   <li>{@code ApplicationRepositoryTest} - Tests for job application data access</li>
 * </ul>
 *
 * <p>Tests in this package use Embedded MongoDB for integration testing, allowing
 * repository operations to be tested against a real MongoDB instance without external
 * dependencies.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeirtest.repository;