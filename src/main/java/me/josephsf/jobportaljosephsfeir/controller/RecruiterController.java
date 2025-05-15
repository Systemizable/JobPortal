package me.josephsf.jobportaljosephsfeir.controller;

import me.josephsf.jobportaljosephsfeir.dto.RecruiterDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import me.josephsf.jobportaljosephsfeir.service.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing recruiter profiles in the Job Portal system.
 * <p>
 * This controller handles HTTP requests related to recruiter profiles, including
 * creating, retrieving, updating, and deleting recruiter information. It also provides
 * endpoints for searching recruiters by various criteria such as company name and location.
 * Additional functionality includes recruiter verification by administrators and
 * retrieving recruiter statistics.
 * </p>
 * <p>
 * Most operations in this controller require either RECRUITER or ADMIN role authorization,
 * with certain operations like verification restricted to administrators only.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {
    private final RecruiterService recruiterService;

    /**
     * Constructs a RecruiterController with the specified service.
     *
     * @param recruiterService The service for recruiter-related operations
     */
    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    /**
     * Retrieves a recruiter profile by its ID.
     * <p>
     * This endpoint is accessible to recruiters and administrators.
     * It returns the recruiter's profile information if found.
     * </p>
     *
     * @param id The ID of the recruiter profile to retrieve
     * @return ResponseEntity with the recruiter profile or a not-found status
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getRecruiterById(@PathVariable String id) {
        Recruiter recruiter = recruiterService.getRecruiterById(id);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

    /**
     * Retrieves a recruiter profile by the associated user ID.
     * <p>
     * This endpoint allows recruiters to access their own profile by their user ID.
     * It is primarily used for profile setup and management.
     * </p>
     *
     * @param userId The ID of the user associated with the recruiter profile
     * @return ResponseEntity with the recruiter profile or a not-found status
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getRecruiterByUserId(@PathVariable String userId) {
        Recruiter recruiter = recruiterService.getRecruiterByUserId(userId);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

    /**
     * Creates a new recruiter profile.
     * <p>
     * This endpoint allows users with the RECRUITER role to create their profile
     * by providing company and professional information. A user can only have
     * one recruiter profile.
     * </p>
     *
     * @param recruiterDto The DTO containing recruiter profile information
     * @return ResponseEntity with the created profile or an error response
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createRecruiterProfile(@Valid @RequestBody RecruiterDto recruiterDto) {
        Recruiter recruiter = recruiterService.createRecruiterProfile(recruiterDto);
        if (recruiter == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(false, "Recruiter profile already exists for this user"));
        }
        return ResponseEntity.ok(recruiter);
    }

    /**
     * Updates an existing recruiter profile.
     * <p>
     * This endpoint allows recruiters to update their profile information
     * such as company details, contact information, and professional description.
     * </p>
     *
     * @param id The ID of the recruiter profile to update
     * @param recruiterDto The DTO containing updated profile information
     * @return ResponseEntity with the updated profile or a not-found status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateRecruiterProfile(@PathVariable String id, @Valid @RequestBody RecruiterDto recruiterDto) {
        Recruiter updatedRecruiter = recruiterService.updateRecruiterProfile(id, recruiterDto);
        if (updatedRecruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRecruiter);
    }

    /**
     * Deletes a recruiter profile.
     * <p>
     * This endpoint allows recruiters to delete their own profile or
     * administrators to delete any recruiter profile.
     * </p>
     *
     * @param id The ID of the recruiter profile to delete
     * @return ResponseEntity with a success response or a not-found status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> deleteRecruiterProfile(@PathVariable String id) {
        boolean deleted = recruiterService.deleteRecruiterProfile(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Recruiter profile deleted successfully"));
    }

    /**
     * Retrieves recruiters by company name.
     * <p>
     * This endpoint allows recruiters and administrators to find all recruiters
     * associated with a specific company.
     * </p>
     *
     * @param companyName The name of the company to search for
     * @return ResponseEntity with a list of recruiters from the specified company
     */
    @GetMapping("/company/{companyName}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getRecruitersByCompany(@PathVariable String companyName) {
        List<Recruiter> recruiters = recruiterService.getRecruitersByCompany(companyName);
        return ResponseEntity.ok(recruiters);
    }

    /**
     * Searches for recruiters based on multiple criteria.
     * <p>
     * This endpoint allows recruiters and administrators to search for recruiters
     * based on company name, location, and industry. All parameters are optional
     * and can be combined for more specific searches.
     * </p>
     *
     * @param companyName The company name to search for
     * @param location The location to search for
     * @param industry The industry to search for
     * @return ResponseEntity with a list of matching recruiters
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> searchRecruiters(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry) {

        List<Recruiter> recruiters = recruiterService.searchRecruiters(companyName, location, industry);
        return ResponseEntity.ok(recruiters);
    }

    /**
     * Verifies a recruiter profile.
     * <p>
     * This endpoint allows administrators to verify recruiter profiles,
     * which can provide additional credibility and visibility to verified recruiters.
     * Only users with the ADMIN role can perform this operation.
     * </p>
     *
     * @param id The ID of the recruiter profile to verify
     * @return ResponseEntity with the updated recruiter profile or a not-found status
     */
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifyRecruiter(@PathVariable String id) {
        Recruiter recruiter = recruiterService.verifyRecruiter(id);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

    /**
     * Retrieves all verified recruiter profiles.
     * <p>
     * This endpoint allows recruiters and administrators to view all
     * recruiter profiles that have been verified by administrators.
     * </p>
     *
     * @return ResponseEntity with a list of verified recruiters
     */
    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getVerifiedRecruiters() {
        List<Recruiter> recruiters = recruiterService.getVerifiedRecruiters();
        return ResponseEntity.ok(recruiters);
    }

    /**
     * Retrieves statistics about a recruiter's profile and job postings.
     * <p>
     * This endpoint provides statistical information about a recruiter's
     * job posting history, including counts of active jobs, applications
     * received, and other recruitment metrics.
     * </p>
     *
     * @param id The ID of the recruiter profile
     * @return ResponseEntity with statistical information about the recruiter
     */
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getRecruiterStats(@PathVariable String id) {
        Map<String, Object> stats = recruiterService.getRecruiterStats(id);
        return ResponseEntity.ok(stats);
    }
}