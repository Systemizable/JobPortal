package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {

    // Find candidate by user ID (one-to-one relationship)
    Optional<Candidate> findByUserId(String userId);

    // Check if candidate exists for a user
    boolean existsByUserId(String userId);

    // Find candidates by skills
    @Query("{ 'skills': { $in: ?0 } }")
    List<Candidate> findBySkills(List<String> skills);

    // Find candidates by experience level
    @Query("{ 'experienceLevel': ?0 }")
    List<Candidate> findByExperienceLevel(String experienceLevel);

    // Find candidates by location
    List<Candidate> findByLocationContainingIgnoreCase(String location);

    // Find candidates by years of experience range
    @Query("{ 'yearsOfExperience': { $gte: ?0, $lte: ?1 } }")
    List<Candidate> findByYearsOfExperienceRange(Integer minYears, Integer maxYears);

    // Find candidates by education level
    @Query("{ 'education.degree': ?0 }")
    List<Candidate> findByEducationDegree(String degree);

    // Find candidates by current job title
    @Query("{ 'experience.title': { $regex: ?0, $options: 'i' } }")
    List<Candidate> findByCurrentTitle(String title);

    // Search candidates by multiple criteria
    @Query("{ $and: [ " +
            "{ 'skills': { $in: ?0 } }, " +
            "{ 'yearsOfExperience': { $gte: ?1 } }, " +
            "{ 'location': { $regex: ?2, $options: 'i' } } " +
            "] }")
    List<Candidate> searchCandidates(List<String> skills, Integer minExperience, String location);
}