package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {

    // Find jobs by title containing keyword (case-insensitive)
    List<Job> findByTitleContainingIgnoreCase(String title);

    // Find jobs by category
    List<Job> findByCategory(String category);

    // Find jobs by location
    List<Job> findByLocation(String location);

    // Find jobs by company name
    List<Job> findByCompanyNameContainingIgnoreCase(String companyName);

    // Find jobs by salary range
    @Query("{ 'salary' : { $gte : ?0, $lte : ?1 } }")
    List<Job> findBySalaryRange(Double minSalary, Double maxSalary);

    // Find jobs by recruiter ID
    List<Job> findByRecruiterId(String recruiterId);

    // Find active/inactive jobs
    List<Job> findByIsActive(Boolean isActive);

    // Find jobs with pagination and sorting
    Page<Job> findAll(Pageable pageable);

    // Combined search - find by multiple criteria
    @Query("{ $and: [ " +
            "{ $or: [ " +
            "  { 'title': { $regex: ?0, $options: 'i' } }, " +
            "  { 'description': { $regex: ?0, $options: 'i' } }, " +
            "  { 'companyName': { $regex: ?0, $options: 'i' } } " +
            "  ] }, " +
            "{ 'isActive': true } " +
            "] }")
    Page<Job> searchJobs(String keyword, Pageable pageable);

    // Count active jobs by recruiter
    long countByRecruiterIdAndIsActive(String recruiterId, Boolean isActive);
}