package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating Candidate documents in MongoDB.
 * <p>
 * This repository provides methods for CRUD operations on Candidate entities, as well as
 * custom queries for retrieving candidates based on various criteria such as user ID,
 * skills, experience level, location, education, and work history. It leverages Spring Data
 * MongoDB's query method naming conventions and the @Query annotation for more complex queries.
 * </p>
 *
 * <p>As a Spring Data repository, it automatically provides standard operations like:</p>
 * <ul>
 *   <li>Saving and updating candidate profiles</li>
 *   <li>Deleting candidate profiles</li>
 *   <li>Finding candidate profiles by ID</li>
 *   <li>Retrieving all candidate profiles</li>
 * </ul>
 *
 * <p>The interface also defines custom queries for candidate searching and filtering,
 * which are essential for job matching and recruiter search functionalities.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see me.josephsf.jobportaljosephsfeir.model.Candidate
 * @see org.springframework.data.mongodb.repository.MongoRepository
 */
@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {

    /**
     * Finds a candidate profile by its associated user ID.
     * This method is used to retrieve the profile for a specific user account.
     *
     * @param userId the ID of the user account
     * @return an Optional containing the candidate profile if found, or empty if not
     */
    Optional<Candidate> findByUserId(String userId);

    /**
     * Checks if a candidate profile exists for a specific user.
     * This is useful during profile creation to prevent duplicates.
     *
     * @param userId the ID of the user account
     * @return true if a profile exists, false otherwise
     */
    boolean existsByUserId(String userId);

    /**
     * Finds candidates who have any of the specified skills.
     * Uses MongoDB's $in operator to match against an array of skills.
     *
     * @param skills a list of skills to search for
     * @return a list of candidates matching the skills criteria
     */
    @Query("{ 'skills': { $in: ?0 } }")
    List<Candidate> findBySkills(List<String> skills);

    /**
     * Finds candidates with a specific experience level.
     *
     * @param experienceLevel the experience level to match (e.g., ENTRY, JUNIOR, MID, SENIOR, EXECUTIVE)
     * @return a list of candidates with the specified experience level
     */
    @Query("{ 'experienceLevel': ?0 }")
    List<Candidate> findByExperienceLevel(String experienceLevel);

    /**
     * Finds candidates by partial location (case-insensitive).
     * This is useful for geographic filtering of candidates.
     *
     * @param location the partial location to search for
     * @return a list of candidates matching the location search
     */
    List<Candidate> findByLocationContainingIgnoreCase(String location);

    /**
     * Finds candidates within a specific range of years of experience.
     * Uses MongoDB's $gte (greater than or equal) and $lte (less than or equal) operators.
     *
     * @param minYears the minimum years of experience
     * @param maxYears the maximum years of experience
     * @return a list of candidates with experience within the specified range
     */
    @Query("{ 'yearsOfExperience': { $gte: ?0, $lte: ?1 } }")
    List<Candidate> findByYearsOfExperienceRange(Integer minYears, Integer maxYears);

    /**
     * Finds candidates with a specific degree in their education history.
     * This query searches within the embedded education array for matching degrees.
     *
     * @param degree the degree to search for
     * @return a list of candidates with the specified degree
     */
    @Query("{ 'education.degree': ?0 }")
    List<Candidate> findByEducationDegree(String degree);

    /**
     * Finds candidates with a specific job title in their work experience.
     * This query searches within the embedded experience array for matching titles.
     * Uses a case-insensitive regular expression match.
     *
     * @param title the job title to search for
     * @return a list of candidates with the specified title in their work history
     */
    @Query("{ 'experience.title': { $regex: ?0, $options: 'i' } }")
    List<Candidate> findByCurrentTitle(String title);

    /**
     * Searches for candidates matching multiple criteria: skills, minimum experience, and location.
     * This complex query combines multiple conditions using MongoDB's $and operator.
     *
     * @param skills a list of skills to search for
     * @param minExperience the minimum years of experience
     * @param location the location to search for
     * @return a list of candidates matching all the specified criteria
     */
    @Query("{ $and: [ " +
            "{ 'skills': { $in: ?0 } }, " +
            "{ 'yearsOfExperience': { $gte: ?1 } }, " +
            "{ 'location': { $regex: ?2, $options: 'i' } } " +
            "] }")
    List<Candidate> searchCandidates(List<String> skills, Integer minExperience, String location);
}