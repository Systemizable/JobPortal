package me.josephsf.jobportaljosephsfeirtest.util;

import me.josephsf.jobportaljosephsfeir.model.Recruiter;

/**
 * A simplified Recruiter DTO for testing purposes.
 * <p>
 * This class omits the date/time fields to avoid serialization issues in tests.
 * It's used to create test doubles that don't depend on LocalDateTime handling.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
public class RecruiterTestDto {
    private String id;
    private String userId;
    private String companyName;
    private String companySize;
    private String location;
    private String industry;
    private String department;
    private String position;
    private String phoneNumber;
    private String linkedInUrl;
    private String companyWebsite;
    private String companyDescription;
    private Boolean isVerified;

    /**
     * Default constructor.
     */
    public RecruiterTestDto() {
    }

    /**
     * Creates a RecruiterTestDto from a Recruiter entity.
     *
     * @param recruiter The recruiter entity to convert
     * @return A new RecruiterTestDto with copied fields
     */
    public static RecruiterTestDto from(Recruiter recruiter) {
        RecruiterTestDto dto = new RecruiterTestDto();
        dto.setId(recruiter.getId());
        dto.setUserId(recruiter.getUserId());
        dto.setCompanyName(recruiter.getCompanyName());
        dto.setCompanySize(recruiter.getCompanySize());
        dto.setLocation(recruiter.getLocation());
        dto.setIndustry(recruiter.getIndustry());
        dto.setDepartment(recruiter.getDepartment());
        dto.setPosition(recruiter.getPosition());
        dto.setPhoneNumber(recruiter.getPhoneNumber());
        dto.setLinkedInUrl(recruiter.getLinkedInUrl());
        dto.setCompanyWebsite(recruiter.getCompanyWebsite());
        dto.setCompanyDescription(recruiter.getCompanyDescription());
        dto.setIsVerified(recruiter.getIsVerified());
        return dto;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}