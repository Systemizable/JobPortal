package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.RecruiterDto;
import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import me.josephsf.jobportaljosephsfeir.repository.RecruiterRepository;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing recruiter data and operations in the Job Portal system.
 *
 * <p>This service provides methods for creating, retrieving, updating, and deleting recruiter profiles,
 * as well as searching for recruiters and calculating recruiter statistics. It interacts with the MongoDB
 * database through the RecruiterRepository and JobRepository, handling related business logic.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class RecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;

    /**
     * Constructs a new RecruiterService with required dependencies.
     *
     * @param recruiterRepository Repository for recruiter data operations
     * @param jobRepository Repository for job data operations
     */
    public RecruiterService(RecruiterRepository recruiterRepository, JobRepository jobRepository) {
        this.recruiterRepository = recruiterRepository;
        this.jobRepository = jobRepository;
    }

    /**
     * Retrieves a recruiter by their ID.
     *
     * @param id The unique identifier of the recruiter
     * @return The recruiter if found, null otherwise
     */
    public Recruiter getRecruiterById(String id) {
        return recruiterRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a recruiter by their user ID.
     *
     * @param userId The user ID associated with the recruiter profile
     * @return The recruiter if found, null otherwise
     */
    public Recruiter getRecruiterByUserId(String userId) {
        return recruiterRepository.findByUserId(userId).orElse(null);
    }

    /**
     * Creates a new recruiter profile.
     *
     * @param recruiterDto The data transfer object containing recruiter information
     * @return The created recruiter profile or null if a profile already exists for the user
     */
    public Recruiter createRecruiterProfile(RecruiterDto recruiterDto) {
        // Check if profile already exists for this user
        if (recruiterRepository.existsByUserId(recruiterDto.getUserId())) {
            return null;
        }

        Recruiter recruiter = new Recruiter();
        mapDtoToRecruiter(recruiterDto, recruiter);
        return recruiterRepository.save(recruiter);
    }

    /**
     * Updates an existing recruiter profile.
     *
     * @param id The unique identifier of the recruiter to update
     * @param recruiterDto The data transfer object containing updated recruiter information
     * @return The updated recruiter profile or null if the recruiter is not found
     */
    public Recruiter updateRecruiterProfile(String id, RecruiterDto recruiterDto) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(id);
        if (recruiterOptional.isPresent()) {
            Recruiter recruiter = recruiterOptional.get();
            mapDtoToRecruiter(recruiterDto, recruiter);
            recruiter.setUpdatedAt(LocalDateTime.now());
            return recruiterRepository.save(recruiter);
        }
        return null;
    }

    /**
     * Deletes a recruiter profile.
     *
     * @param id The unique identifier of the recruiter to delete
     * @return true if the recruiter was successfully deleted, false otherwise
     */
    public boolean deleteRecruiterProfile(String id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Finds recruiters by company name.
     *
     * @param companyName The company name to search for
     * @return List of recruiters associated with the specified company
     */
    public List<Recruiter> getRecruitersByCompany(String companyName) {
        return recruiterRepository.findByCompanyName(companyName);
    }

    /**
     * Searches for recruiters based on various criteria.
     *
     * @param companyName The company name to filter by
     * @param location The location to filter by
     * @param industry The industry to filter by
     * @return List of recruiters matching the search criteria
     */
    public List<Recruiter> searchRecruiters(String companyName, String location, String industry) {
        if (companyName != null && !companyName.isEmpty()) {
            return recruiterRepository.findByCompanyNameContainingIgnoreCase(companyName);
        }
        if (location != null && !location.isEmpty()) {
            return recruiterRepository.findByLocationContainingIgnoreCase(location);
        }
        return recruiterRepository.findAll();
    }

    /**
     * Verifies a recruiter profile.
     * Verification is an administrative action that confirms the legitimacy of a recruiter.
     *
     * @param id The unique identifier of the recruiter to verify
     * @return The updated, verified recruiter or null if the recruiter is not found
     */
    public Recruiter verifyRecruiter(String id) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(id);
        if (recruiterOptional.isPresent()) {
            Recruiter recruiter = recruiterOptional.get();
            recruiter.setIsVerified(true);
            recruiter.setUpdatedAt(LocalDateTime.now());
            return recruiterRepository.save(recruiter);
        }
        return null;
    }

    /**
     * Retrieves all verified recruiters.
     *
     * @return List of verified recruiter profiles
     */
    public List<Recruiter> getVerifiedRecruiters() {
        return recruiterRepository.findByIsVerified(true);
    }

    /**
     * Generates statistics for a recruiter.
     *
     * @param recruiterId The unique identifier of the recruiter
     * @return A map containing various statistics about the recruiter's job postings
     */
    public Map<String, Object> getRecruiterStats(String recruiterId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", jobRepository.countByRecruiterIdAndIsActive(recruiterId, true));
        stats.put("activeJobs", jobRepository.countByRecruiterIdAndIsActive(recruiterId, true));

        // Get recent jobs
        List<me.josephsf.jobportaljosephsfeir.model.Job> recentJobs = jobRepository.findByRecruiterId(recruiterId);
        stats.put("recentJobs", recentJobs.size() > 5 ? recentJobs.subList(0, 5) : recentJobs);

        return stats;
    }

    /**
     * Checks if a recruiter is verified.
     *
     * @param recruiterId The unique identifier of the recruiter
     * @return true if the recruiter is verified, false otherwise
     */
    public boolean isVerified(String recruiterId) {
        Recruiter recruiter = getRecruiterById(recruiterId);
        return recruiter != null && recruiter.getIsVerified();
    }

    /**
     * Maps data from a RecruiterDto to a Recruiter entity.
     *
     * @param dto The source DTO containing recruiter data
     * @param recruiter The target Recruiter entity to update
     */
    private void mapDtoToRecruiter(RecruiterDto dto, Recruiter recruiter) {
        recruiter.setUserId(dto.getUserId());
        recruiter.setCompanyName(dto.getCompanyName());
        recruiter.setCompanySize(dto.getCompanySize());
        recruiter.setLocation(dto.getLocation());
        recruiter.setIndustry(dto.getIndustry());
        recruiter.setDepartment(dto.getDepartment());
        recruiter.setPosition(dto.getPosition());
        recruiter.setPhoneNumber(dto.getPhoneNumber());
        recruiter.setLinkedInUrl(dto.getLinkedInUrl());
        recruiter.setCompanyWebsite(dto.getCompanyWebsite());
        recruiter.setCompanyDescription(dto.getCompanyDescription());

        if (recruiter.getId() == null) {
            recruiter.setCreatedAt(LocalDateTime.now());
        }
        recruiter.setUpdatedAt(LocalDateTime.now());
    }
}