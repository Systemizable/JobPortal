/**
 * Provides model classes for the Job Portal application.
 *
 * <p>This package contains the domain model entities that represent the core business
 * objects in the Job Portal system. These classes are mapped to MongoDB collections
 * using Spring Data MongoDB annotations and are the foundation of the application's
 * data structure.</p>
 *
 * <p>The key entities in this package include:</p>
 * <ul>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.User} - Represents user accounts with authentication data</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Role} - Defines user roles and permissions</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Candidate} - Contains candidate profiles for job seekers</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Recruiter} - Contains recruiter profiles for employers</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Job} - Represents job postings with requirements and details</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.JobApplication} - Tracks applications submitted by candidates</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Experience} - Represents work history entries for candidates</li>
 *   <li>{@link me.josephsf.jobportaljosephsfeir.model.Education} - Represents educational background entries for candidates</li>
 * </ul>
 *
 * <p>The model classes follow a consistent pattern with:</p>
 * <ul>
 *   <li>MongoDB document mapping annotations</li>
 *   <li>Descriptive field definitions</li>
 *   <li>Appropriate constructors</li>
 *   <li>Complete getter and setter methods</li>
 *   <li>Audit fields for creation and modification timestamps</li>
 * </ul>
 *
 * <p>The relationships between entities are represented through document references
 * (typically by ID) following MongoDB best practices for document databases rather
 * than traditional relational database foreign keys.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
package me.josephsf.jobportaljosephsfeir.model;