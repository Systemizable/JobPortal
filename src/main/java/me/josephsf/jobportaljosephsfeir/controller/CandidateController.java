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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getCandidateById(@PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateByUserId(@PathVariable String userId) {
        Candidate candidate = candidateService.getCandidateByUserId(userId);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateCandidateProfile(@PathVariable String id, @Valid @RequestBody CandidateDto candidateDto) {
        Candidate updatedCandidate = candidateService.updateCandidateProfile(id, candidateDto);
        if (updatedCandidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCandidate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'ADMIN')")
    public ResponseEntity<?> deleteCandidateProfile(@PathVariable String id) {
        boolean deleted = candidateService.deleteCandidateProfile(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Candidate profile deleted successfully"));
    }

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

    @GetMapping("/skills/{skill}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getCandidatesBySkill(@PathVariable String skill) {
        List<Candidate> candidates = candidateService.getCandidatesBySkill(skill);
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/experience/{level}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getCandidatesByExperienceLevel(@PathVariable String level) {
        List<Candidate> candidates = candidateService.getCandidatesByExperienceLevel(level);
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateResume(@PathVariable String id, @RequestParam String resumeUrl) {
        Candidate candidate = candidateService.updateResume(id, resumeUrl);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateAvailability(@PathVariable String id, @RequestParam Boolean isAvailable) {
        Candidate candidate = candidateService.updateAvailability(id, isAvailable);
        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getAvailableCandidates() {
        List<Candidate> candidates = candidateService.getAvailableCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateStats(@PathVariable String id) {
        Map<String, Object> stats = candidateService.getCandidateStats(id);
        return ResponseEntity.ok(stats);
    }
}