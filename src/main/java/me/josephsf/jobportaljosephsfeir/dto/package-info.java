/**
 * Provides Data Transfer Objects (DTOs) for the Job Portal application.
 *
 * <p>This package contains all the Data Transfer Objects used throughout the Job Portal system
 * to transfer data between different layers of the application (controller, service, repository)
 * and between the server and client. DTOs are immutable objects that only contain fields,
 * getters/setters, and validation constraints.</p>
 *
 * <p>The DTO classes in this package serve several purposes:</p>
 * <ul>
 *   <li>Decoupling the domain model from the presentation layer</li>
 *   <li>Handling input validation through Jakarta Bean Validation annotations</li>
 *   <li>Providing clear API contracts for data exchange</li>
 *   <li>Hiding implementation details of the domain model</li>
 * </ul>
 *
 * <p>Key DTOs include:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.CandidateDto} - For job seeker profile data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.RecruiterDto} - For company recruiter profile data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.JobDto} - For job listing information</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.ApplicationDto} - For job application data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.LoginDto} - For authentication credentials</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.RegisterDto} - For user registration data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.JwtResponseDto} - For authentication token responses</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto} - For standardized API responses</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.dto.UserDto} - For user account information</li>
 * </ul>
 *
 * <p>Most DTOs contain validation annotations from the Jakarta Bean Validation API to ensure
 * data integrity before processing requests:</p>
 * <ul>
 *   <li>@NotNull - Ensures a value is not null</li>
 *   <li>@NotBlank - Ensures a string value is not null, empty, or just whitespace</li>
 *   <li>@Size - Constrains the size of strings, collections, maps, and arrays</li>
 *   <li>@Min/@Max - Constrains numeric values</li>
 *   <li>@Email - Validates email format</li>
 * </ul>
 *
 * <p>Note that DTOs are distinct from domain model entities in the
 * {@link me.josephsf.jobportaljosephsfeir.model} package, which represent the database entities.
 * Conversion between DTOs and model entities is typically handled in the service layer.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
package me.josephsf.jobportaljosephsfeir.dto;