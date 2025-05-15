/**
 * REST controllers for the Job Portal application.
 * <p>
 * This package contains the REST controllers that handle HTTP requests and manage the
 * interaction between clients and the application's business logic. The controllers
 * in this package expose RESTful endpoints for various entities in the Job Portal system:
 * </p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller.AuthController} - Handles user authentication and registration</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller.JobController} - Manages job posting operations</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller.CandidateController} - Manages candidate profile operations</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller.RecruiterController} - Manages recruiter profile operations</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.controller.ApplicationController} - Manages job application operations</li>
 * </ul>
 * <p>
 * The controllers implement a RESTful API design, following best practices such as:
 * </p>
 * <ul>
 *   <li>Using appropriate HTTP methods (GET, POST, PUT, DELETE) for different operations</li>
 *   <li>Implementing proper authorization using Spring Security annotations</li>
 *   <li>Returning meaningful HTTP status codes and response bodies</li>
 *   <li>Supporting pagination, sorting, and filtering for list operations</li>
 *   <li>Using DTOs for request/response data validation and transformation</li>
 * </ul>
 * <p>
 * Most endpoints in this package require authentication through JWT tokens,
 * with specific role-based authorization requirements for protected operations.
 * Public endpoints like job browsing and searching are accessible without authentication.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
package me.josephsf.jobportaljosephsfeir.controller;