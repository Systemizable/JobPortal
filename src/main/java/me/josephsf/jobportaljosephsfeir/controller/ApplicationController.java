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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<?> getApplicationById(@PathVariable String id) {
        JobApplication application = applicationService.getApplicationById(id);
        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(application);
    }

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> withdrawApplication(@PathVariable String id) {
        boolean withdrawn = applicationService.withdrawApplication(id);
        if (!withdrawn) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Application withdrawn successfully"));
    }

    @GetMapping("/stats/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getApplicationStats(@PathVariable String jobId) {
        Map<String, Long> stats = applicationService.getApplicationStats(jobId);
        return ResponseEntity.ok(stats);
    }
}