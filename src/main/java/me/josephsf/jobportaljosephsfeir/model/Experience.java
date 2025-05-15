package me.josephsf.jobportaljosephsfeir.model;

import java.time.LocalDate;

/**
 * Model class representing a work experience entry for a candidate in the Job Portal system.
 * <p>
 * The Experience class is an embedded document within the Candidate entity that captures
 * details about a candidate's professional work history, including job titles, employers,
 * locations, durations, and responsibilities.
 * </p>
 *
 * <p>This class is used to structure work history information within a candidate's
 * profile. Multiple Experience instances can be included in a Candidate document to represent
 * the complete professional background of a job seeker.</p>
 *
 * <p>Experience records include:</p>
 * <ul>
 *   <li>Job title or position held</li>
 *   <li>Company or organization name</li>
 *   <li>Work location</li>
 *   <li>Employment period (start and end dates)</li>
 *   <li>Job description and responsibilities</li>
 *   <li>Indicator of current employment</li>
 * </ul>
 *
 * <p>This information is used for candidate qualification evaluation, matching to job
 * requirements, and displaying work history to recruiters.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see Candidate
 */
public class Experience {
    /**
     * Job title or position held.
     * Example: "Software Engineer", "Project Manager", etc.
     */
    private String title;

    /**
     * Name of the company or organization.
     * Example: "Google", "Microsoft", etc.
     */
    private String company;

    /**
     * Location where the job was performed.
     * Can be city, state, country, or "Remote".
     */
    private String location;

    /**
     * Date when employment began.
     */
    private LocalDate startDate;

    /**
     * Date when employment ended.
     * Will be null if this is the current position.
     */
    private LocalDate endDate;

    /**
     * Description of job duties, responsibilities, and achievements.
     */
    private String description;

    /**
     * Flag indicating if this is the candidate's current position.
     */
    private Boolean isCurrent;

    /**
     * Default constructor.
     */
    public Experience() {}

    /**
     * Constructor with all experience details.
     *
     * @param title the job title
     * @param company the company name
     * @param location the job location
     * @param startDate the employment start date
     * @param endDate the employment end date (null if current)
     * @param description the job description
     * @param isCurrent flag indicating if this is the current position
     */
    public Experience(String title, String company, String location, LocalDate startDate, LocalDate endDate, String description, Boolean isCurrent) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.isCurrent = isCurrent;
    }

    /**
     * Gets the job title.
     *
     * @return the job title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the job title.
     *
     * @param title the job title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the company name.
     *
     * @return the company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the company name.
     *
     * @param company the company name to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Gets the job location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the job location.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the employment start date.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the employment start date.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the employment end date.
     * Will be null if this is the current position.
     *
     * @return the end date or null if current
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the employment end date.
     * Should be null if this is the current position.
     *
     * @param endDate the end date to set or null if current
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the job description.
     *
     * @return the job description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the job description.
     *
     * @param description the job description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets whether this is the candidate's current position.
     *
     * @return true if this is the current position, false otherwise
     */
    public Boolean getIsCurrent() {
        return isCurrent;
    }

    /**
     * Sets whether this is the candidate's current position.
     * When set to true, the endDate should typically be null.
     *
     * @param isCurrent true if this is the current position, false otherwise
     */
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}