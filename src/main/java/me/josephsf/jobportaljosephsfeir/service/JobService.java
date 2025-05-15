package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.JobDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing job data and operations in the Job Portal system.
 *
 * <p>This service provides methods for creating, retrieving, updating, and deleting job postings,
 * as well as searching and filtering jobs based on various criteria. It interacts with the MongoDB
 * database through the JobRepository and handles related business logic.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class JobService {
    private final JobRepository jobRepository;

    /**
     * Constructs a new JobService with required dependencies.
     *
     * @param jobRepository Repository for job data operations
     */
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Retrieves all jobs with pagination support.
     *
     * @param pageable Pagination and sorting parameters
     * @return A page of job postings
     */
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    /**
     * Retrieves a job by its ID.
     *
     * @param id The unique identifier of the job
     * @return The job if found, null otherwise
     */
    public Job getJobById(String id) {
        return jobRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new job posting.
     *
     * @param jobDto The data transfer object containing job information
     * @return The created job posting
     */
    public Job createJob(JobDto jobDto) {
        Job job = new Job();
        mapDtoToJob(jobDto, job);
        job.setPostedDate(LocalDateTime.now());
        job.setIsActive(true);
        return jobRepository.save(job);
    }

    /**
     * Updates an existing job posting.
     *
     * @param id The unique identifier of the job to update
     * @param jobDto The data transfer object containing updated job information
     * @return The updated job posting or null if the job is not found
     */
    public Job updateJob(String id, JobDto jobDto) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            mapDtoToJob(jobDto, job);
            job.setUpdatedAt(LocalDateTime.now());
            return jobRepository.save(job);
        }
        return null;
    }

    /**
     * Deletes a job posting.
     *
     * @param id The unique identifier of the job to delete
     * @return true if the job was successfully deleted, false otherwise
     */
    public boolean deleteJob(String id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Searches for jobs based on a keyword.
     *
     * @param keyword The search keyword to match against job title, description, or company name
     * @param pageable Pagination and sorting parameters
     * @return A page of jobs matching the search criteria
     */
    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchJobs(keyword, pageable);
    }

    /**
     * Finds jobs by category.
     *
     * @param category The job category to filter by
     * @return List of jobs in the specified category
     */
    public List<Job> getJobsByCategory(String category) {
        return jobRepository.findByCategory(category);
    }

    /**
     * Finds jobs by location.
     *
     * @param location The location to filter by
     * @return List of jobs in the specified location
     */
    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocation(location);
    }

    /**
     * Finds jobs posted by a specific recruiter.
     *
     * @param recruiterId The ID of the recruiter
     * @return List of jobs posted by the specified recruiter
     */
    public List<Job> getJobsByRecruiter(String recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    /**
     * Finds jobs by company name.
     *
     * @param companyName The company name to search for
     * @return List of jobs posted by the specified company
     */
    public List<Job> getJobsByCompany(String companyName) {
        return jobRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    /**
     * Finds jobs within a specified salary range.
     *
     * @param minSalary The minimum salary
     * @param maxSalary The maximum salary
     * @return List of jobs with salaries within the specified range
     */
    public List<Job> getJobsBySalaryRange(Double minSalary, Double maxSalary) {
        return jobRepository.findBySalaryRange(minSalary, maxSalary);
    }

    /**
     * Toggles the active status of a job posting.
     *
     * @param id The unique identifier of the job
     * @return The updated job with toggled active status or null if the job is not found
     */
    public Job toggleJobActive(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setIsActive(!job.getIsActive());
            job.setUpdatedAt(LocalDateTime.now());
            return jobRepository.save(job);
        }
        return null;
    }

    /**
     * Retrieves all jobs that are marked as active.
     *
     * @return List of active job postings
     */
    public List<Job> getActiveJobs() {
        return jobRepository.findByIsActive(true);
    }

    /**
     * Counts the number of active jobs posted by a specific recruiter.
     *
     * @param recruiterId The ID of the recruiter
     * @return The count of active jobs posted by the specified recruiter
     */
    public long countJobsByRecruiter(String recruiterId) {
        return jobRepository.countByRecruiterIdAndIsActive(recruiterId, true);
    }

    /**
     * Maps data from a JobDto to a Job entity.
     *
     * @param jobDto The source DTO containing job data
     * @param job The target Job entity to update
     */
    private void mapDtoToJob(JobDto jobDto, Job job) {
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setCompanyName(jobDto.getCompanyName());
        job.setLocation(jobDto.getLocation());
        job.setCategory(jobDto.getCategory());
        job.setEmploymentType(jobDto.getEmploymentType());
        job.setSalary(jobDto.getSalary());
        job.setRecruiterId(jobDto.getRecruiterId());
        job.setRequirements(jobDto.getRequirements());
        job.setResponsibilities(jobDto.getResponsibilities());
        job.setDeadline(jobDto.getDeadline());
    }
}