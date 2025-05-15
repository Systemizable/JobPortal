package me.josephsf.jobportaljosephsfeir.controller;

import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing job applications in the Job Portal system.
 * <p>
 * This controller handles HTTP requests related to job applications, including
 * submitting applications, retrieving application details, updating application
 * status, and generating application statistics. It provides endpoints for both
 * candidates and recruiters based on their roles and permissions.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    /**
     * Constructs an ApplicationController with the specified service.
     *
     * @param applicationService The service for application-related operations
     */
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Endpoint for a candidate to apply to a job.
     * <p>
     * This endpoint allows a candidate to submit an application for a specific job.
     * It requires a valid application DTO containing job and candidate information.
     * A candidate can only apply once to each job.
     * </p>
     *
     * @param applicationDto The DTO containing application details
     * @return ResponseEntity with the created application or an error response
     */
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> applyToJob(@Valid @RequestBody ApplicationDto applicationDto) {
        JobApplication application = applicationService.applyToJob(applicationDto);
        if (application == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(false, "You have already applied to this job"));
        }
        return ResponseEntity.ok(application);
    }

    /**
     * Retrieves a specific job application by its ID.
     * <p>
     * This endpoint is accessible to both candidates and recruiters.
     * It returns the application details if found.
     * </p>
     *
     * @param id The ID of the application to retrieve
     * @return ResponseEntity with the application or a not-found status
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<?> getApplicationById(@PathVariable String id) {
        JobApplication application = applicationService.getApplicationById(id);
        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(application);
    }

    /**
     * Retrieves all applications submitted by a specific candidate.
     * <p>
     * This endpoint supports pagination and sorting of results.
     * It returns a paginated response with application details.
     * </p>
     *
     * @param candidateId The ID of the candidate
     * @param page The page number (zero-based)
     * @param size The size of each page
     * @param sortBy The field to sort by
     * @param sortDir The sort direction (asc or desc)
     * @return ResponseEntity with paginated application data
     */
    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, Object>> getApplicationsByCandidate(
            @PathVariable String candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobApplication> applicationsPage = applicationService.getApplicationsByCandidate(candidateId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("applications", applicationsPage.getContent());
        response.put("currentPage", applicationsPage.getNumber());
        response.put("totalItems", applicationsPage.getTotalElements());
        response.put("totalPages", applicationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all applications for a specific job.
     * <p>
     * This endpoint is accessible to recruiters and supports pagination and sorting.
     * It returns a paginated response with application details for the specified job.
     * </p>
     *
     * @param jobId The ID of the job
     * @param page The page number (zero-based)
     * @param size The size of each page
     * @param sortBy The field to sort by
     * @param sortDir The sort direction (asc or desc)
     * @return ResponseEntity with paginated application data
     */
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Map<String, Object>> getApplicationsByJob(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobApplication> applicationsPage = applicationService.getApplicationsByJob(jobId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("applications", applicationsPage.getContent());
        response.put("currentPage", applicationsPage.getNumber());
        response.put("totalItems", applicationsPage.getTotalElements());
        response.put("totalPages", applicationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Updates the status of a job application.
     * <p>
     * This endpoint allows recruiters to change the status of an application
     * (e.g., to REVIEWING, SHORTLISTED, REJECTED, or ACCEPTED) and optionally
     * add review notes about the application.
     * </p>
     *
     * @param id The ID of the application to update
     * @param status The new status for the application
     * @param reviewNotes Optional notes about the application review
     * @return ResponseEntity with the updated application or a not-found status
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewNotes) {

        JobApplication updatedApplication = applicationService.updateApplicationStatus(id, status, reviewNotes);
        if (updatedApplication == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedApplication);
    }

    /**
     * Adds or updates the review notes for a job application.
     * <p>
     * This endpoint allows recruiters to add or modify review notes
     * about an application without changing its status.
     * </p>
     *
     * @param id The ID of the application
     * @param reviewNotes The review notes to add or update
     * @return ResponseEntity with the updated application or a not-found status
     */
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> addReviewNotes(
            @PathVariable String id,
            @RequestParam String reviewNotes) {

        JobApplication updatedApplication = applicationService.addReviewNotes(id, reviewNotes);
        if (updatedApplication == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedApplication);
    }

    /**
     * Adds or updates the interview notes for a job application.
     * <p>
     * This endpoint allows recruiters to add or modify notes about
     * an interview conducted with the candidate.
     * </p>
     *
     * @param id The ID of the application
     * @param interviewNotes The interview notes to add or update
     * @return ResponseEntity with the updated application or a not-found status
     */
    @PutMapping("/{id}/interview")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> addInterviewNotes(
            @PathVariable String id,
            @RequestParam String interviewNotes) {

        JobApplication updatedApplication = applicationService.addInterviewNotes(id, interviewNotes);
        if (updatedApplication == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedApplication);
    }

    /**
     * Withdraws a job application.
     * <p>
     * This endpoint allows candidates to withdraw their application
     * for a job, removing it from the system.
     * </p>
     *
     * @param id The ID of the application to withdraw
     * @return ResponseEntity with a success response or a not-found status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> withdrawApplication(@PathVariable String id) {
        boolean withdrawn = applicationService.withdrawApplication(id);
        if (!withdrawn) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Application withdrawn successfully"));
    }

    /**
     * Retrieves statistics for applications to a specific job.
     * <p>
     * This endpoint provides counts of applications by status
     * (e.g., APPLIED, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED)
     * for a given job.
     * </p>
     *
     * @param jobId The ID of the job
     * @return ResponseEntity with application statistics
     */
    @GetMapping("/stats/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getApplicationStats(@PathVariable String jobId) {
        Map<String, Long> stats = applicationService.getApplicationStats(jobId);
        return ResponseEntity.ok(stats);
    }
}