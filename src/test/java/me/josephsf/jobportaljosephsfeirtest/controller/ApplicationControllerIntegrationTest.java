package me.josephsf.jobportaljosephsfeirtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephsf.jobportaljosephsfeir.JobPortalJosephSfeirApplication;
import me.josephsf.jobportaljosephsfeir.controller.ApplicationController;
import me.josephsf.jobportaljosephsfeir.dto.ApplicationDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.JobApplication;
import me.josephsf.jobportaljosephsfeir.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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

/**
 * Integration tests for the ApplicationController.
 * Tests all endpoints and verifies proper handling of requests and responses.
 */
@SpringBootTest(classes = JobPortalJosephSfeirApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private JobApplication testApplication;
    private ApplicationDto testApplicationDto;

    /**
     * Sets up test data before each test execution.
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
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testApplyToJob() throws Exception {
        when(applicationService.applyToJob(any(ApplicationDto.class)))
                .thenReturn(testApplication);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testApplicationDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.jobId", is("jobId123")))
                .andExpect(jsonPath("$.candidateId", is("candidateId123")))
                .andExpect(jsonPath("$.status", is("APPLIED")));

        verify(applicationService, times(1)).applyToJob(any(ApplicationDto.class));
    }

    /**
     * Tests handling of duplicate job applications.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testApplyToJobDuplicate() throws Exception {
        when(applicationService.applyToJob(any(ApplicationDto.class)))
                .thenReturn(null); // Indicates duplicate application

        ApiResponseDto errorResponse = new ApiResponseDto(false, "You have already applied to this job");

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testApplicationDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already applied")));

        verify(applicationService, times(1)).applyToJob(any(ApplicationDto.class));
    }

    /**
     * Tests retrieving a job application by ID.
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER"})
    void testGetApplicationById() throws Exception {
        when(applicationService.getApplicationById("appId123"))
                .thenReturn(testApplication);

        mockMvc.perform(get("/api/applications/appId123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.jobId", is("jobId123")))
                .andExpect(jsonPath("$.candidateId", is("candidateId123")))
                .andExpect(jsonPath("$.status", is("APPLIED")));

        verify(applicationService, times(1)).getApplicationById("appId123");
    }

    /**
     * Tests the 404 response when an application is not found.
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER"})
    void testGetApplicationByIdNotFound() throws Exception {
        when(applicationService.getApplicationById("nonExistentId"))
                .thenReturn(null);

        mockMvc.perform(get("/api/applications/nonExistentId"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

        verify(applicationService, times(1)).getApplicationById("nonExistentId");
    }

    /**
     * Tests retrieving applications by candidate ID.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testGetApplicationsByCandidate() throws Exception {
        List<JobApplication> applications = Arrays.asList(testApplication);
        Page<JobApplication> applicationPage = new PageImpl<>(applications);

        when(applicationService.getApplicationsByCandidate(eq("candidateId123"), any(Pageable.class)))
                .thenReturn(applicationPage);

        // Create a response structure that matches what the controller produces
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("applications", applications);
        expectedResponse.put("currentPage", 0);
        expectedResponse.put("totalItems", 1L);
        expectedResponse.put("totalPages", 1);

        mockMvc.perform(get("/api/applications/candidate/candidateId123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applications", hasSize(1)))
                .andExpect(jsonPath("$.applications[0].id", is("appId123")))
                .andExpect(jsonPath("$.applications[0].status", is("APPLIED")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(applicationService, times(1))
                .getApplicationsByCandidate(eq("candidateId123"), any(Pageable.class));
    }

    /**
     * Tests retrieving applications for a specific job.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetApplicationsByJob() throws Exception {
        List<JobApplication> applications = Arrays.asList(testApplication);
        Page<JobApplication> applicationPage = new PageImpl<>(applications);

        when(applicationService.getApplicationsByJob(eq("jobId123"), any(Pageable.class)))
                .thenReturn(applicationPage);

        // Create a response structure that matches what the controller produces
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("applications", applications);
        expectedResponse.put("currentPage", 0);
        expectedResponse.put("totalItems", 1L);
        expectedResponse.put("totalPages", 1);

        mockMvc.perform(get("/api/applications/job/jobId123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applications", hasSize(1)))
                .andExpect(jsonPath("$.applications[0].id", is("appId123")))
                .andExpect(jsonPath("$.applications[0].jobId", is("jobId123")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(applicationService, times(1))
                .getApplicationsByJob(eq("jobId123"), any(Pageable.class));
    }

    /**
     * Tests updating an application's status.
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.status", is("REVIEWING")))
                .andExpect(jsonPath("$.reviewNotes", is("Good candidate, proceed to interview")));

        verify(applicationService, times(1))
                .updateApplicationStatus(eq("appId123"), eq("REVIEWING"), eq("Good candidate, proceed to interview"));
    }

    /**
     * Tests adding review notes to an application.
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.reviewNotes", is("Impressive technical skills")));

        verify(applicationService, times(1))
                .addReviewNotes(eq("appId123"), eq("Impressive technical skills"));
    }

    /**
     * Tests adding interview notes to an application.
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("appId123")))
                .andExpect(jsonPath("$.interviewNotes", is("Good communication skills, technically strong")));

        verify(applicationService, times(1))
                .addInterviewNotes(eq("appId123"), eq("Good communication skills, technically strong"));
    }

    /**
     * Tests withdrawing (deleting) a job application.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testWithdrawApplication() throws Exception {
        when(applicationService.withdrawApplication("appId123"))
                .thenReturn(true);

        mockMvc.perform(delete("/api/applications/appId123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("withdrawn successfully")));

        verify(applicationService, times(1)).withdrawApplication("appId123");
    }

    /**
     * Tests retrieving application statistics for a job.
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total", is(10)))
                .andExpect(jsonPath("$.applied", is(5)))
                .andExpect(jsonPath("$.reviewing", is(3)))
                .andExpect(jsonPath("$.shortlisted", is(1)))
                .andExpect(jsonPath("$.rejected", is(0)))
                .andExpect(jsonPath("$.accepted", is(1)));

        verify(applicationService, times(1)).getApplicationStats("jobId123");
    }
}