package me.josephsf.jobportaljosephsfeirtest.service;

import me.josephsf.jobportaljosephsfeir.dto.RecruiterDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import me.josephsf.jobportaljosephsfeir.repository.RecruiterRepository;
import me.josephsf.jobportaljosephsfeir.service.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecruiterService.
 * These tests validate the service layer logic for recruiter management.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@ExtendWith(MockitoExtension.class)
public class RecruiterServiceTest {

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private RecruiterService recruiterService;

    private Recruiter testRecruiter;
    private RecruiterDto testRecruiterDto;
    private Job testJob;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test recruiter data
        testRecruiter = new Recruiter();
        testRecruiter.setId("testRecruiterId");
        testRecruiter.setUserId("testUserId");
        testRecruiter.setCompanyName("Tech Corp");
        testRecruiter.setCompanySize("MEDIUM");
        testRecruiter.setLocation("San Francisco");
        testRecruiter.setIndustry("Technology");
        testRecruiter.setDepartment("HR");
        testRecruiter.setPosition("Recruiter");
        testRecruiter.setPhoneNumber("1234567890");
        testRecruiter.setIsVerified(true);
        testRecruiter.setCreatedAt(LocalDateTime.now());
        testRecruiter.setUpdatedAt(LocalDateTime.now());

        testRecruiterDto = new RecruiterDto();
        testRecruiterDto.setUserId("testUserId");
        testRecruiterDto.setCompanyName("Tech Corp");
        testRecruiterDto.setCompanySize("MEDIUM");
        testRecruiterDto.setLocation("San Francisco");
        testRecruiterDto.setIndustry("Technology");
        testRecruiterDto.setDepartment("HR");
        testRecruiterDto.setPosition("Recruiter");
        testRecruiterDto.setPhoneNumber("1234567890");

        // Set up test job data
        testJob = new Job();
        testJob.setId("testJobId");
        testJob.setTitle("Software Engineer");
        testJob.setCompanyName("Tech Corp");
        testJob.setRecruiterId("testRecruiterId");
        testJob.setIsActive(true);
    }

    /**
     * Test retrieving a recruiter by ID.
     */
    @Test
    void testGetRecruiterById() {
        // Setup
        when(recruiterRepository.findById("testRecruiterId")).thenReturn(Optional.of(testRecruiter));

        // Execute
        Recruiter result = recruiterService.getRecruiterById("testRecruiterId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testRecruiterId", result.getId(), "Recruiter ID should match");
        assertEquals("Tech Corp", result.getCompanyName(), "Company name should match");
        verify(recruiterRepository, times(1)).findById("testRecruiterId");
    }

    /**
     * Test retrieving a recruiter by user ID.
     */
    @Test
    void testGetRecruiterByUserId() {
        // Setup
        when(recruiterRepository.findByUserId("testUserId")).thenReturn(Optional.of(testRecruiter));

        // Execute
        Recruiter result = recruiterService.getRecruiterByUserId("testUserId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testRecruiterId", result.getId(), "Recruiter ID should match");
        assertEquals("testUserId", result.getUserId(), "User ID should match");
        verify(recruiterRepository, times(1)).findByUserId("testUserId");
    }

    /**
     * Test creating a new recruiter profile.
     */
    @Test
    void testCreateRecruiterProfile() {
        // Setup
        when(recruiterRepository.existsByUserId("testUserId")).thenReturn(false);
        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(testRecruiter);

        // Execute
        Recruiter result = recruiterService.createRecruiterProfile(testRecruiterDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testRecruiterId", result.getId(), "Recruiter ID should match");
        assertEquals("Tech Corp", result.getCompanyName(), "Company name should match");
        verify(recruiterRepository, times(1)).existsByUserId("testUserId");
        verify(recruiterRepository, times(1)).save(any(Recruiter.class));
    }

    /**
     * Test that creating a profile fails if one already exists.
     */
    @Test
    void testCreateRecruiterProfileAlreadyExists() {
        // Setup
        when(recruiterRepository.existsByUserId("testUserId")).thenReturn(true);

        // Execute
        Recruiter result = recruiterService.createRecruiterProfile(testRecruiterDto);

        // Verify
        assertNull(result, "Result should be null for duplicate profile");
        verify(recruiterRepository, times(1)).existsByUserId("testUserId");
        verify(recruiterRepository, never()).save(any(Recruiter.class));
    }

    /**
     * Test updating an existing recruiter profile.
     */
    @Test
    void testUpdateRecruiterProfile() {
        // Setup
        testRecruiterDto.setCompanyName("New Tech Corp");

        when(recruiterRepository.findById("testRecruiterId")).thenReturn(Optional.of(testRecruiter));
        when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Recruiter result = recruiterService.updateRecruiterProfile("testRecruiterId", testRecruiterDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testRecruiterId", result.getId(), "Recruiter ID should match");
        assertEquals("New Tech Corp", result.getCompanyName(), "Company name should be updated");
        verify(recruiterRepository, times(1)).findById("testRecruiterId");
        verify(recruiterRepository, times(1)).save(any(Recruiter.class));
    }

    /**
     * Test that updating a non-existent profile returns null.
     */
    @Test
    void testUpdateRecruiterProfileNotFound() {
        // Setup
        when(recruiterRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        // Execute
        Recruiter result = recruiterService.updateRecruiterProfile("nonExistentId", testRecruiterDto);

        // Verify
        assertNull(result, "Result should be null for non-existent profile");
        verify(recruiterRepository, times(1)).findById("nonExistentId");
        verify(recruiterRepository, never()).save(any(Recruiter.class));
    }

    /**
     * Test deleting a recruiter profile.
     */
    @Test
    void testDeleteRecruiterProfile() {
        // Setup
        when(recruiterRepository.existsById("testRecruiterId")).thenReturn(true);
        doNothing().when(recruiterRepository).deleteById("testRecruiterId");

        // Execute
        boolean result = recruiterService.deleteRecruiterProfile("testRecruiterId");

        // Verify
        assertTrue(result, "Should return true when profile is found and deleted");
        verify(recruiterRepository, times(1)).existsById("testRecruiterId");
        verify(recruiterRepository, times(1)).deleteById("testRecruiterId");
    }

    /**
     * Test that deleting a non-existent profile returns false.
     */
    @Test
    void testDeleteRecruiterProfileNotFound() {
        // Setup
        when(recruiterRepository.existsById("nonExistentId")).thenReturn(false);

        // Execute
        boolean result = recruiterService.deleteRecruiterProfile("nonExistentId");

        // Verify
        assertFalse(result, "Should return false when profile is not found");
        verify(recruiterRepository, times(1)).existsById("nonExistentId");
        verify(recruiterRepository, never()).deleteById("nonExistentId");
    }

    /**
     * Test finding recruiters by company name.
     */
    @Test
    void testGetRecruitersByCompany() {
        // Setup
        when(recruiterRepository.findByCompanyName("Tech Corp"))
                .thenReturn(Arrays.asList(testRecruiter));

        // Execute
        List<Recruiter> result = recruiterService.getRecruitersByCompany("Tech Corp");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one recruiter");
        assertEquals("testRecruiterId", result.get(0).getId(), "Recruiter ID should match");
        assertEquals("Tech Corp", result.get(0).getCompanyName(), "Company name should match");
        verify(recruiterRepository, times(1)).findByCompanyName("Tech Corp");
    }

    /**
     * Test searching for recruiters by company name.
     */
    @Test
    void testSearchRecruiters_ByCompanyName() {
        // Setup
        when(recruiterRepository.findByCompanyNameContainingIgnoreCase("Tech"))
                .thenReturn(Arrays.asList(testRecruiter));

        // Execute
        List<Recruiter> result = recruiterService.searchRecruiters("Tech", null, null);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one recruiter");
        assertEquals("testRecruiterId", result.get(0).getId(), "Recruiter ID should match");
        verify(recruiterRepository, times(1)).findByCompanyNameContainingIgnoreCase("Tech");
    }

    /**
     * Test searching for recruiters by location.
     */
    @Test
    void testSearchRecruiters_ByLocation() {
        // Setup
        when(recruiterRepository.findByLocationContainingIgnoreCase("San Francisco"))
                .thenReturn(Arrays.asList(testRecruiter));

        // Execute
        List<Recruiter> result = recruiterService.searchRecruiters(null, "San Francisco", null);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one recruiter");
        assertEquals("testRecruiterId", result.get(0).getId(), "Recruiter ID should match");
        verify(recruiterRepository, times(1)).findByLocationContainingIgnoreCase("San Francisco");
    }

    /**
     * Test searching for recruiters with no criteria.
     */
    @Test
    void testSearchRecruiters_NoCriteria() {
        // Setup
        when(recruiterRepository.findAll())
                .thenReturn(Arrays.asList(testRecruiter));

        // Execute
        List<Recruiter> result = recruiterService.searchRecruiters(null, null, null);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one recruiter");
        assertEquals("testRecruiterId", result.get(0).getId(), "Recruiter ID should match");
        verify(recruiterRepository, times(1)).findAll();
    }

    /**
     * Test verifying a recruiter.
     */
    @Test
    void testVerifyRecruiter() {
        // Setup
        Recruiter unverifiedRecruiter = new Recruiter();
        unverifiedRecruiter.setId("testRecruiterId");
        unverifiedRecruiter.setCompanyName("Tech Corp");
        unverifiedRecruiter.setIsVerified(false);

        when(recruiterRepository.findById("testRecruiterId")).thenReturn(Optional.of(unverifiedRecruiter));
        when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Recruiter result = recruiterService.verifyRecruiter("testRecruiterId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getIsVerified(), "Recruiter should be verified");
        verify(recruiterRepository, times(1)).findById("testRecruiterId");
        verify(recruiterRepository, times(1)).save(any(Recruiter.class));
    }

    /**
     * Test retrieving verified recruiters.
     */
    @Test
    void testGetVerifiedRecruiters() {
        // Setup
        when(recruiterRepository.findByIsVerified(true))
                .thenReturn(Arrays.asList(testRecruiter));

        // Execute
        List<Recruiter> result = recruiterService.getVerifiedRecruiters();

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one verified recruiter");
        assertEquals("testRecruiterId", result.get(0).getId(), "Recruiter ID should match");
        assertTrue(result.get(0).getIsVerified(), "Recruiter should be verified");
        verify(recruiterRepository, times(1)).findByIsVerified(true);
    }

    /**
     * Test retrieving recruiter statistics.
     */
    @Test
    void testGetRecruiterStats() {
        // Setup
        when(jobRepository.countByRecruiterIdAndIsActive("testRecruiterId", true)).thenReturn(5L);
        when(jobRepository.findByRecruiterId("testRecruiterId"))
                .thenReturn(Arrays.asList(testJob));

        // Execute
        Map<String, Object> result = recruiterService.getRecruiterStats("testRecruiterId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(5L, result.get("totalJobs"), "Total jobs should match");
        assertEquals(5L, result.get("activeJobs"), "Active jobs should match");
        assertNotNull(result.get("recentJobs"), "Recent jobs should not be null");
        assertEquals(1, ((List<?>) result.get("recentJobs")).size(), "Should have one recent job");
        verify(jobRepository, times(2)).countByRecruiterIdAndIsActive("testRecruiterId", true);
        verify(jobRepository, times(1)).findByRecruiterId("testRecruiterId");
    }

    /**
     * Test checking if a recruiter is verified.
     */
    @Test
    void testIsVerified() {
        // Setup
        when(recruiterRepository.findById("testRecruiterId")).thenReturn(Optional.of(testRecruiter));

        // Execute
        boolean result = recruiterService.isVerified("testRecruiterId");

        // Verify
        assertTrue(result, "Recruiter should be verified");
        verify(recruiterRepository, times(1)).findById("testRecruiterId");
    }

    /**
     * Test checking if a non-existent recruiter is verified.
     */
    @Test
    void testIsVerified_NonExistent() {
        // Setup
        when(recruiterRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        // Execute
        boolean result = recruiterService.isVerified("nonExistentId");

        // Verify
        assertFalse(result, "Non-existent recruiter should not be verified");
        verify(recruiterRepository, times(1)).findById("nonExistentId");
    }

    /**
     * Test checking if an unverified recruiter is verified.
     */
    @Test
    void testIsVerified_Unverified() {
        // Setup
        Recruiter unverifiedRecruiter = new Recruiter();
        unverifiedRecruiter.setId("testRecruiterId");
        unverifiedRecruiter.setCompanyName("Tech Corp");
        unverifiedRecruiter.setIsVerified(false);

        when(recruiterRepository.findById("testRecruiterId")).thenReturn(Optional.of(unverifiedRecruiter));

        // Execute
        boolean result = recruiterService.isVerified("testRecruiterId");

        // Verify
        assertFalse(result, "Unverified recruiter should return false");
        verify(recruiterRepository, times(1)).findById("testRecruiterId");
    }
}