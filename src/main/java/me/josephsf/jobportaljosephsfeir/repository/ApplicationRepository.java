package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<JobApplication, String> {

    // Find applications by candidate ID
    List<JobApplication> findByCandidateId(String candidateId);

    // Find applications by job ID
    List<JobApplication> findByJobId(String jobId);

    // Find applications by status
    List<JobApplication> findByStatus(String status);

    // Find applications by candidate and job (check if already applied)
    Optional<JobApplication> findByCandidateIdAndJobId(String candidateId, String jobId);

    // Find applications by candidate with pagination
    Page<JobApplication> findByCandidateId(String candidateId, Pageable pageable);

    // Find applications by job with pagination
    Page<JobApplication> findByJobId(String jobId, Pageable pageable);

    // Find applications by status with pagination
    Page<JobApplication> findByStatus(String status, Pageable pageable);

    // Find applications submitted within date range
    List<JobApplication> findByApplicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Count applications by status for a job
    long countByJobIdAndStatus(String jobId, String status);

    // Count applications by candidate
    long countByCandidateId(String candidateId);

    // Count applications by job ID (ADDED THIS)
    long countByJobId(String jobId);

    // Find recent applications (last N days)
    List<JobApplication> findByApplicationDateAfter(LocalDateTime date);

    // Find applications by multiple statuses
    List<JobApplication> findByStatusIn(List<String> statuses);
}