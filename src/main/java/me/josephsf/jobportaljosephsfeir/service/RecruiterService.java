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

@Service
public class RecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;

    public RecruiterService(RecruiterRepository recruiterRepository, JobRepository jobRepository) {
        this.recruiterRepository = recruiterRepository;
        this.jobRepository = jobRepository;
    }

    public Recruiter getRecruiterById(String id) {
        return recruiterRepository.findById(id).orElse(null);
    }

    public Recruiter getRecruiterByUserId(String userId) {
        return recruiterRepository.findByUserId(userId).orElse(null);
    }

    public Recruiter createRecruiterProfile(RecruiterDto recruiterDto) {
        // Check if profile already exists for this user
        if (recruiterRepository.existsByUserId(recruiterDto.getUserId())) {
            return null;
        }

        Recruiter recruiter = new Recruiter();
        mapDtoToRecruiter(recruiterDto, recruiter);
        return recruiterRepository.save(recruiter);
    }

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

    public boolean deleteRecruiterProfile(String id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Recruiter> getRecruitersByCompany(String companyName) {
        return recruiterRepository.findByCompanyName(companyName);
    }

    public List<Recruiter> searchRecruiters(String companyName, String location, String industry) {
        if (companyName != null && !companyName.isEmpty()) {
            return recruiterRepository.findByCompanyNameContainingIgnoreCase(companyName);
        }
        if (location != null && !location.isEmpty()) {
            return recruiterRepository.findByLocationContainingIgnoreCase(location);
        }
        return recruiterRepository.findAll();
    }

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

    public List<Recruiter> getVerifiedRecruiters() {
        return recruiterRepository.findByIsVerified(true);
    }

    public Map<String, Object> getRecruiterStats(String recruiterId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", jobRepository.countByRecruiterIdAndIsActive(recruiterId, true));
        stats.put("activeJobs", jobRepository.countByRecruiterIdAndIsActive(recruiterId, true));

        // Get recent jobs
        List<me.josephsf.jobportaljosephsfeir.model.Job> recentJobs = jobRepository.findByRecruiterId(recruiterId);
        stats.put("recentJobs", recentJobs.size() > 5 ? recentJobs.subList(0, 5) : recentJobs);

        return stats;
    }

    public boolean isVerified(String recruiterId) {
        Recruiter recruiter = getRecruiterById(recruiterId);
        return recruiter != null && recruiter.getIsVerified();
    }

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