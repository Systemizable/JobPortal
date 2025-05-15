/**
 * Provides repository interfaces for data access in the Job Portal application.
 *
 * <p>This package contains Spring Data MongoDB repository interfaces that define
 * data access operations for the application's domain model. These repositories
 * extend Spring Data's {@link org.springframework.data.mongodb.repository.MongoRepository}
 * interface to provide built-in CRUD operations and support for custom queries.</p>
 *
 * <p>The key repositories in this package include:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository.UserRepository} -
 *       For user account management and authentication</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository.CandidateRepository} -
 *       For candidate profile management and searching</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository.RecruiterRepository} -
 *       For recruiter profile management</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository.JobRepository} -
 *       For job posting management and searching</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository} -
 *       For job application tracking and management</li>
 * </ul>
 *
 * <p>These repositories leverage several approaches to define query methods:</p>
 * <ul>
 *   <li>Method name conventions for simple queries (e.g., findByUsername)</li>
 *   <li>The @Query annotation for complex MongoDB queries</li>
 *   <li>Support for pagination and sorting via Pageable parameters</li>
 *   <li>Support for custom query methods that return counts and aggregations</li>
 * </ul>
 *
 * <p>The repositories in this package form the data access layer of the application,
 * providing a clean separation between the business logic in the service layer and
 * the underlying MongoDB database. They are used by the corresponding service classes
 * to implement business operations while abstracting away the details of database access.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir.repository;