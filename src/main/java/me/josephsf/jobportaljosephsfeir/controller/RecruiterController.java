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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {
    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getRecruiterById(@PathVariable String id) {
        Recruiter recruiter = recruiterService.getRecruiterById(id);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getRecruiterByUserId(@PathVariable String userId) {
        Recruiter recruiter = recruiterService.getRecruiterByUserId(userId);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateRecruiterProfile(@PathVariable String id, @Valid @RequestBody RecruiterDto recruiterDto) {
        Recruiter updatedRecruiter = recruiterService.updateRecruiterProfile(id, recruiterDto);
        if (updatedRecruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRecruiter);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> deleteRecruiterProfile(@PathVariable String id) {
        boolean deleted = recruiterService.deleteRecruiterProfile(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDto(true, "Recruiter profile deleted successfully"));
    }

    @GetMapping("/company/{companyName}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getRecruitersByCompany(@PathVariable String companyName) {
        List<Recruiter> recruiters = recruiterService.getRecruitersByCompany(companyName);
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> searchRecruiters(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry) {

        List<Recruiter> recruiters = recruiterService.searchRecruiters(companyName, location, industry);
        return ResponseEntity.ok(recruiters);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifyRecruiter(@PathVariable String id) {
        Recruiter recruiter = recruiterService.verifyRecruiter(id);
        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> getVerifiedRecruiters() {
        List<Recruiter> recruiters = recruiterService.getVerifiedRecruiters();
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getRecruiterStats(@PathVariable String id) {
        Map<String, Object> stats = recruiterService.getRecruiterStats(id);
        return ResponseEntity.ok(stats);
    }
}