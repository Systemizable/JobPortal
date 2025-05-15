package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing job applications in the Job Portal system.
 *
 * <p>This service provides methods for creating, retrieving, updating, and deleting job applications,
 * as well as managing application statuses and generating application statistics. It interacts with
 * the MongoDB database through the ApplicationRepository and handles related business logic.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    /**
     * Constructs a new ApplicationService with required dependencies.
     *
     * @param applicationRepository Repository for job application data operations
     */
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Creates a new job application.
     * Checks if the candidate has already applied to the job to prevent duplicate applications.
     *
     * @param applicationDto The data transfer object containing application information
     * @return The created application or null if the candidate has already applied
     */
    public JobApplication applyToJob(ApplicationDto applicationDto) {
        // Check if already applied
        Optional<JobApplication> existingApplication = applicationRepository
                .findByCandidateIdAndJobId(applicationDto.getCandidateId(), applicationDto.getJobId());

        if (existingApplication.isPresent()) {
            return null; // Already applied
        }

        JobApplication application = new JobApplication();
        application.setJobId(applicationDto.getJobId());
        application.setCandidateId(applicationDto.getCandidateId());
        application.setCoverLetter(applicationDto.getCoverLetter());
        application.setResumeUrl(applicationDto.getResumeUrl());
        application.setStatus("APPLIED");
        application.setApplicationDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param id The unique identifier of the application
     * @return The application if found, null otherwise
     */
    public JobApplication getApplicationById(String id) {
        return applicationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves applications submitted by a specific candidate with pagination support.
     *
     * @param candidateId The ID of the candidate
     * @param pageable Pagination and sorting parameters
     * @return A page of job applications for the specified candidate
     */
    public Page<JobApplication> getApplicationsByCandidate(String candidateId, Pageable pageable) {
        return applicationRepository.findByCandidateId(candidateId, pageable);
    }

    /**
     * Retrieves applications for a specific job with pagination support.
     *
     * @param jobId The ID of the job
     * @param pageable Pagination and sorting parameters
     * @return A page of job applications for the specified job
     */
    public Page<JobApplication> getApplicationsByJob(String jobId, Pageable pageable) {
        return applicationRepository.findByJobId(jobId, pageable);
    }

    /**
     * Retrieves applications with a specific status.
     *
     * @param status The application status (e.g., APPLIED, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED)
     * @return List of applications with the specified status
     */
    public List<JobApplication> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    /**
     * Updates the status of a job application and optionally adds review notes.
     *
     * @param id The unique identifier of the application
     * @param status The new status to set
     * @param reviewNotes Optional review notes to add
     * @return The updated application or null if the application is not found
     */
    public JobApplication updateApplicationStatus(String id, String status, String reviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setStatus(status);
            application.setReviewDate(LocalDateTime.now());
            if (reviewNotes != null) {
                application.setReviewNotes(reviewNotes);
            }
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    /**
     * Adds review notes to an application.
     *
     * @param id The unique identifier of the application
     * @param reviewNotes The review notes to add
     * @return The updated application or null if the application is not found
     */
    public JobApplication addReviewNotes(String id, String reviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setReviewNotes(reviewNotes);
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    /**
     * Adds interview notes to an application.
     *
     * @param id The unique identifier of the application
     * @param interviewNotes The interview notes to add
     * @return The updated application or null if the application is not found
     */
    public JobApplication addInterviewNotes(String id, String interviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setInterviewNotes(interviewNotes);
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    /**
     * Withdraws (deletes) a job application.
     *
     * @param id The unique identifier of the application to withdraw
     * @return true if the application was successfully withdrawn, false otherwise
     */
    public boolean withdrawApplication(String id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Generates statistics for applications to a specific job.
     *
     * @param jobId The ID of the job
     * @return A map containing counts of applications in various statuses
     */
    public Map<String, Long> getApplicationStats(String jobId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", applicationRepository.countByJobId(jobId));
        stats.put("applied", applicationRepository.countByJobIdAndStatus(jobId, "APPLIED"));
        stats.put("reviewing", applicationRepository.countByJobIdAndStatus(jobId, "REVIEWING"));
        stats.put("shortlisted", applicationRepository.countByJobIdAndStatus(jobId, "SHORTLISTED"));
        stats.put("rejected", applicationRepository.countByJobIdAndStatus(jobId, "REJECTED"));
        stats.put("accepted", applicationRepository.countByJobIdAndStatus(jobId, "ACCEPTED"));
        return stats;
    }

    /**
     * Retrieves applications submitted within a specific number of days.
     *
     * @param days The number of days to look back
     * @return List of recent applications
     */
    public List<JobApplication> getRecentApplications(int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        return applicationRepository.findByApplicationDateAfter(sinceDate);
    }

    /**
     * Counts the number of applications submitted by a specific candidate.
     *
     * @param candidateId The ID of the candidate
     * @return The count of applications submitted by the candidate
     */
    public long countApplicationsByCandidate(String candidateId) {
        return applicationRepository.countByCandidateId(candidateId);
    }

    /**
     * Retrieves applications submitted within a specific date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of applications submitted within the specified date range
     */
    public List<JobApplication> getApplicationsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return applicationRepository.findByApplicationDateBetween(startDate, endDate);
    }
}