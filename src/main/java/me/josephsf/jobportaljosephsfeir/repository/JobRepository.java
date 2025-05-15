package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating Job documents in MongoDB.
 * <p>
 * This repository provides methods for CRUD operations on Job entities, as well as
 * custom queries for retrieving jobs based on various criteria such as title, category,
 * location, company, salary range, and recruiter. It leverages Spring Data MongoDB's
 * query method naming conventions and the @Query annotation for more complex queries.
 * </p>
 *
 * <p>As a Spring Data repository, it automatically provides standard operations like:</p>
 * <ul>
 *   <li>Saving and updating jobs</li>
 *   <li>Deleting jobs</li>
 *   <li>Finding jobs by ID</li>
 *   <li>Retrieving all jobs</li>
 * </ul>
 *
 * <p>The interface also defines custom queries for job searching and filtering
 * capabilities needed by the application.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see me.josephsf.jobportaljosephsfeir.model.Job
 * @see org.springframework.data.mongodb.repository.MongoRepository
 */
@Repository
public interface JobRepository extends MongoRepository<Job, String> {

    /**
     * Finds jobs with titles containing the specified keyword (case-insensitive).
     *
     * @param title the keyword to search for in job titles
     * @return a list of jobs matching the search criteria
     */
    List<Job> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds jobs in a specific category.
     *
     * @param category the job category
     * @return a list of jobs in the specified category
     */
    List<Job> findByCategory(String category);

    /**
     * Finds jobs in a specific location.
     *
     * @param location the job location
     * @return a list of jobs in the specified location
     */
    List<Job> findByLocation(String location);

    /**
     * Finds jobs from companies with names containing the specified keyword (case-insensitive).
     *
     * @param companyName the keyword to search for in company names
     * @return a list of jobs matching the search criteria
     */
    List<Job> findByCompanyNameContainingIgnoreCase(String companyName);

    /**
     * Finds jobs within a specified salary range.
     *
     * @param minSalary the minimum salary
     * @param maxSalary the maximum salary
     * @return a list of jobs with salaries within the specified range
     */
    @Query("{ 'salary' : { $gte : ?0, $lte : ?1 } }")
    List<Job> findBySalaryRange(Double minSalary, Double maxSalary);

    /**
     * Finds jobs posted by a specific recruiter.
     *
     * @param recruiterId the ID of the recruiter
     * @return a list of jobs posted by the specified recruiter
     */
    List<Job> findByRecruiterId(String recruiterId);

    /**
     * Finds jobs based on their active status.
     *
     * @param isActive true to find active jobs, false to find inactive jobs
     * @return a list of jobs with the specified active status
     */
    List<Job> findByIsActive(Boolean isActive);

    /**
     * Retrieves all jobs with pagination and sorting support.
     * Overrides the standard findAll method to explicitly support pagination.
     *
     * @param pageable pagination and sorting information
     * @return a page of jobs
     */
    Page<Job> findAll(Pageable pageable);

    /**
     * Searches for active jobs matching a keyword in title, description, or company name.
     * This is a complex query that uses MongoDB's $and and $or operators to perform
     * a search across multiple fields while filtering for active jobs only.
     *
     * @param keyword the search keyword
     * @param pageable pagination and sorting information
     * @return a page of jobs matching the search criteria
     */
    @Query("{ $and: [ " +
            "{ $or: [ " +
            "  { 'title': { $regex: ?0, $options: 'i' } }, " +
            "  { 'description': { $regex: ?0, $options: 'i' } }, " +
            "  { 'companyName': { $regex: ?0, $options: 'i' } } " +
            "  ] }, " +
            "{ 'isActive': true } " +
            "] }")
    Page<Job> searchJobs(String keyword, Pageable pageable);

    /**
     * Counts the number of active jobs posted by a specific recruiter.
     *
     * @param recruiterId the ID of the recruiter
     * @param isActive the active status to filter by
     * @return the count of jobs matching the criteria
     */
    long countByRecruiterIdAndIsActive(String recruiterId, Boolean isActive);
}