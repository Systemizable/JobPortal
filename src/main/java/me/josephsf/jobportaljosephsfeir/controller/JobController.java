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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobDto jobDto) {
        Job createdJob = jobService.createJob(jobDto);
        return ResponseEntity.ok(createdJob);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateJob(@PathVariable String id, @Valid @RequestBody JobDto jobDto) {
        Job updatedJob = jobService.updateJob(id, jobDto);
        if (updatedJob == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        boolean deleted = jobService.deleteJob(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Job deleted successfully"));
    }

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

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getJobsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(jobService.getJobsByCategory(category));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<?> getJobsByLocation(@PathVariable String location) {
        return ResponseEntity.ok(jobService.getJobsByLocation(location));
    }

    @GetMapping("/recruiter/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getJobsByRecruiter(@PathVariable String recruiterId) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(recruiterId));
    }

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