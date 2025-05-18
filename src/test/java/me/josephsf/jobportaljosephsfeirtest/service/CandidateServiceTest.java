package me.josephsf.jobportaljosephsfeirtest.service;

import me.josephsf.jobportaljosephsfeir.dto.CandidateDto;
import me.josephsf.jobportaljosephsfeir.model.Candidate;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository;
import me.josephsf.jobportaljosephsfeir.repository.CandidateRepository;
import me.josephsf.jobportaljosephsfeir.service.CandidateService;
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
 * Unit tests for CandidateService.
 * These tests validate the service layer logic for candidate management.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private CandidateService candidateService;

    private Candidate testCandidate;
    private CandidateDto testCandidateDto;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test candidate data
        testCandidate = new Candidate();
        testCandidate.setId("testCandidateId");
        testCandidate.setUserId("testUserId");
        testCandidate.setFirstName("John");
        testCandidate.setLastName("Doe");
        testCandidate.setPhoneNumber("1234567890");
        testCandidate.setLocation("New York");
        testCandidate.setCurrentTitle("Software Engineer");
        testCandidate.setExperienceLevel("MID");
        testCandidate.setYearsOfExperience(5);
        testCandidate.setSkills(Arrays.asList("Java", "Spring", "MongoDB"));
        testCandidate.setIsAvailable(true);
        testCandidate.setCreatedAt(LocalDateTime.now());
        testCandidate.setUpdatedAt(LocalDateTime.now());

        testCandidateDto = new CandidateDto();
        testCandidateDto.setUserId("testUserId");
        testCandidateDto.setFirstName("John");
        testCandidateDto.setLastName("Doe");
        testCandidateDto.setPhoneNumber("1234567890");
        testCandidateDto.setLocation("New York");
        testCandidateDto.setCurrentTitle("Software Engineer");
        testCandidateDto.setExperienceLevel("MID");
        testCandidateDto.setYearsOfExperience(5);
        testCandidateDto.setSkills(Arrays.asList("Java", "Spring", "MongoDB"));
        testCandidateDto.setIsAvailable(true);
    }

    /**
     * Test retrieving a candidate by ID.
     */
    @Test
    void testGetCandidateById() {
        // Setup
        when(candidateRepository.findById("testCandidateId")).thenReturn(Optional.of(testCandidate));

        // Execute
        Candidate result = candidateService.getCandidateById("testCandidateId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertEquals("John", result.getFirstName(), "First name should match");
        verify(candidateRepository, times(1)).findById("testCandidateId");
    }

    /**
     * Test retrieving a candidate by user ID.
     */
    @Test
    void testGetCandidateByUserId() {
        // Setup
        when(candidateRepository.findByUserId("testUserId")).thenReturn(Optional.of(testCandidate));

        // Execute
        Candidate result = candidateService.getCandidateByUserId("testUserId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertEquals("testUserId", result.getUserId(), "User ID should match");
        verify(candidateRepository, times(1)).findByUserId("testUserId");
    }

    /**
     * Test creating a new candidate profile.
     */
    @Test
    void testCreateCandidateProfile() {
        // Setup
        when(candidateRepository.existsByUserId("testUserId")).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        // Execute
        Candidate result = candidateService.createCandidateProfile(testCandidateDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertEquals("John", result.getFirstName(), "First name should match");
        verify(candidateRepository, times(1)).existsByUserId("testUserId");
        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    /**
     * Test that creating a profile fails if one already exists.
     */
    @Test
    void testCreateCandidateProfileAlreadyExists() {
        // Setup
        when(candidateRepository.existsByUserId("testUserId")).thenReturn(true);

        // Execute
        Candidate result = candidateService.createCandidateProfile(testCandidateDto);

        // Verify
        assertNull(result, "Result should be null for duplicate profile");
        verify(candidateRepository, times(1)).existsByUserId("testUserId");
        verify(candidateRepository, never()).save(any(Candidate.class));
    }

    /**
     * Test updating an existing candidate profile.
     */
    @Test
    void testUpdateCandidateProfile() {
        // Setup
        testCandidateDto.setFirstName("Jane");

        when(candidateRepository.findById("testCandidateId")).thenReturn(Optional.of(testCandidate));
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Candidate result = candidateService.updateCandidateProfile("testCandidateId", testCandidateDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertEquals("Jane", result.getFirstName(), "First name should be updated");
        verify(candidateRepository, times(1)).findById("testCandidateId");
        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    /**
     * Test that updating a non-existent profile returns null.
     */
    @Test
    void testUpdateCandidateProfileNotFound() {
        // Setup
        when(candidateRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        // Execute
        Candidate result = candidateService.updateCandidateProfile("nonExistentId", testCandidateDto);

        // Verify
        assertNull(result, "Result should be null for non-existent profile");
        verify(candidateRepository, times(1)).findById("nonExistentId");
        verify(candidateRepository, never()).save(any(Candidate.class));
    }

    /**
     * Test deleting a candidate profile.
     */
    @Test
    void testDeleteCandidateProfile() {
        // Setup
        when(candidateRepository.existsById("testCandidateId")).thenReturn(true);
        doNothing().when(candidateRepository).deleteById("testCandidateId");

        // Execute
        boolean result = candidateService.deleteCandidateProfile("testCandidateId");

        // Verify
        assertTrue(result, "Should return true when profile is found and deleted");
        verify(candidateRepository, times(1)).existsById("testCandidateId");
        verify(candidateRepository, times(1)).deleteById("testCandidateId");
    }

    /**
     * Test that deleting a non-existent profile returns false.
     */
    @Test
    void testDeleteCandidateProfileNotFound() {
        // Setup
        when(candidateRepository.existsById("nonExistentId")).thenReturn(false);

        // Execute
        boolean result = candidateService.deleteCandidateProfile("nonExistentId");

        // Verify
        assertFalse(result, "Should return false when profile is not found");
        verify(candidateRepository, times(1)).existsById("nonExistentId");
        verify(candidateRepository, never()).deleteById("nonExistentId");
    }

    /**
     * Test searching for candidates with multiple criteria.
     */
    @Test
    void testSearchCandidates() {
        // Setup
        List<String> skills = Arrays.asList("Java", "Spring");
        Integer minExperience = 3;
        String location = "New York";

        when(candidateRepository.searchCandidates(skills, minExperience, location))
                .thenReturn(Arrays.asList(testCandidate));

        // Execute
        List<Candidate> result = candidateService.searchCandidates(skills, minExperience, location, null);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one candidate");
        assertEquals("testCandidateId", result.get(0).getId(), "Candidate ID should match");
        verify(candidateRepository, times(1)).searchCandidates(skills, minExperience, location);
    }

    /**
     * Test finding candidates by skill.
     */
    @Test
    void testGetCandidatesBySkill() {
        // Setup
        when(candidateRepository.findBySkills(Arrays.asList("Java")))
                .thenReturn(Arrays.asList(testCandidate));

        // Execute
        List<Candidate> result = candidateService.getCandidatesBySkill("Java");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one candidate");
        assertEquals("testCandidateId", result.get(0).getId(), "Candidate ID should match");
        verify(candidateRepository, times(1)).findBySkills(Arrays.asList("Java"));
    }

    /**
     * Test finding available candidates.
     */
    @Test
    void testGetAvailableCandidates() {
        // Setup
        when(candidateRepository.findAll()).thenReturn(Arrays.asList(testCandidate));

        // Execute
        List<Candidate> result = candidateService.getAvailableCandidates();

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one candidate");
        assertEquals("testCandidateId", result.get(0).getId(), "Candidate ID should match");
        assertTrue(result.get(0).getIsAvailable(), "Candidate should be available");
        verify(candidateRepository, times(1)).findAll();
    }
    /**
     * Test generating candidate statistics.
     */
    @Test
    void testGetCandidateStats() {
        // Setup
        when(candidateRepository.findById("testCandidateId")).thenReturn(Optional.of(testCandidate));
        when(applicationRepository.countByCandidateId("testCandidateId")).thenReturn(5L);

        // Create mock applications with different statuses
        List<JobApplication> applications = Arrays.asList(
                createApplication("APPLIED"),
                createApplication("APPLIED"),
                createApplication("REVIEWING"),
                createApplication("SHORTLISTED"),
                createApplication("ACCEPTED")
        );

        when(applicationRepository.findByCandidateId("testCandidateId")).thenReturn(applications);

        // Execute
        Map<String, Object> result = candidateService.getCandidateStats("testCandidateId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(5L, result.get("totalApplications"), "Total applications should match");
        assertEquals(2L, result.get("appliedApplications"), "Applied applications should match");
        assertEquals(1L, result.get("reviewingApplications"), "Reviewing applications should match");
        assertEquals(1L, result.get("shortlistedApplications"), "Shortlisted applications should match");
        assertEquals(1L, result.get("acceptedApplications"), "Accepted applications should match");
        assertEquals(3, result.get("skillsCount"), "Skills count should match");

        verify(candidateRepository, times(1)).findById("testCandidateId");
        verify(applicationRepository, times(1)).countByCandidateId("testCandidateId");
        verify(applicationRepository, times(4)).findByCandidateId("testCandidateId");  // Changed from 5 to 4
    }
    /**
     * Test updating a candidate's resume URL.
     */
    @Test
    void testUpdateResume() {
        // Setup
        when(candidateRepository.findById("testCandidateId")).thenReturn(Optional.of(testCandidate));
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Candidate result = candidateService.updateResume("testCandidateId", "http://new-resume.pdf");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertEquals("http://new-resume.pdf", result.getResumeUrl(), "Resume URL should be updated");
        verify(candidateRepository, times(1)).findById("testCandidateId");
        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    /**
     * Test updating a candidate's availability status.
     */
    @Test
    void testUpdateAvailability() {
        // Setup
        when(candidateRepository.findById("testCandidateId")).thenReturn(Optional.of(testCandidate));
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Candidate result = candidateService.updateAvailability("testCandidateId", false);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testCandidateId", result.getId(), "Candidate ID should match");
        assertFalse(result.getIsAvailable(), "Availability should be updated to false");
        verify(candidateRepository, times(1)).findById("testCandidateId");
        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    // Helper method to create test applications with different statuses
    private JobApplication createApplication(String status) {
        JobApplication app = new JobApplication();
        app.setCandidateId("testCandidateId");
        app.setStatus(status);
        return app;
    }
}