package me.josephsf.jobportaljosephsfeirtest.service;

import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.repository.ApplicationRepository;
import me.josephsf.jobportaljosephsfeir.service.ApplicationService;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ApplicationService.
 * These tests validate the service layer logic for job applications.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private JobApplication testApplication;
    private ApplicationDto testApplicationDto;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test application data
        testApplication = new JobApplication();
        testApplication.setId("testAppId");
        testApplication.setJobId("testJobId");
        testApplication.setCandidateId("testCandidateId");
        testApplication.setStatus("APPLIED");
        testApplication.setCoverLetter("I am very interested in this position");
        testApplication.setResumeUrl("http://example.com/resume.pdf");
        testApplication.setApplicationDate(LocalDateTime.now());
        testApplication.setCreatedAt(LocalDateTime.now());
        testApplication.setUpdatedAt(LocalDateTime.now());

        testApplicationDto = new ApplicationDto();
        testApplicationDto.setJobId("testJobId");
        testApplicationDto.setCandidateId("testCandidateId");
        testApplicationDto.setCoverLetter("I am very interested in this position");
        testApplicationDto.setResumeUrl("http://example.com/resume.pdf");
    }

    /**
     * Test applying to a job for the first time.
     */
    @Test
    void testApplyToJob() {
        // Setup
        when(applicationRepository.findByCandidateIdAndJobId("testCandidateId", "testJobId"))
                .thenReturn(Optional.empty());
        when(applicationRepository.save(any(JobApplication.class))).thenReturn(testApplication);

        // Execute
        JobApplication result = applicationService.applyToJob(testApplicationDto);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testAppId", result.getId(), "Application ID should match");
        assertEquals("testJobId", result.getJobId(), "Job ID should match");
        assertEquals("testCandidateId", result.getCandidateId(), "Candidate ID should match");
        assertEquals("APPLIED", result.getStatus(), "Status should be APPLIED");

        verify(applicationRepository, times(1)).findByCandidateIdAndJobId("testCandidateId", "testJobId");
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    /**
     * Test that a candidate cannot apply to the same job twice.
     */
    @Test
    void testApplyToJobAlreadyApplied() {
        // Setup
        when(applicationRepository.findByCandidateIdAndJobId("testCandidateId", "testJobId"))
                .thenReturn(Optional.of(testApplication));

        // Execute
        JobApplication result = applicationService.applyToJob(testApplicationDto);

        // Verify
        assertNull(result, "Result should be null since candidate already applied");
        verify(applicationRepository, times(1)).findByCandidateIdAndJobId("testCandidateId", "testJobId");
        verify(applicationRepository, never()).save(any(JobApplication.class));
    }

    /**
     * Test retrieving an application by ID.
     */
    @Test
    void testGetApplicationById() {
        // Setup
        when(applicationRepository.findById("testAppId")).thenReturn(Optional.of(testApplication));

        // Execute
        JobApplication result = applicationService.getApplicationById("testAppId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testAppId", result.getId(), "Application ID should match");
        assertEquals("testJobId", result.getJobId(), "Job ID should match");
        verify(applicationRepository, times(1)).findById("testAppId");
    }

    /**
     * Test retrieving all applications submitted by a candidate.
     */
    @Test
    void testGetApplicationsByCandidate() {
        // Setup
        Page<JobApplication> applicationPage = new PageImpl<>(List.of(testApplication));
        when(applicationRepository.findByCandidateId(eq("testCandidateId"), any(Pageable.class)))
                .thenReturn(applicationPage);

        // Execute
        Page<JobApplication> result = applicationService.getApplicationsByCandidate("testCandidateId", Pageable.unpaged());

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "Should find one application");
        assertEquals("testAppId", result.getContent().get(0).getId(), "Application ID should match");
        verify(applicationRepository, times(1)).findByCandidateId(eq("testCandidateId"), any(Pageable.class));
    }

    /**
     * Test retrieving all applications for a specific job.
     */
    @Test
    void testGetApplicationsByJob() {
        // Setup
        Page<JobApplication> applicationPage = new PageImpl<>(List.of(testApplication));
        when(applicationRepository.findByJobId(eq("testJobId"), any(Pageable.class)))
                .thenReturn(applicationPage);

        // Execute
        Page<JobApplication> result = applicationService.getApplicationsByJob("testJobId", Pageable.unpaged());

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "Should find one application");
        assertEquals("testAppId", result.getContent().get(0).getId(), "Application ID should match");
        verify(applicationRepository, times(1)).findByJobId(eq("testJobId"), any(Pageable.class));
    }

    /**
     * Test retrieving applications with a specific status.
     */
    @Test
    void testGetApplicationsByStatus() {
        // Setup
        when(applicationRepository.findByStatus("APPLIED"))
                .thenReturn(List.of(testApplication));

        // Execute
        List<JobApplication> result = applicationService.getApplicationsByStatus("APPLIED");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one application");
        assertEquals("testAppId", result.get(0).getId(), "Application ID should match");
        assertEquals("APPLIED", result.get(0).getStatus(), "Status should match");
        verify(applicationRepository, times(1)).findByStatus("APPLIED");
    }

    /**
     * Test updating an application's status.
     */
    @Test
    void testUpdateApplicationStatus() {
        // Setup
        when(applicationRepository.findById("testAppId")).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        JobApplication result = applicationService.updateApplicationStatus("testAppId", "REVIEWING", "Good candidate");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testAppId", result.getId(), "Application ID should match");
        assertEquals("REVIEWING", result.getStatus(), "Status should be updated to REVIEWING");
        assertEquals("Good candidate", result.getReviewNotes(), "Review notes should be updated");
        assertNotNull(result.getReviewDate(), "Review date should be set");
        verify(applicationRepository, times(1)).findById("testAppId");
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    /**
     * Test that updating a non-existent application returns null.
     */
    @Test
    void testUpdateApplicationStatusNotFound() {
        // Setup
        when(applicationRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        // Execute
        JobApplication result = applicationService.updateApplicationStatus("nonExistentId", "REVIEWING", "Good candidate");

        // Verify
        assertNull(result, "Result should be null for non-existent application");
        verify(applicationRepository, times(1)).findById("nonExistentId");
        verify(applicationRepository, never()).save(any(JobApplication.class));
    }

    /**
     * Test adding review notes to an application.
     */
    @Test
    void testAddReviewNotes() {
        // Setup
        when(applicationRepository.findById("testAppId")).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        JobApplication result = applicationService.addReviewNotes("testAppId", "Excellent communication skills");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testAppId", result.getId(), "Application ID should match");
        assertEquals("Excellent communication skills", result.getReviewNotes(), "Review notes should be updated");
        verify(applicationRepository, times(1)).findById("testAppId");
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    /**
     * Test adding interview notes to an application.
     */
    @Test
    void testAddInterviewNotes() {
        // Setup
        when(applicationRepository.findById("testAppId")).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        JobApplication result = applicationService.addInterviewNotes("testAppId", "Interview went well");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals("testAppId", result.getId(), "Application ID should match");
        assertEquals("Interview went well", result.getInterviewNotes(), "Interview notes should be updated");
        verify(applicationRepository, times(1)).findById("testAppId");
        verify(applicationRepository, times(1)).save(any(JobApplication.class));
    }

    /**
     * Test withdrawing (deleting) an application.
     */
    @Test
    void testWithdrawApplication() {
        // Setup
        when(applicationRepository.existsById("testAppId")).thenReturn(true);
        doNothing().when(applicationRepository).deleteById("testAppId");

        // Execute
        boolean result = applicationService.withdrawApplication("testAppId");

        // Verify
        assertTrue(result, "Should return true when application is found and deleted");
        verify(applicationRepository, times(1)).existsById("testAppId");
        verify(applicationRepository, times(1)).deleteById("testAppId");
    }

    /**
     * Test that withdrawing a non-existent application returns false.
     */
    @Test
    void testWithdrawApplicationNotFound() {
        // Setup
        when(applicationRepository.existsById("nonExistentId")).thenReturn(false);

        // Execute
        boolean result = applicationService.withdrawApplication("nonExistentId");

        // Verify
        assertFalse(result, "Should return false when application is not found");
        verify(applicationRepository, times(1)).existsById("nonExistentId");
        verify(applicationRepository, never()).deleteById("nonExistentId");
    }

    /**
     * Test retrieving application statistics for a job.
     */
    @Test
    void testGetApplicationStats() {
        // Setup
        when(applicationRepository.countByJobId("testJobId")).thenReturn(10L);
        when(applicationRepository.countByJobIdAndStatus("testJobId", "APPLIED")).thenReturn(4L);
        when(applicationRepository.countByJobIdAndStatus("testJobId", "REVIEWING")).thenReturn(3L);
        when(applicationRepository.countByJobIdAndStatus("testJobId", "SHORTLISTED")).thenReturn(2L);
        when(applicationRepository.countByJobIdAndStatus("testJobId", "REJECTED")).thenReturn(0L);
        when(applicationRepository.countByJobIdAndStatus("testJobId", "ACCEPTED")).thenReturn(1L);

        // Execute
        Map<String, Long> result = applicationService.getApplicationStats("testJobId");

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(10L, result.get("total"), "Total applications should match");
        assertEquals(4L, result.get("applied"), "Applied applications should match");
        assertEquals(3L, result.get("reviewing"), "Reviewing applications should match");
        assertEquals(2L, result.get("shortlisted"), "Shortlisted applications should match");
        assertEquals(0L, result.get("rejected"), "Rejected applications should match");
        assertEquals(1L, result.get("accepted"), "Accepted applications should match");

        verify(applicationRepository, times(1)).countByJobId("testJobId");
        verify(applicationRepository, times(1)).countByJobIdAndStatus("testJobId", "APPLIED");
        verify(applicationRepository, times(1)).countByJobIdAndStatus("testJobId", "REVIEWING");
        verify(applicationRepository, times(1)).countByJobIdAndStatus("testJobId", "SHORTLISTED");
        verify(applicationRepository, times(1)).countByJobIdAndStatus("testJobId", "REJECTED");
        verify(applicationRepository, times(1)).countByJobIdAndStatus("testJobId", "ACCEPTED");
    }

    /**
     * Test retrieving recent applications.
     */
    @Test
    void testGetRecentApplications() {
        // Setup
        when(applicationRepository.findByApplicationDateAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(testApplication));

        // Execute
        List<JobApplication> result = applicationService.getRecentApplications(7);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find one recent application");
        assertEquals("testAppId", result.get(0).getId(), "Application ID should match");
        verify(applicationRepository, times(1)).findByApplicationDateAfter(any(LocalDateTime.class));
    }
}