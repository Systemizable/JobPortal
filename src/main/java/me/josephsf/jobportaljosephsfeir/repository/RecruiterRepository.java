package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends MongoRepository<Recruiter, String> {

    // Find recruiter by user ID (one-to-one relationship)
    Optional<Recruiter> findByUserId(String userId);

    // Find recruiter by company name
    List<Recruiter> findByCompanyName(String companyName);

    // Find recruiters by company name containing (case-insensitive)
    List<Recruiter> findByCompanyNameContainingIgnoreCase(String companyName);

    // Check if recruiter exists for a user
    boolean existsByUserId(String userId);

    // Find recruiters by company size category
    List<Recruiter> findByCompanySize(String companySize);

    // Find verified recruiters
    List<Recruiter> findByIsVerified(Boolean isVerified);

    // Find recruiters by location/headquarters
    List<Recruiter> findByLocationContainingIgnoreCase(String location);
}