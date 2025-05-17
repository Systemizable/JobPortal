package me.josephsf.jobportaljosephsfeirtest.repository;

import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for JobRepository.
 * These tests validate that the repository methods correctly interact with MongoDB.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.mongodb.embedded.version=4.0.21",
        "de.flapdoodle.mongodb.embedded.version=4.0.21"
})
public class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    private Job testJob;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        jobRepository.deleteAll();

        // Create a test job
        testJob = new Job();
        testJob.setTitle("Software Engineer");
        testJob.setDescription("Java developer position");
        testJob.setCompanyName("Tech Company");
        testJob.setLocation("New York");
        testJob.setCategory("IT");
        testJob.setEmploymentType("FULL_TIME");
        testJob.setSalary(100000.0);
        testJob.setRecruiterId("recruiterId123");
        testJob.setPostedDate(LocalDateTime.now());
        testJob.setIsActive(true);
        testJob.setRequirements(List.of("Java", "Spring Boot", "MongoDB"));
        testJob.setResponsibilities(List.of("Develop backend services", "Write unit tests"));

        // Save the test job
        jobRepository.save(testJob);
    }

    /**
     * Clean up after each test.
     */
    @AfterEach
    void tearDown() {
        jobRepository.deleteAll();
    }

    /**
     * Test finding a job by ID.
     */
    @Test
    void testFindById() {
        // Get the ID of the saved job
        String jobId = jobRepository.findAll().get(0).getId();

        // Find by ID
        Optional<Job> foundJob = jobRepository.findById(jobId);

        // Assert
        assertTrue(foundJob.isPresent(), "Job should be found by its ID");
        assertEquals("Software Engineer", foundJob.get().getTitle(), "Job title should match");
        assertEquals("Tech Company", foundJob.get().getCompanyName(), "Company name should match");
    }

    /**
     * Test finding jobs by title (case insensitive).
     */
    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Find by partial title (case insensitive)
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCase("software");

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs with 'software' in title");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals("Software Engineer", jobs.get(0).getTitle(), "Job title should match");

        // Test with different case
        jobs = jobRepository.findByTitleContainingIgnoreCase("SOFTWARE");
        assertFalse(jobs.isEmpty(), "Should find jobs with 'SOFTWARE' in title (case insensitive)");
        assertEquals(1, jobs.size(), "Should find exactly one job");
    }

    /**
     * Test finding jobs by category.
     */
    @Test
    void testFindByCategory() {
        // Find by category
        List<Job> jobs = jobRepository.findByCategory("IT");

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs in IT category");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals("IT", jobs.get(0).getCategory(), "Job category should match");

        // Test with non-existent category
        jobs = jobRepository.findByCategory("Finance");
        assertTrue(jobs.isEmpty(), "Should not find jobs in Finance category");
    }

    /**
     * Test finding jobs by location.
     */
    @Test
    void testFindByLocation() {
        // Find by location
        List<Job> jobs = jobRepository.findByLocation("New York");

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs in New York");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals("New York", jobs.get(0).getLocation(), "Job location should match");
    }

    /**
     * Test finding jobs by company name.
     */
    @Test
    void testFindByCompanyNameContainingIgnoreCase() {
        // Find by company name containing
        List<Job> jobs = jobRepository.findByCompanyNameContainingIgnoreCase("Tech");

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs at companies with 'Tech' in name");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals("Tech Company", jobs.get(0).getCompanyName(), "Company name should match");
    }

    /**
     * Test finding jobs by salary range.
     */
    @Test
    void testFindBySalaryRange() {
        // Find by salary range
        List<Job> jobs = jobRepository.findBySalaryRange(90000.0, 110000.0);

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs in salary range");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals(100000.0, jobs.get(0).getSalary(), "Job salary should match");

        // Test outside range
        jobs = jobRepository.findBySalaryRange(110000.0, 120000.0);
        assertTrue(jobs.isEmpty(), "Should not find jobs outside salary range");
    }

    /**
     * Test finding jobs by recruiter ID.
     */
    @Test
    void testFindByRecruiterId() {
        // Find by recruiter ID
        List<Job> jobs = jobRepository.findByRecruiterId("recruiterId123");

        // Assert
        assertFalse(jobs.isEmpty(), "Should find jobs posted by recruiter");
        assertEquals(1, jobs.size(), "Should find exactly one job");
        assertEquals("recruiterId123", jobs.get(0).getRecruiterId(), "Recruiter ID should match");
    }

    /**
     * Test finding jobs by active status.
     */
    @Test
    void testFindByIsActive() {
        // Find active jobs
        List<Job> activeJobs = jobRepository.findByIsActive(true);

        // Assert
        assertFalse(activeJobs.isEmpty(), "Should find active jobs");
        assertEquals(1, activeJobs.size(), "Should find exactly one job");
        assertTrue(activeJobs.get(0).getIsActive(), "Job should be active");

        // Update job to inactive
        Job job = activeJobs.get(0);
        job.setIsActive(false);
        jobRepository.save(job);

        // Find inactive jobs
        List<Job> inactiveJobs = jobRepository.findByIsActive(false);
        assertFalse(inactiveJobs.isEmpty(), "Should find inactive jobs");
        assertEquals(1, inactiveJobs.size(), "Should find exactly one job");
        assertFalse(inactiveJobs.get(0).getIsActive(), "Job should be inactive");
    }

    /**
     * Test searching jobs by keyword (in title, description, or company name).
     */
    @Test
    void testSearchJobs() {
        // Test search in title
        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> jobsPage = jobRepository.searchJobs("Software", pageable);

        // Assert
        assertFalse(jobsPage.isEmpty(), "Should find jobs with 'Software' in title");
        assertEquals(1, jobsPage.getTotalElements(), "Should find exactly one job");

        // Test search in description
        jobsPage = jobRepository.searchJobs("Java", pageable);
        assertFalse(jobsPage.isEmpty(), "Should find jobs with 'Java' in description");
        assertEquals(1, jobsPage.getTotalElements(), "Should find exactly one job");

        // Test search in company name
        jobsPage = jobRepository.searchJobs("Tech", pageable);
        assertFalse(jobsPage.isEmpty(), "Should find jobs with 'Tech' in company name");
        assertEquals(1, jobsPage.getTotalElements(), "Should find exactly one job");

        // Test search with no results
        jobsPage = jobRepository.searchJobs("Healthcare", pageable);
        assertTrue(jobsPage.isEmpty(), "Should not find jobs with 'Healthcare'");
    }

    /**
     * Test counting jobs by recruiter ID and active status.
     */
    @Test
    void testCountByRecruiterIdAndIsActive() {
        // Count active jobs by recruiter
        long count = jobRepository.countByRecruiterIdAndIsActive("recruiterId123", true);

        // Assert
        assertEquals(1, count, "Should count exactly one active job");

        // Update job to inactive
        Job job = jobRepository.findAll().get(0);
        job.setIsActive(false);
        jobRepository.save(job);

        // Count inactive jobs by recruiter
        count = jobRepository.countByRecruiterIdAndIsActive("recruiterId123", false);
        assertEquals(1, count, "Should count exactly one inactive job");

        // Count active jobs by recruiter (should be 0 now)
        count = jobRepository.countByRecruiterIdAndIsActive("recruiterId123", true);
        assertEquals(0, count, "Should count zero active jobs");
    }
}