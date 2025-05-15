/**
 * Job Portal application - A MongoDB-based job listing and application platform.
 *
 * <p>This is the root package of the Job Portal application, a Spring Boot application that
 * provides a complete system for job seekers and recruiters to connect. The application
 * uses MongoDB for data storage and offers both REST API endpoints and a command-line
 * interface.</p>
 *
 * <p>Key features of the application include:</p>
 * <ul>
 *   <li>User authentication and authorization with JWT</li>
 *   <li>Role-based access control (candidates, recruiters, admins)</li>
 *   <li>Job posting and browsing functionality</li>
 *   <li>Candidate profile management</li>
 *   <li>Recruiter company profile management</li>
 *   <li>Job application submission and tracking</li>
 *   <li>Application status management</li>
 *   <li>Search capabilities for jobs and candidates</li>
 * </ul>
 *
 * <p>The application is organized into the following subpackages:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.cli} - Command-line interface components</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.config} - Application configuration classes</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller} - REST API controllers</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto} - Data Transfer Objects</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.exception} - Exception handling</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model} - Data model classes</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.repository} - Data access interfaces</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.security} - Security components</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.service} - Business logic services</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.util} - Utility classes</li>
 * </ul>
 *
 * <p>The main class of the application is
 * {@link me.josephsf.jobportaljosephsfeir.JobPortalJosephSfeirApplication}, which sets up
 * the Spring Boot application context and initiates the embedded web server.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir;