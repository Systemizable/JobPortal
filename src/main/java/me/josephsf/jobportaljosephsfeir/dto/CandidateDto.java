package me.josephsf.jobportaljosephsfeir.dto;

import me.josephsf.jobportaljosephsfeir.model.Experience;
import me.josephsf.jobportaljosephsfeir.model.Education;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for candidate profile information.
 *
 * <p>This class encapsulates all the information related to a job candidate's profile
 * in the Job Portal system. It includes personal details, professional experience,
 * education, skills, and other relevant information for job seekers. The class
 * contains validation constraints to ensure that mandatory fields are provided
 * and meet size requirements.</p>
 *
 * <p>A candidate profile includes:</p>
 * <ul>
 *   <li>Personal information (name, contact details, location)</li>
 *   <li>Professional background (current title, experience level, years of experience)</li>
 *   <li>Skills list</li>
 *   <li>Professional experience history</li>
 *   <li>Educational background</li>
 *   <li>Resume and portfolio links</li>
 *   <li>Salary expectations and availability status</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class CandidateDto {
    /** MongoDB document ID */
    private String id;

    /** Reference to the User document ID, cannot be blank */
    @NotBlank(message = "User ID is required")
    private String userId;

    /** Candidate's first name with validation constraints */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /** Candidate's last name with validation constraints */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /** Candidate's contact phone number */
    private String phoneNumber;

    /** Candidate's geographic location, cannot be blank */
    @NotBlank(message = "Location is required")
    private String location;

    /** Candidate's current job title */
    private String currentTitle;

    /** Experience level category (ENTRY, JUNIOR, MID, SENIOR, EXECUTIVE) */
    private String experienceLevel;

    /** Number of years of professional experience with range validation */
    @Min(value = 0, message = "Years of experience must be at least 0")
    @Max(value = 50, message = "Years of experience must not exceed 50")
    private Integer yearsOfExperience;

    /** List of professional skills */
    private List<String> skills;

    /** List of previous work experiences */
    private List<Experience> experience;

    /** List of educational qualifications */
    private List<Education> education;

    /** URL to the candidate's resume */
    private String resumeUrl;

    /** Professional summary/bio with size constraint */
    @Size(max = 1000, message = "Profile summary must not exceed 1000 characters")
    private String profileSummary;

    /** URL to the candidate's LinkedIn profile */
    private String linkedInUrl;

    /** URL to the candidate's portfolio website */
    private String portfolioUrl;

    /** Candidate's expected salary */
    private Double expectedSalary;

    /** Flag indicating if the candidate is available for hire */
    private Boolean isAvailable;

    /** Timestamp when the profile was created */
    private LocalDateTime createdAt;

    /** Timestamp when the profile was last updated */
    private LocalDateTime updatedAt;

    /**
     * Default constructor for deserialization.
     */
    public CandidateDto() {}

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
     * Gets the user ID associated with this candidate profile.
     *
     * @return The user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this candidate profile.
     *
     * @param userId The user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the candidate's first name.
     *
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the candidate's first name.
     *
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the candidate's last name.
     *
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the candidate's last name.
     *
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the candidate's phone number.
     *
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the candidate's phone number.
     *
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the candidate's location.
     *
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the candidate's location.
     *
     * @param location The location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the candidate's current job title.
     *
     * @return The current job title
     */
    public String getCurrentTitle() {
        return currentTitle;
    }

    /**
     * Sets the candidate's current job title.
     *
     * @param currentTitle The current job title to set
     */
    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    /**
     * Gets the candidate's experience level category.
     *
     * @return The experience level
     */
    public String getExperienceLevel() {
        return experienceLevel;
    }

    /**
     * Sets the candidate's experience level category.
     *
     * @param experienceLevel The experience level to set
     */
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    /**
     * Gets the candidate's years of professional experience.
     *
     * @return The years of experience
     */
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Sets the candidate's years of professional experience.
     *
     * @param yearsOfExperience The years of experience to set
     */
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Gets the list of candidate's skills.
     *
     * @return The list of skills
     */
    public List<String> getSkills() {
        return skills;
    }

    /**
     * Sets the list of candidate's skills.
     *
     * @param skills The list of skills to set
     */
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    /**
     * Gets the list of candidate's work experiences.
     *
     * @return The list of work experiences
     */
    public List<Experience> getExperience() {
        return experience;
    }

    /**
     * Sets the list of candidate's work experiences.
     *
     * @param experience The list of work experiences to set
     */
    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }

    /**
     * Gets the list of candidate's educational qualifications.
     *
     * @return The list of educational qualifications
     */
    public List<Education> getEducation() {
        return education;
    }

    /**
     * Sets the list of candidate's educational qualifications.
     *
     * @param education The list of educational qualifications to set
     */
    public void setEducation(List<Education> education) {
        this.education = education;
    }

    /**
     * Gets the URL to the candidate's resume.
     *
     * @return The resume URL
     */
    public String getResumeUrl() {
        return resumeUrl;
    }

    /**
     * Sets the URL to the candidate's resume.
     *
     * @param resumeUrl The resume URL to set
     */
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    /**
     * Gets the candidate's profile summary/bio.
     *
     * @return The profile summary
     */
    public String getProfileSummary() {
        return profileSummary;
    }

    /**
     * Sets the candidate's profile summary/bio.
     *
     * @param profileSummary The profile summary to set
     */
    public void setProfileSummary(String profileSummary) {
        this.profileSummary = profileSummary;
    }

    /**
     * Gets the URL to the candidate's LinkedIn profile.
     *
     * @return The LinkedIn URL
     */
    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    /**
     * Sets the URL to the candidate's LinkedIn profile.
     *
     * @param linkedInUrl The LinkedIn URL to set
     */
    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    /**
     * Gets the URL to the candidate's portfolio website.
     *
     * @return The portfolio URL
     */
    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    /**
     * Sets the URL to the candidate's portfolio website.
     *
     * @param portfolioUrl The portfolio URL to set
     */
    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    /**
     * Gets the candidate's expected salary.
     *
     * @return The expected salary
     */
    public Double getExpectedSalary() {
        return expectedSalary;
    }

    /**
     * Sets the candidate's expected salary.
     *
     * @param expectedSalary The expected salary to set
     */
    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    /**
     * Gets the candidate's availability status.
     *
     * @return The availability status (true if available, false if not available)
     */
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Sets the candidate's availability status.
     *
     * @param isAvailable The availability status to set
     */
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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