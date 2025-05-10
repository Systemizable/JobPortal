package me.josephsf.jobportaljosephsfeir.util;

public final class Constants {

    // Prevent instantiation
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_RECRUITER = "RECRUITER";
    public static final String ROLE_CANDIDATE = "CANDIDATE";

    // Application Status
    public static final String APPLICATION_STATUS_APPLIED = "APPLIED";
    public static final String APPLICATION_STATUS_REVIEWING = "REVIEWING";
    public static final String APPLICATION_STATUS_SHORTLISTED = "SHORTLISTED";
    public static final String APPLICATION_STATUS_REJECTED = "REJECTED";
    public static final String APPLICATION_STATUS_ACCEPTED = "ACCEPTED";

    // Employment Types
    public static final String EMPLOYMENT_TYPE_FULL_TIME = "FULL_TIME";
    public static final String EMPLOYMENT_TYPE_PART_TIME = "PART_TIME";
    public static final String EMPLOYMENT_TYPE_CONTRACT = "CONTRACT";
    public static final String EMPLOYMENT_TYPE_INTERNSHIP = "INTERNSHIP";

    // Experience Levels
    public static final String EXPERIENCE_LEVEL_ENTRY = "ENTRY";
    public static final String EXPERIENCE_LEVEL_JUNIOR = "JUNIOR";
    public static final String EXPERIENCE_LEVEL_MID = "MID";
    public static final String EXPERIENCE_LEVEL_SENIOR = "SENIOR";
    public static final String EXPERIENCE_LEVEL_EXECUTIVE = "EXECUTIVE";

    // Company Sizes
    public static final String COMPANY_SIZE_STARTUP = "STARTUP";
    public static final String COMPANY_SIZE_SMALL = "SMALL";
    public static final String COMPANY_SIZE_MEDIUM = "MEDIUM";
    public static final String COMPANY_SIZE_LARGE = "LARGE";
    public static final String COMPANY_SIZE_ENTERPRISE = "ENTERPRISE";

    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // JWT Constants
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final int JWT_EXPIRATION_DAYS = 1;

    // Error Messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String JOB_NOT_FOUND = "Job not found";
    public static final String APPLICATION_NOT_FOUND = "Application not found";
    public static final String RECRUITER_NOT_FOUND = "Recruiter not found";
    public static final String CANDIDATE_NOT_FOUND = "Candidate not found";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String ALREADY_EXISTS = "Resource already exists";

    // Success Messages
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully!";
    public static final String JOB_CREATED_SUCCESS = "Job created successfully!";
    public static final String APPLICATION_SUBMITTED_SUCCESS = "Application submitted successfully!";
    public static final String PROFILE_UPDATED_SUCCESS = "Profile updated successfully!";
    public static final String DELETED_SUCCESS = "Deleted successfully!";

    // Validation Messages
    public static final String INVALID_INPUT = "Invalid input provided";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already in use";
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // MongoDB Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_JOBS = "jobs";
    public static final String COLLECTION_APPLICATIONS = "job_applications";
    public static final String COLLECTION_RECRUITERS = "recruiters";
    public static final String COLLECTION_CANDIDATES = "candidates";
}