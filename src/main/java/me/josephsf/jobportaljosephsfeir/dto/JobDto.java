package me.josephsf.jobportaljosephsfeir.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for job posting information.
 *
 * <p>This class encapsulates all the information related to a job posting in the Job Portal system.
 * It is used for creating new job listings and updating existing ones. The class includes
 * validation constraints to ensure that all required fields are provided and meet
 * size requirements.</p>
 *
 * <p>Job postings include:</p>
 * <ul>
 *   <li>Basic job details (title, description, company name, location)</li>
 *   <li>Classification information (category, employment type)</li>
 *   <li>Compensation details (salary)</li>
 *   <li>Job requirements and responsibilities</li>
 *   <li>Status indicators (active status, posting and deadline dates)</li>
 *   <li>Reference to the recruiter who posted the job</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
public class JobDto {
    /** MongoDB document ID */
    private String id;

    /** Job title with validation constraints */
    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Job title must be between 3 and 100 characters")
    private String title;

    /** Detailed job description with validation constraints */
    @NotBlank(message = "Job description is required")
    @Size(min = 10, max = 2000, message = "Job description must be between 10 and 2000 characters")
    private String description;

    /** Name of the company offering the job */
    @NotBlank(message = "Company name is required")
    private String companyName;

    /** Geographic location of the job (city, state, country, or remote) */
    @NotBlank(message = "Location is required")
    private String location;

    /** Job category (e.g., IT, Marketing, Finance) */
    @NotBlank(message = "Category is required")
    private String category;

    /** Type of employment (FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP) */
    @NotBlank(message = "Employment type is required")
    private String employmentType;

    /** Offered salary or salary range */
    private Double salary;

    /** Reference to the recruiter who posted the job */
    private String recruiterId;

    /** List of job requirements/qualifications */
    private List<String> requirements;

    /** List of job responsibilities/duties */
    private List<String> responsibilities;

    /** Indicator whether the job posting is active and visible to candidates */
    private Boolean isActive;

    /** Date when the job was posted */
    private LocalDateTime postedDate;

    /** Application deadline date */
    private LocalDateTime deadline;

    /** Timestamp when the record was created */
    private LocalDateTime createdAt;

    /** Timestamp when the record was last updated */
    private LocalDateTime updatedAt;

    /**
     * Default constructor for deserialization.
     */
    public JobDto() {}

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
     * Gets the job title.
     *
     * @return The job title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the job title.
     *
     * @param title The job title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the job description.
     *
     * @return The job description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the job description.
     *
     * @param description The job description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Gets the job location.
     *
     * @return The job location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the job location.
     *
     * @param location The job location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the job category.
     *
     * @return The job category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the job category.
     *
     * @param category The job category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the employment type.
     *
     * @return The employment type
     */
    public String getEmploymentType() {
        return employmentType;
    }

    /**
     * Sets the employment type.
     *
     * @param employmentType The employment type to set
     */
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    /**
     * Gets the salary.
     *
     * @return The salary
     */
    public Double getSalary() {
        return salary;
    }

    /**
     * Sets the salary.
     *
     * @param salary The salary to set
     */
    public void setSalary(Double salary) {
        this.salary = salary;
    }

    /**
     * Gets the recruiter ID.
     *
     * @return The recruiter ID
     */
    public String getRecruiterId() {
        return recruiterId;
    }

    /**
     * Sets the recruiter ID.
     *
     * @param recruiterId The recruiter ID to set
     */
    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    /**
     * Gets the list of job requirements.
     *
     * @return The list of job requirements
     */
    public List<String> getRequirements() {
        return requirements;
    }

    /**
     * Sets the list of job requirements.
     *
     * @param requirements The list of job requirements to set
     */
    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    /**
     * Gets the list of job responsibilities.
     *
     * @return The list of job responsibilities
     */
    public List<String> getResponsibilities() {
        return responsibilities;
    }

    /**
     * Sets the list of job responsibilities.
     *
     * @param responsibilities The list of job responsibilities to set
     */
    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    /**
     * Gets the active status of the job posting.
     *
     * @return The active status (true if active, false if inactive)
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Sets the active status of the job posting.
     *
     * @param isActive The active status to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gets the posted date.
     *
     * @return The posted date
     */
    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    /**
     * Sets the posted date.
     *
     * @param postedDate The posted date to set
     */
    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    /**
     * Gets the application deadline.
     *
     * @return The application deadline
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Sets the application deadline.
     *
     * @param deadline The application deadline to set
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
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