package me.josephsf.jobportaljosephsfeir.controller;

import me.josephsf.jobportaljosephsfeir.dto.CandidateDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Candidate;
import me.josephsf.jobportaljosephsfeir.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing candidate profiles in the Job Portal system.
 * <p>
 * This controller handles HTTP requests related to candidate profiles, including
 * creating, retrieving, updating, and deleting candidate information. It also provides
 * endpoints for searching candidates based on various criteria such as skills and
 * experience levels, which are primarily used by recruiters. Candidate-specific
 * operations like updating resume and availability status are also supported.
 * </p>
 * <p>
 * Different endpoints have different authorization requirements based on user roles.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    /**
     * Constructs a CandidateController with the specified service.
     *
     * @param candidateService The service for candidate-related operations
     */
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    /**
     * Retrieves a candidate profile by its ID.
     * <p>
     * This endpoint is accessible to candidates, recruiters, and administrators.
     * It returns the candidate's profile information if found.
     * </p>
     *
     * @param id The ID of the candidate profile to retrieve
     * @return ResponseEntity with the candidate profile or a not-found status
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getCandidateById(@PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    /**
     * Retrieves a candidate profile by the associated user ID.
     * <p>
     * This endpoint allows candidates to access their own profile by their user ID.
     * It is primarily used for profile setup and management.
     * </p>
     *
     * @param userId The ID of the user associated with the candidate profile
     * @return ResponseEntity with the candidate profile or a not-found status
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateByUserId(@PathVariable String userId) {
        Candidate candidate = candidateService.getCandidateByUserId(userId);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    /**
     * Creates a new candidate profile.
     * <p>
     * This endpoint allows users with the CANDIDATE role to create their profile
     * by providing their personal and professional information. A user can only
     * have one candidate profile.
     * </p>
     *
     * @param candidateDto The DTO containing candidate profile information
     * @return ResponseEntity with the created profile or an error response
     */
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> createCandidateProfile(@Valid @RequestBody CandidateDto candidateDto) {
        Candidate candidate = candidateService.createCandidateProfile(candidateDto);
        if (candidate == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(false, "Candidate profile already exists for this user"));
        }
        return ResponseEntity.ok(candidate);
    }

    /**
     * Updates an existing candidate profile.
     * <p>
     * This endpoint allows candidates to update their profile information
     * such as personal details, skills, experience, and education.
     * </p>
     *
     * @param id The ID of the candidate profile to update
     * @param candidateDto The DTO containing updated profile information
     * @return ResponseEntity with the updated profile or a not-found status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateCandidateProfile(@PathVariable String id, @Valid @RequestBody CandidateDto candidateDto) {
        Candidate updatedCandidate = candidateService.updateCandidateProfile(id, candidateDto);
        if (updatedCandidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCandidate);
    }

    /**
     * Deletes a candidate profile.
     * <p>
     * This endpoint allows candidates to delete their own profile or
     * administrators to delete any candidate profile.
     * </p>
     *
     * @param id The ID of the candidate profile to delete
     * @return ResponseEntity with a success response or a not-found status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'ADMIN')")
    public ResponseEntity<?> deleteCandidateProfile(@PathVariable String id) {
        boolean deleted = candidateService.deleteCandidateProfile(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Candidate profile deleted successfully"));
    }

    /**
     * Searches for candidates based on multiple criteria.
     * <p>
     * This endpoint allows recruiters to search for candidates based on
     * skills, minimum experience, location, and experience level. All
     * parameters are optional and can be combined for more specific searches.
     * </p>
     *
     * @param skills The list of skills to search for
     * @param minExperience The minimum years of experience required
     * @param location The preferred location of candidates
     * @param experienceLevel The experience level category (e.g., ENTRY, JUNIOR, MID, SENIOR)
     * @return ResponseEntity with a list of matching candidates
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> searchCandidates(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel) {

        List<Candidate> candidates = candidateService.searchCandidates(skills, minExperience, location, experienceLevel);
        return ResponseEntity.ok(candidates);
    }

    /**
     * Retrieves candidates who have a specific skill.
     * <p>
     * This endpoint allows recruiters to find candidates with a particular skill.
     * </p>
     *
     * @param skill The skill to search for
     * @return ResponseEntity with a list of candidates having the specified skill
     */
    @GetMapping("/skills/{skill}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getCandidatesBySkill(@PathVariable String skill) {
        List<Candidate> candidates = candidateService.getCandidatesBySkill(skill);
        return ResponseEntity.ok(candidates);
    }

    /**
     * Retrieves candidates with a specific experience level.
     * <p>
     * This endpoint allows recruiters to find candidates based on their
     * experience level category (e.g., ENTRY, JUNIOR, MID, SENIOR, EXECUTIVE).
     * </p>
     *
     * @param level The experience level to search for
     * @return ResponseEntity with a list of candidates at the specified experience level
     */
    @GetMapping("/experience/{level}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getCandidatesByExperienceLevel(@PathVariable String level) {
        List<Candidate> candidates = candidateService.getCandidatesByExperienceLevel(level);
        return ResponseEntity.ok(candidates);
    }

    /**
     * Updates a candidate's resume URL.
     * <p>
     * This endpoint allows candidates to update the URL to their resume
     * without changing other profile information.
     * </p>
     *
     * @param id The ID of the candidate profile
     * @param resumeUrl The new resume URL
     * @return ResponseEntity with the updated candidate profile or a not-found status
     */
    @PutMapping("/{id}/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateResume(@PathVariable String id, @RequestParam String resumeUrl) {
        Candidate candidate = candidateService.updateResume(id, resumeUrl);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    /**
     * Updates a candidate's availability status.
     * <p>
     * This endpoint allows candidates to indicate whether they are
     * currently available for job opportunities.
     * </p>
     *
     * @param id The ID of the candidate profile
     * @param isAvailable The availability status (true if available, false if not)
     * @return ResponseEntity with the updated candidate profile or a not-found status
     */
    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateAvailability(@PathVariable String id, @RequestParam Boolean isAvailable) {
        Candidate candidate = candidateService.updateAvailability(id, isAvailable);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    /**
     * Retrieves all candidates who are currently available for job opportunities.
     * <p>
     * This endpoint allows recruiters to find candidates who have indicated
     * they are actively looking for job opportunities.
     * </p>
     *
     * @return ResponseEntity with a list of available candidates
     */
    @GetMapping("/available")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getAvailableCandidates() {
        List<Candidate> candidates = candidateService.getAvailableCandidates();
        return ResponseEntity.ok(candidates);
    }

    /**
     * Retrieves statistics about a candidate's profile and job applications.
     * <p>
     * This endpoint provides statistical information about a candidate's
     * job application history, including counts of applications by status,
     * skills, experience, and education details.
     * </p>
     *
     * @param id The ID of the candidate profile
     * @return ResponseEntity with statistical information about the candidate
     */
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateStats(@PathVariable String id) {
        Map<String, Object> stats = candidateService.getCandidateStats(id);
        return ResponseEntity.ok(stats);
    }
}