/**
 * Integration tests for ApplicationController.
 * <p>
 * This test class verifies the functionality of the job application endpoints in the
 * Job Portal API. It uses Spring's MockMvc to simulate HTTP requests and validate
 * responses without requiring a fully deployed application. The tests cover all major
 * endpoints related to job applications, including submission, retrieval, and status updates.
 * </p>
 * <p>
 * Test scenarios include:
 * </p>
 * <ul>
 *   <li>Job application submission by candidates</li>
 *   <li>Handling of duplicate applications</li>
 *   <li>Retrieving application details by ID</li>
 *   <li>Listing applications by candidate and by job</li>
 *   <li>Updating application status and notes by recruiters</li>
 *   <li>Application withdrawal by candidates</li>
 *   <li>Retrieving application statistics</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
package me.josephsf.jobportaljosephsfeirtest.controller;
import me.josephsf.jobportaljosephsfeir.controller.ApplicationController;
import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter;
import me.josephsf.jobportaljosephsfeir.security.JwtUtils;
import me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl;
import me.josephsf.jobportaljosephsfeir.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@ActiveProfiles("test")
public class ApplicationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private JobApplication testApplication;
    private ApplicationDto testApplicationDto;

    /**
     * Sets up test data before each test execution.
     * <p>
     * This method initializes the test application and application DTO objects
     * with sample data that will be used across multiple test cases. It ensures
     * that each test starts with a clean, consistent state.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Set up test application
        testApplication = new JobApplication();
        testApplication.setId("appId123");
        testApplication.setJobId("jobId123");
        testApplication.setCandidateId("candidateId123");
        testApplication.setStatus("APPLIED");
        testApplication.setCoverLetter("I am very interested in this position");
        testApplication.setResumeUrl("https://example.com/resume.pdf");
        testApplication.setApplicationDate(LocalDateTime.now());
        testApplication.setCreatedAt(LocalDateTime.now());
        testApplication.setUpdatedAt(LocalDateTime.now());

        // Set up test DTO
        testApplicationDto = new ApplicationDto();
        testApplicationDto.setJobId("jobId123");
        testApplicationDto.setCandidateId("candidateId123");
        testApplicationDto.setCoverLetter("I am very interested in this position");
        testApplicationDto.setResumeUrl("https://example.com/resume.pdf");
    }

    /**
     * Tests the endpoint for submitting a job application.
     * <p>
     * This test verifies that candidates can successfully submit job applications
     * through the API. It checks that the application service is called with the
     * correct parameters and that the response contains the expected application details.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testApplyToJob() throws Exception {
        when(applicationService.applyToJob(any(ApplicationDto.class)))
                .thenReturn(testApplication);
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testApplicationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.jobId", is("jobId123")))
                .andExpect(jsonPath("$.candidateId", is("candidateId123")))
                .andExpect(jsonPath("$.status", is("APPLIED")));

        verify(applicationService, times(1)).applyToJob(any(ApplicationDto.class));
    }

    /**
     * Tests handling of duplicate job applications.
     * <p>
     * This test verifies that the system prevents candidates from submitting
     * multiple applications to the same job. It checks that the response includes
     * an appropriate error message when a duplicate application is detected.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testApplyToJobDuplicate() throws Exception {
        when(applicationService.applyToJob(any(ApplicationDto.class)))
                .thenReturn(null); // Indicates duplicate application

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testApplicationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already applied")));

        verify(applicationService, times(1)).applyToJob(any(ApplicationDto.class));
    }

    /**
     * Tests retrieving a job application by ID.
     * <p>
     * This test verifies that authorized users (candidates and recruiters) can
     * retrieve the details of a specific job application by its ID. It checks that
     * the response contains the expected application information.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER"})
    void testGetApplicationById() throws Exception {
        when(applicationService.getApplicationById("appId123"))
                .thenReturn(testApplication);

        mockMvc.perform(get("/api/applications/appId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.jobId", is("jobId123")))
                .andExpect(jsonPath("$.candidateId", is("candidateId123")))
                .andExpect(jsonPath("$.status", is("APPLIED")));

        verify(applicationService, times(1)).getApplicationById("appId123");
    }

    /**
     * Tests the 404 response when an application is not found.
     * <p>
     * This test verifies that the system returns an appropriate 404 Not Found
     * response when a user attempts to retrieve an application that doesn't exist.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER"})
    void testGetApplicationByIdNotFound() throws Exception {
        when(applicationService.getApplicationById("nonExistentId"))
                .thenReturn(null);

        mockMvc.perform(get("/api/applications/nonExistentId"))
                .andExpect(status().isNotFound());

        verify(applicationService, times(1)).getApplicationById("nonExistentId");
    }

    /**
     * Tests retrieving applications by candidate ID.
     * <p>
     * This test verifies that candidates can retrieve a list of their own job
     * applications. It checks that the response includes pagination information
     * and the expected application details.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testGetApplicationsByCandidate() throws Exception {
        List<JobApplication> applications = Arrays.asList(testApplication);
        Page<JobApplication> applicationPage = new PageImpl<>(applications);

        when(applicationService.getApplicationsByCandidate(eq("candidateId123"), any(Pageable.class)))
                .thenReturn(applicationPage);

        mockMvc.perform(get("/api/applications/candidate/candidateId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applications", hasSize(1)))
                .andExpect(jsonPath("$.applications[0].id", is("appId123")))
                .andExpect(jsonPath("$.applications[0].status", is("APPLIED")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(applicationService, times(1))
                .getApplicationsByCandidate(eq("candidateId123"), any(Pageable.class));
    }

    /**
     * Tests retrieving applications for a specific job.
     * <p>
     * This test verifies that recruiters can retrieve all applications submitted
     * for a particular job posting. It checks that the response includes pagination
     * information and the expected application details.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetApplicationsByJob() throws Exception {
        List<JobApplication> applications = Arrays.asList(testApplication);
        Page<JobApplication> applicationPage = new PageImpl<>(applications);

        when(applicationService.getApplicationsByJob(eq("jobId123"), any(Pageable.class)))
                .thenReturn(applicationPage);

        mockMvc.perform(get("/api/applications/job/jobId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applications", hasSize(1)))
                .andExpect(jsonPath("$.applications[0].id", is("appId123")))
                .andExpect(jsonPath("$.applications[0].jobId", is("jobId123")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(applicationService, times(1))
                .getApplicationsByJob(eq("jobId123"), any(Pageable.class));
    }

    /**
     * Tests updating an application's status.
     * <p>
     * This test verifies that recruiters can update the status of job applications
     * (e.g., to "REVIEWING", "SHORTLISTED", "ACCEPTED", or "REJECTED") and add
     * review notes. It checks that the response contains the updated application details.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testUpdateApplicationStatus() throws Exception {
        // Create updated application with new status
        JobApplication updatedApplication = new JobApplication();
        updatedApplication.setId("appId123");
        updatedApplication.setJobId("jobId123");
        updatedApplication.setCandidateId("candidateId123");
        updatedApplication.setStatus("REVIEWING");
        updatedApplication.setReviewNotes("Good candidate, proceed to interview");
        updatedApplication.setCoverLetter("I am very interested in this position");
        updatedApplication.setResumeUrl("https://example.com/resume.pdf");
        updatedApplication.setApplicationDate(LocalDateTime.now());
        updatedApplication.setReviewDate(LocalDateTime.now());

        when(applicationService.updateApplicationStatus(
                eq("appId123"), eq("REVIEWING"), eq("Good candidate, proceed to interview")))
                .thenReturn(updatedApplication);

        mockMvc.perform(put("/api/applications/appId123/status")
                        .param("status", "REVIEWING")
                        .param("reviewNotes", "Good candidate, proceed to interview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.status", is("REVIEWING")))
                .andExpect(jsonPath("$.reviewNotes", is("Good candidate, proceed to interview")));

        verify(applicationService, times(1))
                .updateApplicationStatus(eq("appId123"), eq("REVIEWING"), eq("Good candidate, proceed to interview"));
    }

    /**
     * Tests adding review notes to an application.
     * <p>
     * This test verifies that recruiters can add or update review notes for
     * job applications without changing their status. It checks that the response
     * contains the updated application with the new review notes.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testAddReviewNotes() throws Exception {
        JobApplication updatedApplication = new JobApplication();
        updatedApplication.setId("appId123");
        updatedApplication.setJobId("jobId123");
        updatedApplication.setCandidateId("candidateId123");
        updatedApplication.setStatus("APPLIED");
        updatedApplication.setReviewNotes("Impressive technical skills");
        updatedApplication.setCoverLetter("I am very interested in this position");
        updatedApplication.setResumeUrl("https://example.com/resume.pdf");
        updatedApplication.setApplicationDate(LocalDateTime.now());

        when(applicationService.addReviewNotes(eq("appId123"), eq("Impressive technical skills")))
                .thenReturn(updatedApplication);

        mockMvc.perform(put("/api/applications/appId123/review")
                        .param("reviewNotes", "Impressive technical skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.reviewNotes", is("Impressive technical skills")));

        verify(applicationService, times(1))
                .addReviewNotes(eq("appId123"), eq("Impressive technical skills"));
    }

    /**
     * Tests adding interview notes to an application.
     * <p>
     * This test verifies that recruiters can add or update interview notes for
     * job applications that have reached the interview stage. It checks that the
     * response contains the updated application with the new interview notes.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testAddInterviewNotes() throws Exception {
        JobApplication updatedApplication = new JobApplication();
        updatedApplication.setId("appId123");
        updatedApplication.setJobId("jobId123");
        updatedApplication.setCandidateId("candidateId123");
        updatedApplication.setStatus("SHORTLISTED");
        updatedApplication.setInterviewNotes("Good communication skills, technically strong");
        updatedApplication.setCoverLetter("I am very interested in this position");
        updatedApplication.setResumeUrl("https://example.com/resume.pdf");
        updatedApplication.setApplicationDate(LocalDateTime.now());

        when(applicationService.addInterviewNotes(
                eq("appId123"), eq("Good communication skills, technically strong")))
                .thenReturn(updatedApplication);

        mockMvc.perform(put("/api/applications/appId123/interview")
                        .param("interviewNotes", "Good communication skills, technically strong"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.interviewNotes", is("Good communication skills, technically strong")));

        verify(applicationService, times(1))
                .addInterviewNotes(eq("appId123"), eq("Good communication skills, technically strong"));
    }

    /**
     * Tests withdrawing (deleting) a job application.
     * <p>
     * This test verifies that candidates can withdraw their job applications,
     * effectively removing them from the system. It checks that the response
     * includes a success message after a successful withdrawal.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testWithdrawApplication() throws Exception {
        when(applicationService.withdrawApplication("appId123"))
                .thenReturn(true);

        mockMvc.perform(delete("/api/applications/appId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("withdrawn successfully")));

        verify(applicationService, times(1)).withdrawApplication("appId123");
    }

    /**
     * Tests retrieving application statistics for a job.
     * <p>
     * This test verifies that recruiters can retrieve statistics about applications
     * for a specific job, including counts of applications in various statuses.
     * It checks that the response contains all the expected statistical metrics.
     * </p>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetApplicationStats() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", 10L);
        stats.put("applied", 5L);
        stats.put("reviewing", 3L);
        stats.put("shortlisted", 1L);
        stats.put("rejected", 0L);
        stats.put("accepted", 1L);

        when(applicationService.getApplicationStats("jobId123"))
                .thenReturn(stats);

        mockMvc.perform(get("/api/applications/stats/job/jobId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(10)))
                .andExpect(jsonPath("$.applied", is(5)))
                .andExpect(jsonPath("$.reviewing", is(3)))
                .andExpect(jsonPath("$.shortlisted", is(1)))
                .andExpect(jsonPath("$.rejected", is(0)))
                .andExpect(jsonPath("$.accepted", is(1)));

        verify(applicationService, times(1)).getApplicationStats("jobId123");
    }
}