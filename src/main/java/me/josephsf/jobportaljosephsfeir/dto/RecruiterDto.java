package me.josephsf.jobportaljosephsfeir.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for recruiter profile information.
 *
 * <p>This class encapsulates the information related to a recruiter's profile in the Job Portal system.
 * It contains details about the recruiter's company, professional information, and contact details.
 * The class includes validation constraints to ensure data integrity when creating or updating
 * recruiter profiles.</p>
 *
 * <p>Fields include:</p>
 * <ul>
 *   <li>Basic identifiers (id, userId)</li>
 *   <li>Company information (name, size, industry, description, website)</li>
 *   <li>Recruiter's professional details (department, position)</li>
 *   <li>Contact information (location, phone number, LinkedIn)</li>
 *   <li>Status indicators (verification status, timestamps)</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class RecruiterDto {
    /** MongoDB document ID */
    private String id;

    /** Reference to the User document ID, cannot be blank */
    @NotBlank(message = "User ID is required")
    private String userId;

    /** Name of the company the recruiter represents, with size constraints */
    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    /** Size category of the company (STARTUP, SMALL, MEDIUM, LARGE, ENTERPRISE) */
    private String companySize;

    /** Geographic location of the company/recruiter, cannot be blank */
    @NotBlank(message = "Location is required")
    private String location;

    /** Industry sector the company operates in */
    private String industry;

    /** Department the recruiter works in */
    private String department;

    /** Position/title of the recruiter */
    private String position;

    /** Recruiter's contact phone number */
    private String phoneNumber;

    /** Recruiter's LinkedIn profile URL */
    private String linkedInUrl;

    /** Company's website URL */
    private String companyWebsite;

    /** Description of the company with size constraint */
    @Size(max = 1000, message = "Company description must not exceed 1000 characters")
    private String companyDescription;

    /** Flag indicating if the recruiter profile has been verified */
    private Boolean isVerified;

    /** Timestamp when the profile was created */
    private LocalDateTime createdAt;

    /** Timestamp when the profile was last updated */
    private LocalDateTime updatedAt;

    /**
     * Default constructor for deserialization.
     */
    public RecruiterDto() {}

    /**
     * Gets the MongoDB document ID.
     *
     * @return The document ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the MongoDB document ID.
     *
     * @param id The document ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the user ID associated with this recruiter profile.
     *
     * @return The user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this recruiter profile.
     *
     * @param userId The user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the company name.
     *
     * @return The company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name.
     *
     * @param companyName The company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the company size category.
     *
     * @return The company size category
     */
    public String getCompanySize() {
        return companySize;
    }

    /**
     * Sets the company size category.
     *
     * @param companySize The company size to set
     */
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    /**
     * Gets the location.
     *
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location The location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the industry.
     *
     * @return The industry
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * Sets the industry.
     *
     * @param industry The industry to set
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     * Gets the department.
     *
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param department The department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the position.
     *
     * @return The position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param position The position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Gets the phone number.
     *
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the LinkedIn URL.
     *
     * @return The LinkedIn URL
     */
    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    /**
     * Sets the LinkedIn URL.
     *
     * @param linkedInUrl The LinkedIn URL to set
     */
    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    /**
     * Gets the company website URL.
     *
     * @return The company website URL
     */
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    /**
     * Sets the company website URL.
     *
     * @param companyWebsite The company website URL to set
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    /**
     * Gets the company description.
     *
     * @return The company description
     */
    public String getCompanyDescription() {
        return companyDescription;
    }

    /**
     * Sets the company description.
     *
     * @param companyDescription The company description to set
     */
    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    /**
     * Gets the verification status.
     *
     * @return The verification status
     */
    public Boolean getIsVerified() {
        return isVerified;
    }

    /**
     * Sets the verification status.
     *
     * @param isVerified The verification status to set
     */
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt The last update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}