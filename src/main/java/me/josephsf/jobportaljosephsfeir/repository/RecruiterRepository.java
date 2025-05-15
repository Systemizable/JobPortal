package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating Recruiter documents in MongoDB.
 * <p>
 * This repository provides methods for CRUD operations on Recruiter entities, as well as
 * custom queries for retrieving recruiters based on various criteria such as user ID,
 * company name, company size, verification status, and location. It leverages Spring Data
 * MongoDB's query method naming conventions for defining custom finder methods.
 * </p>
 *
 * <p>As a Spring Data repository, it automatically provides standard operations like:</p>
 * <ul>
 *   <li>Saving and updating recruiter profiles</li>
 *   <li>Deleting recruiter profiles</li>
 *   <li>Finding recruiter profiles by ID</li>
 *   <li>Retrieving all recruiter profiles</li>
 * </ul>
 *
 * <p>The interface also defines custom queries needed for specific business operations
 * in the application, particularly for linking User accounts to Recruiter profiles
 * and for company-specific queries.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see me.josephsf.jobportaljosephsfeir.model.Recruiter
 * @see org.springframework.data.mongodb.repository.MongoRepository
 */
@Repository
public interface RecruiterRepository extends MongoRepository<Recruiter, String> {

    /**
     * Finds a recruiter profile by its associated user ID.
     * This method is used to retrieve the profile for a specific user account.
     *
     * @param userId the ID of the user account
     * @return an Optional containing the recruiter profile if found, or empty if not
     */
    Optional<Recruiter> findByUserId(String userId);

    /**
     * Finds recruiter profiles by exact company name.
     *
     * @param companyName the exact company name to match
     * @return a list of recruiter profiles from the specified company
     */
    List<Recruiter> findByCompanyName(String companyName);

    /**
     * Finds recruiter profiles by partial company name (case-insensitive).
     * This is useful for searching and filtering recruiters by company.
     *
     * @param companyName the partial company name to search for
     * @return a list of recruiter profiles matching the search criteria
     */
    List<Recruiter> findByCompanyNameContainingIgnoreCase(String companyName);

    /**
     * Checks if a recruiter profile exists for a specific user.
     * This is useful during profile creation to prevent duplicates.
     *
     * @param userId the ID of the user account
     * @return true if a profile exists, false otherwise
     */
    boolean existsByUserId(String userId);

    /**
     * Finds recruiter profiles by company size category.
     *
     * @param companySize the company size category (e.g., STARTUP, SMALL, MEDIUM, LARGE, ENTERPRISE)
     * @return a list of recruiter profiles matching the company size
     */
    List<Recruiter> findByCompanySize(String companySize);

    /**
     * Finds recruiter profiles by verification status.
     * This is used to filter verified vs. unverified recruiters.
     *
     * @param isVerified true to find verified recruiters, false to find unverified recruiters
     * @return a list of recruiter profiles matching the verification status
     */
    List<Recruiter> findByIsVerified(Boolean isVerified);

    /**
     * Finds recruiter profiles by partial location (case-insensitive).
     * This is useful for geographic filtering of recruiters.
     *
     * @param location the partial location to search for
     * @return a list of recruiter profiles matching the location search
     */
    List<Recruiter> findByLocationContainingIgnoreCase(String location);
}