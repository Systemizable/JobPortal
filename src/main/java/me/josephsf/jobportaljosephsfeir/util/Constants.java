package me.josephsf.jobportaljosephsfeir.util;

/**
 * Constants class for the Job Portal application.
 *
 * <p>This class provides centralized storage for constant values used throughout the application.
 * It includes constants for user roles, application statuses, employment types, experience levels,
 * company sizes, pagination defaults, error messages, success messages, validation messages,
 * date formats, and MongoDB collection names.</p>
 *
 * <p>The class is final with a private constructor to prevent instantiation,
 * as it only contains static constant definitions.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws AssertionError if an attempt is made to instantiate this class
     */
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    // =========================================================================
    // User Roles
    // =========================================================================
    /** Constant for administrator role */
    public static final String ROLE_ADMIN = "ADMIN";

    /** Constant for recruiter role */
    public static final String ROLE_RECRUITER = "RECRUITER";

    /** Constant for candidate role */
    public static final String ROLE_CANDIDATE = "CANDIDATE";

    // =========================================================================
    // Application Status
    // =========================================================================
    /** Constant for application status: initial application submitted */
    public static final String APPLICATION_STATUS_APPLIED = "APPLIED";

    /** Constant for application status: under review by recruiter */
    public static final String APPLICATION_STATUS_REVIEWING = "REVIEWING";

    /** Constant for application status: candidate shortlisted for interview */
    public static final String APPLICATION_STATUS_SHORTLISTED = "SHORTLISTED";

    /** Constant for application status: application rejected */
    public static final String APPLICATION_STATUS_REJECTED = "REJECTED";

    /** Constant for application status: application accepted */
    public static final String APPLICATION_STATUS_ACCEPTED = "ACCEPTED";

    // =========================================================================
    // Employment Types
    // =========================================================================
    /** Constant for employment type: full-time */
    public static final String EMPLOYMENT_TYPE_FULL_TIME = "FULL_TIME";

    /** Constant for employment type: part-time */
    public static final String EMPLOYMENT_TYPE_PART_TIME = "PART_TIME";

    /** Constant for employment type: contract */
    public static final String EMPLOYMENT_TYPE_CONTRACT = "CONTRACT";

    /** Constant for employment type: internship */
    public static final String EMPLOYMENT_TYPE_INTERNSHIP = "INTERNSHIP";

    // =========================================================================
    // Experience Levels
    // =========================================================================
    /** Constant for experience level: entry-level (0-1 years) */
    public static final String EXPERIENCE_LEVEL_ENTRY = "ENTRY";

    /** Constant for experience level: junior (1-3 years) */
    public static final String EXPERIENCE_LEVEL_JUNIOR = "JUNIOR";

    /** Constant for experience level: mid-level (3-5 years) */
    public static final String EXPERIENCE_LEVEL_MID = "MID";

    /** Constant for experience level: senior (5-10 years) */
    public static final String EXPERIENCE_LEVEL_SENIOR = "SENIOR";

    /** Constant for experience level: executive (10+ years) */
    public static final String EXPERIENCE_LEVEL_EXECUTIVE = "EXECUTIVE";

    // =========================================================================
    // Company Sizes
    // =========================================================================
    /** Constant for company size: startup (1-10 employees) */
    public static final String COMPANY_SIZE_STARTUP = "STARTUP";

    /** Constant for company size: small (11-50 employees) */
    public static final String COMPANY_SIZE_SMALL = "SMALL";

    /** Constant for company size: medium (51-250 employees) */
    public static final String COMPANY_SIZE_MEDIUM = "MEDIUM";

    /** Constant for company size: large (251-1000 employees) */
    public static final String COMPANY_SIZE_LARGE = "LARGE";

    /** Constant for company size: enterprise (1000+ employees) */
    public static final String COMPANY_SIZE_ENTERPRISE = "ENTERPRISE";

    // =========================================================================
    // Pagination Defaults
    // =========================================================================
    /** Default number of items per page */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** Maximum allowed page size */
    public static final int MAX_PAGE_SIZE = 100;

    /** Default field to sort by */
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    /** Default sort direction */
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // =========================================================================
    // JWT Constants
    // =========================================================================
    /** Header name for JWT authentication token */
    public static final String JWT_HEADER = "Authorization";

    /** Prefix for JWT tokens in the Authorization header */
    public static final String JWT_PREFIX = "Bearer ";

    /** JWT token expiration in days */
    public static final int JWT_EXPIRATION_DAYS = 1;

    // =========================================================================
    // Error Messages
    // =========================================================================
    /** Error message for user not found */
    public static final String USER_NOT_FOUND = "User not found";

    /** Error message for job not found */
    public static final String JOB_NOT_FOUND = "Job not found";

    /** Error message for application not found */
    public static final String APPLICATION_NOT_FOUND = "Application not found";

    /** Error message for recruiter not found */
    public static final String RECRUITER_NOT_FOUND = "Recruiter not found";

    /** Error message for candidate not found */
    public static final String CANDIDATE_NOT_FOUND = "Candidate not found";

    /** Error message for invalid login credentials */
    public static final String INVALID_CREDENTIALS = "Invalid username or password";

    /** Error message for access denied */
    public static final String ACCESS_DENIED = "Access denied";

    /** Error message for already existing resource */
    public static final String ALREADY_EXISTS = "Resource already exists";

    // =========================================================================
    // Success Messages
    // =========================================================================
    /** Success message for user registration */
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully!";

    /** Success message for job creation */
    public static final String JOB_CREATED_SUCCESS = "Job created successfully!";

    /** Success message for application submission */
    public static final String APPLICATION_SUBMITTED_SUCCESS = "Application submitted successfully!";

    /** Success message for profile update */
    public static final String PROFILE_UPDATED_SUCCESS = "Profile updated successfully!";

    /** Success message for deletion */
    public static final String DELETED_SUCCESS = "Deleted successfully!";

    // =========================================================================
    // Validation Messages
    // =========================================================================
    /** Validation message for invalid input */
    public static final String INVALID_INPUT = "Invalid input provided";

    /** Validation message for email already in use */
    public static final String EMAIL_ALREADY_EXISTS = "Email is already in use";

    /** Validation message for username already taken */
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";

    // =========================================================================
    // Date Formats
    // =========================================================================
    /** Standard date format (YYYY-MM-DD) */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /** Standard date-time format (YYYY-MM-DD HH:MM:SS) */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // =========================================================================
    // MongoDB Collections
    // =========================================================================
    /** MongoDB collection name for users */
    public static final String COLLECTION_USERS = "users";

    /** MongoDB collection name for jobs */
    public static final String COLLECTION_JOBS = "jobs";

    /** MongoDB collection name for job applications */
    public static final String COLLECTION_APPLICATIONS = "job_applications";

    /** MongoDB collection name for recruiters */
    public static final String COLLECTION_RECRUITERS = "recruiters";

    /** MongoDB collection name for candidates */
    public static final String COLLECTION_CANDIDATES = "candidates";
}