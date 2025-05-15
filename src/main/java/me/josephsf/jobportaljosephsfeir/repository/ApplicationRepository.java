package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating JobApplication documents in MongoDB.
 * <p>
 * This repository provides methods for CRUD operations on JobApplication entities, as well as
 * custom queries for retrieving applications based on various criteria such as candidate ID,
 * job ID, application status, date ranges, and combinations of these factors. It leverages
 * Spring Data MongoDB's query method naming conventions for defining custom finder methods
 * and supports pagination for result sets.
 * </p>
 *
 * <p>As a Spring Data repository, it automatically provides standard operations like:</p>
 * <ul>
 *   <li>Saving and updating job applications</li>
 *   <li>Deleting job applications</li>
 *   <li>Finding job applications by ID</li>
 *   <li>Retrieving all job applications</li>
 * </ul>
 *
 * <p>The interface also defines custom queries for application tracking, candidate and
 * recruiter dashboards, and application analytics.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see me.josephsf.jobportaljosephsfeir.model.JobApplication
 * @see org.springframework.data.mongodb.repository.MongoRepository
 */
@Repository
public interface ApplicationRepository extends MongoRepository<JobApplication, String> {

    /**
     * Finds all applications submitted by a specific candidate.
     *
     * @param candidateId the ID of the candidate
     * @return a list of job applications from the specified candidate
     */
    List<JobApplication> findByCandidateId(String candidateId);

    /**
     * Finds all applications submitted for a specific job.
     *
     * @param jobId the ID of the job
     * @return a list of job applications for the specified job
     */
    List<JobApplication> findByJobId(String jobId);

    /**
     * Finds all applications with a specific status.
     *
     * @param status the application status to filter by (e.g., APPLIED, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED)
     * @return a list of job applications with the specified status
     */
    List<JobApplication> findByStatus(String status);

    /**
     * Finds an application by both candidate ID and job ID.
     * This is primarily used to check if a candidate has already applied to a specific job.
     *
     * @param candidateId the ID of the candidate
     * @param jobId the ID of the job
     * @return an Optional containing the application if found, or empty if not
     */
    Optional<JobApplication> findByCandidateIdAndJobId(String candidateId, String jobId);

    /**
     * Finds applications submitted by a specific candidate with pagination support.
     *
     * @param candidateId the ID of the candidate
     * @param pageable pagination and sorting information
     * @return a page of job applications from the specified candidate
     */
    Page<JobApplication> findByCandidateId(String candidateId, Pageable pageable);

    /**
     * Finds applications submitted for a specific job with pagination support.
     *
     * @param jobId the ID of the job
     * @param pageable pagination and sorting information
     * @return a page of job applications for the specified job
     */
    Page<JobApplication> findByJobId(String jobId, Pageable pageable);

    /**
     * Finds applications with a specific status with pagination support.
     *
     * @param status the application status to filter by
     * @param pageable pagination and sorting information
     * @return a page of job applications with the specified status
     */
    Page<JobApplication> findByStatus(String status, Pageable pageable);

    /**
     * Finds applications submitted within a specific date range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return a list of job applications submitted within the specified period
     */
    List<JobApplication> findByApplicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Counts the number of applications for a specific job with a specific status.
     * This is useful for generating application statistics.
     *
     * @param jobId the ID of the job
     * @param status the application status to count
     * @return the count of applications matching the criteria
     */
    long countByJobIdAndStatus(String jobId, String status);

    /**
     * Counts the total number of applications submitted by a specific candidate.
     *
     * @param candidateId the ID of the candidate
     * @return the count of applications submitted by the candidate
     */
    long countByCandidateId(String candidateId);

    /**
     * Counts the total number of applications for a specific job.
     *
     * @param jobId the ID of the job
     * @return the count of applications for the job
     */
    long countByJobId(String jobId);

    /**
     * Finds applications submitted after a specific date.
     * This is useful for retrieving recent applications.
     *
     * @param date the threshold date
     * @return a list of job applications submitted after the specified date
     */
    List<JobApplication> findByApplicationDateAfter(LocalDateTime date);

    /**
     * Finds applications with any of the specified statuses.
     *
     * @param statuses a list of application statuses to match
     * @return a list of job applications with any of the specified statuses
     */
    List<JobApplication> findByStatusIn(List<String> statuses);
}