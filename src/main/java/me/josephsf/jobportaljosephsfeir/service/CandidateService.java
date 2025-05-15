package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.CandidateDto;
import me.josephsf.jobportaljosephsfeir.model.Candidate;
import me.josephsf.jobportaljosephsfeir.repository.CandidateRepository;
import me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing candidate data and operations in the Job Portal system.
 *
 * <p>This service provides methods for creating, updating, and managing candidate profiles,
 * as well as searching candidates based on various criteria. It interacts with the MongoDB
 * database through the CandidateRepository and handles related business logic.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * Constructs a new CandidateService with required dependencies.
     *
     * @param candidateRepository Repository for candidate data operations
     * @param applicationRepository Repository for job application data operations
     */
    public CandidateService(CandidateRepository candidateRepository, ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Retrieves a candidate by their ID.
     *
     * @param id The unique identifier of the candidate
     * @return The candidate if found, null otherwise
     */
    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a candidate by their user ID.
     *
     * @param userId The user ID associated with the candidate profile
     * @return The candidate if found, null otherwise
     */
    public Candidate getCandidateByUserId(String userId) {
        return candidateRepository.findByUserId(userId).orElse(null);
    }

    /**
     * Creates a new candidate profile.
     *
     * @param candidateDto The data transfer object containing candidate information
     * @return The created candidate profile or null if a profile already exists for the user
     */
    public Candidate createCandidateProfile(CandidateDto candidateDto) {
        // Check if profile already exists for this user
        if (candidateRepository.existsByUserId(candidateDto.getUserId())) {
            return null;
        }

        Candidate candidate = new Candidate();
        mapDtoToCandidate(candidateDto, candidate);
        return candidateRepository.save(candidate);
    }

    /**
     * Updates an existing candidate profile.
     *
     * @param id The unique identifier of the candidate to update
     * @param candidateDto The data transfer object containing updated candidate information
     * @return The updated candidate profile or null if the candidate is not found
     */
    public Candidate updateCandidateProfile(String id, CandidateDto candidateDto) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            mapDtoToCandidate(candidateDto, candidate);
            candidate.setUpdatedAt(LocalDateTime.now());
            return candidateRepository.save(candidate);
        }
        return null;
    }

    /**
     * Deletes a candidate profile.
     *
     * @param id The unique identifier of the candidate to delete
     * @return true if the candidate was successfully deleted, false otherwise
     */
    public boolean deleteCandidateProfile(String id) {
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Searches for candidates based on various criteria.
     *
     * @param skills List of skills to search for
     * @param minExperience Minimum years of experience required
     * @param location Geographic location to filter by
     * @param experienceLevel Experience level (e.g., ENTRY, JUNIOR, MID, SENIOR, EXECUTIVE)
     * @return List of candidates matching the search criteria
     */
    public List<Candidate> searchCandidates(List<String> skills, Integer minExperience, String location, String experienceLevel) {
        if (skills != null && minExperience != null && location != null) {
            return candidateRepository.searchCandidates(skills, minExperience, location);
        }
        if (skills != null && !skills.isEmpty()) {
            return candidateRepository.findBySkills(skills);
        }
        if (experienceLevel != null && !experienceLevel.isEmpty()) {
            return candidateRepository.findByExperienceLevel(experienceLevel);
        }
        if (location != null && !location.isEmpty()) {
            return candidateRepository.findByLocationContainingIgnoreCase(location);
        }
        if (minExperience != null) {
            return candidateRepository.findByYearsOfExperienceRange(minExperience, Integer.MAX_VALUE);
        }
        return candidateRepository.findAll();
    }

    /**
     * Finds candidates who have a specific skill.
     *
     * @param skill The skill to search for
     * @return List of candidates with the specified skill
     */
    public List<Candidate> getCandidatesBySkill(String skill) {
        return candidateRepository.findBySkills(List.of(skill));
    }

    /**
     * Finds candidates with a specific experience level.
     *
     * @param experienceLevel The experience level to filter by
     * @return List of candidates with the specified experience level
     */
    public List<Candidate> getCandidatesByExperienceLevel(String experienceLevel) {
        return candidateRepository.findByExperienceLevel(experienceLevel);
    }

    /**
     * Updates the resume URL for a candidate.
     *
     * @param id The unique identifier of the candidate
     * @param resumeUrl The new resume URL
     * @return The updated candidate or null if the candidate is not found
     */
    public Candidate updateResume(String id, String resumeUrl) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setResumeUrl(resumeUrl);
            candidate.setUpdatedAt(LocalDateTime.now());
            return candidateRepository.save(candidate);
        }
        return null;
    }

    /**
     * Updates the availability status of a candidate.
     *
     * @param id The unique identifier of the candidate
     * @param isAvailable The new availability status
     * @return The updated candidate or null if the candidate is not found
     */
    public Candidate updateAvailability(String id, Boolean isAvailable) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setIsAvailable(isAvailable);
            candidate.setUpdatedAt(LocalDateTime.now());
            return candidateRepository.save(candidate);
        }
        return null;
    }

    /**
     * Retrieves all candidates who are marked as available.
     *
     * @return List of available candidates
     */
    public List<Candidate> getAvailableCandidates() {
        return candidateRepository.findAll().stream()
                .filter(candidate -> candidate.getIsAvailable() != null && candidate.getIsAvailable())
                .toList();
    }

    /**
     * Generates statistics for a candidate.
     *
     * @param candidateId The unique identifier of the candidate
     * @return A map containing various statistics about the candidate's applications and profile
     */
    public Map<String, Object> getCandidateStats(String candidateId) {
        Map<String, Object> stats = new HashMap<>();

        // Get application statistics
        long totalApplications = applicationRepository.countByCandidateId(candidateId);
        stats.put("totalApplications", totalApplications);

        // Count applications by status
        stats.put("appliedApplications", applicationRepository.findByCandidateId(candidateId).stream()
                .filter(app -> "APPLIED".equals(app.getStatus()))
                .count());
        stats.put("reviewingApplications", applicationRepository.findByCandidateId(candidateId).stream()
                .filter(app -> "REVIEWING".equals(app.getStatus()))
                .count());
        stats.put("shortlistedApplications", applicationRepository.findByCandidateId(candidateId).stream()
                .filter(app -> "SHORTLISTED".equals(app.getStatus()))
                .count());
        stats.put("acceptedApplications", applicationRepository.findByCandidateId(candidateId).stream()
                .filter(app -> "ACCEPTED".equals(app.getStatus()))
                .count());

        // Get candidate profile info
        Candidate candidate = getCandidateById(candidateId);
        if (candidate != null) {
            stats.put("skillsCount", candidate.getSkills() != null ? candidate.getSkills().size() : 0);
            stats.put("experienceCount", candidate.getExperience() != null ? candidate.getExperience().size() : 0);
            stats.put("educationCount", candidate.getEducation() != null ? candidate.getEducation().size() : 0);
        }

        return stats;
    }

    /**
     * Maps data from a CandidateDto to a Candidate entity.
     *
     * @param dto The source DTO containing candidate data
     * @param candidate The target Candidate entity to update
     */
    private void mapDtoToCandidate(CandidateDto dto, Candidate candidate) {
        candidate.setUserId(dto.getUserId());
        candidate.setFirstName(dto.getFirstName());
        candidate.setLastName(dto.getLastName());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setLocation(dto.getLocation());
        candidate.setCurrentTitle(dto.getCurrentTitle());
        candidate.setExperienceLevel(dto.getExperienceLevel());
        candidate.setYearsOfExperience(dto.getYearsOfExperience());
        candidate.setSkills(dto.getSkills());
        candidate.setExperience(dto.getExperience());
        candidate.setEducation(dto.getEducation());
        candidate.setResumeUrl(dto.getResumeUrl());
        candidate.setProfileSummary(dto.getProfileSummary());
        candidate.setLinkedInUrl(dto.getLinkedInUrl());
        candidate.setPortfolioUrl(dto.getPortfolioUrl());
        candidate.setExpectedSalary(dto.getExpectedSalary());
        candidate.setIsAvailable(dto.getIsAvailable());

        if (candidate.getId() == null) {
            candidate.setCreatedAt(LocalDateTime.now());
        }
        candidate.setUpdatedAt(LocalDateTime.now());
    }
}