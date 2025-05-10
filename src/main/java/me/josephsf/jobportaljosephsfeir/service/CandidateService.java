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

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;

    public CandidateService(CandidateRepository candidateRepository, ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public Candidate getCandidateByUserId(String userId) {
        return candidateRepository.findByUserId(userId).orElse(null);
    }

    public Candidate createCandidateProfile(CandidateDto candidateDto) {
        // Check if profile already exists for this user
        if (candidateRepository.existsByUserId(candidateDto.getUserId())) {
            return null;
        }

        Candidate candidate = new Candidate();
        mapDtoToCandidate(candidateDto, candidate);
        return candidateRepository.save(candidate);
    }

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

    public boolean deleteCandidateProfile(String id) {
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
            return true;
        }
        return false;
    }

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

    public List<Candidate> getCandidatesBySkill(String skill) {
        return candidateRepository.findBySkills(List.of(skill));
    }

    public List<Candidate> getCandidatesByExperienceLevel(String experienceLevel) {
        return candidateRepository.findByExperienceLevel(experienceLevel);
    }

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

    public List<Candidate> getAvailableCandidates() {
        return candidateRepository.findAll().stream()
                .filter(candidate -> candidate.getIsAvailable() != null && candidate.getIsAvailable())
                .toList();
    }

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