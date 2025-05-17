package me.josephsf.jobportaljosephsfeirtest.service;

import me.josephsf.jobportaljosephsfeir.dto.JobDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import me.josephsf.jobportaljosephsfeir.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JobService.
 * These tests validate the service layer logic for job management.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job testJob;
    private JobDto testJobDto;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test data
        testJob = new Job();
        testJob.setId("testJobId");
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
        testJob.setCreatedAt(LocalDateTime.now());
        testJob.setUpdatedAt(LocalDateTime.now());
        testJob.setRequirements(Arrays.asList("Java", "Spring Boot", "MongoDB"));
        testJob.setResponsibilities(Arrays.asList("Develop backend services", "Write unit tests"));

        testJobDto = new JobDto();
        testJobDto.setTitle("Software Engineer");
        testJobDto.setDescription("Java developer position");
        testJobDto.setCompanyName("Tech Company");
        testJobDto.setLocation("New York");
        testJobDto.setCategory("IT");
        testJobDto.setEmploymentType("FULL_TIME");
        testJobDto.setSalary(100000.0);
        testJobDto.setRecruiterId("recruiterId123");
        testJobDto.setRequirements(Arrays.asList("Java", "Spring Boot", "MongoDB"));
        testJobDto.setResponsibilities(Arrays.asList("Develop backend services", "Write unit tests"));
    }

    /**
     * Test retrieving all jobs with pagination.
     */
    @Test
    void testGetAllJobs() {
        // Setup
        Job job2 = new Job();
        job2.setId("jobId2");
        job2.setTitle("Software Developer");

        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob, job2));
        when(jobRepository.findAll(any(Pageable.class))).thenReturn(jobPage);

        // Execute
        Page<Job> result = jobService.getAllJobs(Pageable.unpaged());

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.getContent().size(), "Should find two jobs");
        verify(jobRepository, times(1)).findAll(any(Pageable.class));
    }

    /**
     * Test retrieving a job by ID.
     */
    @Test
    void testGetJobById() {
        // Setup
        when(jobRepository.findById("testJobId")).thenReturn(Optional.of(testJob));

        // Execute
        Job result = jobService.getJobById("testJobId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testJobId", result.getId(), "Job ID should match");
        assertEquals("Software Engineer", result.getTitle(), "Job title should match");
        verify(jobRepository, times(1)).findById("testJobId");
    }

    /**
     * Test that retrieving a non-existent job returns null.
     */
    @Test
    void testGetJobByIdNotFound() {
        // Setup
        when(jobRepository.findById("nonExistentJobId")).thenReturn(Optional.empty());

        // Execute
        Job result = jobService.getJobById("nonExistentJobId");

        // Verify
        assertNull(result, "Result should be null for non-existent job");
        verify(jobRepository, times(1)).findById("nonExistentJobId");
    }

    /**
     * Test creating a new job posting.
     */
    @Test
    void testCreateJob() {
        // Setup
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> {
            Job savedJob = invocation.getArgument(0);
            savedJob.setId("newJobId");
            return savedJob;
        });

        // Execute
        Job result = jobService.createJob(testJobDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("newJobId", result.getId(), "Job ID should be set");
        assertEquals("Software Engineer", result.getTitle(), "Job title should match");
        assertEquals("Tech Company", result.getCompanyName(), "Company name should match");
        assertTrue(result.getIsActive(), "Job should be active");
        assertNotNull(result.getPostedDate(), "Posted date should be set");
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    /**
     * Test updating an existing job posting.
     */
    @Test
    void testUpdateJob() {
        // Setup
        testJobDto.setTitle("Updated Title");
        testJobDto.setDescription("Updated description");

        when(jobRepository.findById("testJobId")).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Job result = jobService.updateJob("testJobId", testJobDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testJobId", result.getId(), "Job ID should match");
        assertEquals("Updated Title", result.getTitle(), "Job title should be updated");
        assertEquals("Updated description", result.getDescription(), "Job description should be updated");
        assertNotNull(result.getUpdatedAt(), "Update timestamp should be set");
        verify(jobRepository, times(1)).findById("testJobId");
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    /**
     * Test that updating a non-existent job returns null.
     */
    @Test
    void testUpdateJobNotFound() {
        // Setup
        when(jobRepository.findById("nonExistentJobId")).thenReturn(Optional.empty());

        // Execute
        Job result = jobService.updateJob("nonExistentJobId", testJobDto);

        // Verify
        assertNull(result, "Result should be null for non-existent job");
        verify(jobRepository, times(1)).findById("nonExistentJobId");
        verify(jobRepository, never()).save(any(Job.class));
    }

    /**
     * Test deleting a job posting.
     */
    @Test
    void testDeleteJob() {
        // Setup
        when(jobRepository.existsById("testJobId")).thenReturn(true);
        doNothing().when(jobRepository).deleteById("testJobId");

        // Execute
        boolean result = jobService.deleteJob("testJobId");

        // Verify
        assertTrue(result, "Should return true when job is found and deleted");
        verify(jobRepository, times(1)).existsById("testJobId");
        verify(jobRepository, times(1)).deleteById("testJobId");
    }

    /**
     * Test that deleting a non-existent job returns false.
     */
    @Test
    void testDeleteJobNotFound() {
        // Setup
        when(jobRepository.existsById("nonExistentJobId")).thenReturn(false);

        // Execute
        boolean result = jobService.deleteJob("nonExistentJobId");

        // Verify
        assertFalse(result, "Should return false when job is not found");
        verify(jobRepository, times(1)).existsById("nonExistentJobId");
        verify(jobRepository, never()).deleteById("nonExistentJobId");
    }

    /**
     * Test searching for jobs by keyword.
     */
    @Test
    void testSearchJobs() {
        // Setup
        Page<Job> jobPage = new PageImpl<>(List.of(testJob));
        when(jobRepository.searchJobs(eq("Java"), any(Pageable.class))).thenReturn(jobPage);

        // Execute
        Page<Job> result = jobService.searchJobs("Java", Pageable.unpaged());

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getContent().size(), "Should find one job");
        assertEquals("Software Engineer", result.getContent().get(0).getTitle(), "Job title should match");
        verify(jobRepository, times(1)).searchJobs(eq("Java"), any(Pageable.class));
    }

    /**
     * Test filtering jobs by category.
     */
    @Test
    void testGetJobsByCategory() {
        // Setup
        when(jobRepository.findByCategory("IT")).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getJobsByCategory("IT");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one job");
        assertEquals("IT", result.get(0).getCategory(), "Job category should match");
        verify(jobRepository, times(1)).findByCategory("IT");
    }

    /**
     * Test filtering jobs by location.
     */
    @Test
    void testGetJobsByLocation() {
        // Setup
        when(jobRepository.findByLocation("New York")).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getJobsByLocation("New York");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one job");
        assertEquals("New York", result.get(0).getLocation(), "Job location should match");
        verify(jobRepository, times(1)).findByLocation("New York");
    }

    /**
     * Test retrieving jobs posted by a specific recruiter.
     */
    @Test
    void testGetJobsByRecruiter() {
        // Setup
        when(jobRepository.findByRecruiterId("recruiterId123")).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getJobsByRecruiter("recruiterId123");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one job");
        assertEquals("recruiterId123", result.get(0).getRecruiterId(), "Recruiter ID should match");
        verify(jobRepository, times(1)).findByRecruiterId("recruiterId123");
    }

    /**
     * Test filtering jobs by company name.
     */
    @Test
    void testGetJobsByCompany() {
        // Setup
        when(jobRepository.findByCompanyNameContainingIgnoreCase("Tech")).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getJobsByCompany("Tech");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one job");
        assertEquals("Tech Company", result.get(0).getCompanyName(), "Company name should match");
        verify(jobRepository, times(1)).findByCompanyNameContainingIgnoreCase("Tech");
    }

    /**
     * Test filtering jobs by salary range.
     */
    @Test
    void testGetJobsBySalaryRange() {
        // Setup
        when(jobRepository.findBySalaryRange(90000.0, 110000.0)).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getJobsBySalaryRange(90000.0, 110000.0);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one job");
        assertEquals(100000.0, result.get(0).getSalary(), "Job salary should match");
        verify(jobRepository, times(1)).findBySalaryRange(90000.0, 110000.0);
    }

    /**
     * Test toggling a job's active status.
     */
    @Test
    void testToggleJobActive() {
        // Setup
        Job testJobCopy = new Job();
        testJobCopy.setId(testJob.getId());
        testJobCopy.setTitle(testJob.getTitle());
        testJobCopy.setIsActive(true);  // Start with active = true

        when(jobRepository.findById("testJobId")).thenReturn(Optional.of(testJobCopy));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Job result = jobService.toggleJobActive("testJobId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testJobId", result.getId(), "Job ID should match");
        assertFalse(result.getIsActive(), "Job active status should be toggled to false");
        verify(jobRepository, times(1)).findById("testJobId");
        verify(jobRepository, times(1)).save(any(Job.class));

        // Toggle back to active
        when(jobRepository.findById("testJobId")).thenReturn(Optional.of(result));

        Job secondResult = jobService.toggleJobActive("testJobId");

        assertTrue(secondResult.getIsActive(), "Job active status should be toggled back to true");
    }

    /**
     * Test retrieving active jobs.
     */
    @Test
    void testGetActiveJobs() {
        // Setup
        when(jobRepository.findByIsActive(true)).thenReturn(List.of(testJob));

        // Execute
        List<Job> result = jobService.getActiveJobs();

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one active job");
        assertTrue(result.get(0).getIsActive(), "Job should be active");
        verify(jobRepository, times(1)).findByIsActive(true);
    }

    /**
     * Test counting jobs posted by a specific recruiter.
     */
    @Test
    void testCountJobsByRecruiter() {
        // Setup
        when(jobRepository.countByRecruiterIdAndIsActive("recruiterId123", true)).thenReturn(5L);

        // Execute
        long result = jobService.countJobsByRecruiter("recruiterId123");

        // Verify
        assertEquals(5L, result, "Should count 5 active jobs for the recruiter");
        verify(jobRepository, times(1)).countByRecruiterIdAndIsActive("recruiterId123", true);
    }
}