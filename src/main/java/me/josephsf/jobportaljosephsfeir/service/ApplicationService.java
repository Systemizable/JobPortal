package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public JobApplication applyToJob(ApplicationDto applicationDto) {
        // Check if already applied
        Optional<JobApplication> existingApplication = applicationRepository
                .findByCandidateIdAndJobId(applicationDto.getCandidateId(), applicationDto.getJobId());

        if (existingApplication.isPresent()) {
            return null; // Already applied
        }

        JobApplication application = new JobApplication();
        application.setJobId(applicationDto.getJobId());
        application.setCandidateId(applicationDto.getCandidateId());
        application.setCoverLetter(applicationDto.getCoverLetter());
        application.setResumeUrl(applicationDto.getResumeUrl());
        application.setStatus("APPLIED");
        application.setApplicationDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public JobApplication getApplicationById(String id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public Page<JobApplication> getApplicationsByCandidate(String candidateId, Pageable pageable) {
        return applicationRepository.findByCandidateId(candidateId, pageable);
    }

    public Page<JobApplication> getApplicationsByJob(String jobId, Pageable pageable) {
        return applicationRepository.findByJobId(jobId, pageable);
    }

    public List<JobApplication> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    public JobApplication updateApplicationStatus(String id, String status, String reviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setStatus(status);
            application.setReviewDate(LocalDateTime.now());
            if (reviewNotes != null) {
                application.setReviewNotes(reviewNotes);
            }
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    public JobApplication addReviewNotes(String id, String reviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setReviewNotes(reviewNotes);
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    public JobApplication addInterviewNotes(String id, String interviewNotes) {
        Optional<JobApplication> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            JobApplication application = applicationOptional.get();
            application.setInterviewNotes(interviewNotes);
            application.setUpdatedAt(LocalDateTime.now());
            return applicationRepository.save(application);
        }
        return null;
    }

    public boolean withdrawApplication(String id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String, Long> getApplicationStats(String jobId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", applicationRepository.countByJobId(jobId));
        stats.put("applied", applicationRepository.countByJobIdAndStatus(jobId, "APPLIED"));
        stats.put("reviewing", applicationRepository.countByJobIdAndStatus(jobId, "REVIEWING"));
        stats.put("shortlisted", applicationRepository.countByJobIdAndStatus(jobId, "SHORTLISTED"));
        stats.put("rejected", applicationRepository.countByJobIdAndStatus(jobId, "REJECTED"));
        stats.put("accepted", applicationRepository.countByJobIdAndStatus(jobId, "ACCEPTED"));
        return stats;
    }

    public List<JobApplication> getRecentApplications(int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        return applicationRepository.findByApplicationDateAfter(sinceDate);
    }

    public long countApplicationsByCandidate(String candidateId) {
        return applicationRepository.countByCandidateId(candidateId);
    }

    public List<JobApplication> getApplicationsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return applicationRepository.findByApplicationDateBetween(startDate, endDate);
    }
}