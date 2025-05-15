package me.josephsf.jobportaljosephsfeir.model;

/**
 * Enum representing the different user roles in the Job Portal system.
 * <p>
 * This enum is used to define the various permission levels within the application.
 * Each user in the system is assigned one or more of these roles, which determines
 * the actions they are authorized to perform.
 * </p>
 *
 * <p>Available roles:</p>
 * <ul>
 *   <li>{@code CANDIDATE} - Regular job-seeking users who can browse jobs and submit applications</li>
 *   <li>{@code RECRUITER} - Users representing companies who can post jobs and review applications</li>
 *   <li>{@code ADMIN} - System administrators with full access to all features and data</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
public enum Role {
    /**
     * Role for job seekers who can browse jobs and submit applications.
     */
    CANDIDATE,

    /**
     * Role for company representatives who can post jobs and review applications.
     */
    RECRUITER,

    /**
     * Role for system administrators with full access to all features.
     */
    ADMIN
}