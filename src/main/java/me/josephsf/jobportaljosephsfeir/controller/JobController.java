package me.josephsf.jobportaljosephsfeir.controller;

import me.josephsf.jobportaljosephsfeir.dto.JobDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.service.JobService;
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
 * REST controller for managing job postings in the Job Portal system.
 * <p>
 * This controller handles HTTP requests related to job listings, including
 * creating, retrieving, updating, and deleting job postings. It provides
 * endpoints for both public access (browsing and searching jobs) and
 * restricted access for recruiters (posting and managing jobs). The controller
 * supports pagination, sorting, and filtering of job listings to enhance
 * the job search experience.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    /**
     * Constructs a JobController with the specified service.
     *
     * @param jobService The service for job-related operations
     */
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Retrieves a paginated list of all job postings.
     * <p>
     * This endpoint is publicly accessible and supports pagination and sorting
     * to optimize the browsing experience. By default, jobs are sorted by
     * posted date in descending order (newest first).
     * </p>
     *
     * @param page The page number (zero-based)
     * @param size The size of each page
     * @param sortBy The field to sort by
     * @param sortDir The sort direction (asc or desc)
     * @return ResponseEntity with paginated job listings and metadata
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobsPage = jobService.getAllJobs(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobsPage.getContent());
        response.put("currentPage", jobsPage.getNumber());
        response.put("totalItems", jobsPage.getTotalElements());
        response.put("totalPages", jobsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific job posting by its ID.
     * <p>
     * This endpoint is publicly accessible and allows viewing the details
     * of a particular job posting.
     * </p>
     *
     * @param id The ID of the job posting to retrieve
     * @return ResponseEntity with the job details or a not-found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    /**
     * Creates a new job posting.
     * <p>
     * This endpoint allows recruiters to post new job listings.
     * It requires RECRUITER role authorization and validates the
     * job details before creation.
     * </p>
     *
     * @param jobDto The DTO containing job details
     * @return ResponseEntity with the created job posting
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobDto jobDto) {
        Job createdJob = jobService.createJob(jobDto);
        return ResponseEntity.ok(createdJob);
    }

    /**
     * Updates an existing job posting.
     * <p>
     * This endpoint allows recruiters to modify the details of a job
     * posting they have created. It requires RECRUITER role authorization
     * and validates the updated job details.
     * </p>
     *
     * @param id The ID of the job posting to update
     * @param jobDto The DTO containing updated job details
     * @return ResponseEntity with the updated job posting or a not-found status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateJob(@PathVariable String id, @Valid @RequestBody JobDto jobDto) {
        Job updatedJob = jobService.updateJob(id, jobDto);
        if (updatedJob == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedJob);
    }

    /**
     * Deletes a job posting.
     * <p>
     * This endpoint allows recruiters to remove job postings they have created.
     * It requires RECRUITER role authorization.
     * </p>
     *
     * @param id The ID of the job posting to delete
     * @return ResponseEntity with a success response or a not-found status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        boolean deleted = jobService.deleteJob(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Job deleted successfully"));
    }

    /**
     * Searches for job postings based on a keyword.
     * <p>
     * This endpoint is publicly accessible and allows users to search for jobs
     * by matching the keyword against job titles, descriptions, and company names.
     * It supports pagination of search results.
     * </p>
     *
     * @param keyword The search keyword
     * @param page The page number (zero-based)
     * @param size The size of each page
     * @return ResponseEntity with paginated search results and metadata
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobsPage = jobService.searchJobs(keyword, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobsPage.getContent());
        response.put("currentPage", jobsPage.getNumber());
        response.put("totalItems", jobsPage.getTotalElements());
        response.put("totalPages", jobsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves job postings by category.
     * <p>
     * This endpoint is publicly accessible and allows filtering jobs
     * by their category (e.g., IT, Marketing, Finance).
     * </p>
     *
     * @param category The job category to filter by
     * @return ResponseEntity with a list of matching job postings
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getJobsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(jobService.getJobsByCategory(category));
    }

    /**
     * Retrieves job postings by location.
     * <p>
     * This endpoint is publicly accessible and allows filtering jobs
     * by their geographic location.
     * </p>
     *
     * @param location The location to filter by
     * @return ResponseEntity with a list of matching job postings
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<?> getJobsByLocation(@PathVariable String location) {
        return ResponseEntity.ok(jobService.getJobsByLocation(location));
    }

    /**
     * Retrieves job postings created by a specific recruiter.
     * <p>
     * This endpoint allows recruiters to view all the job postings they have created.
     * It requires RECRUITER role authorization.
     * </p>
     *
     * @param recruiterId The ID of the recruiter
     * @return ResponseEntity with a list of job postings by the recruiter
     */
    @GetMapping("/recruiter/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getJobsByRecruiter(@PathVariable String recruiterId) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(recruiterId));
    }

    /**
     * Toggles the active status of a job posting.
     * <p>
     * This endpoint allows recruiters to activate or deactivate a job posting
     * without deleting it. Inactive jobs are not displayed in public searches.
     * It requires RECRUITER role authorization.
     * </p>
     *
     * @param id The ID of the job posting to toggle
     * @return ResponseEntity with the updated job posting or a not-found status
     */
    @PutMapping("/{id}/toggleActive")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> toggleJobActive(@PathVariable String id) {
        Job job = jobService.toggleJobActive(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }
}