package me.josephsf.jobportaljosephsfeirtest.controller;

import me.josephsf.jobportaljosephsfeir.controller.JobController;
import me.josephsf.jobportaljosephsfeir.dto.JobDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter;
import me.josephsf.jobportaljosephsfeir.security.JwtUtils;
import me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl;
import me.josephsf.jobportaljosephsfeir.service.JobService;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for JobController.
 * These tests validate that the controller endpoints work correctly with the service layer.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@WebMvcTest(JobController.class)
@ActiveProfiles("test")
public class JobControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    /**
     * Test that all jobs can be retrieved without authentication.
     */
    @Test
    void testGetAllJobsPublicAccess() throws Exception {
        List<Job> jobs = Arrays.asList(testJob);
        Page<Job> jobPage = new PageImpl<>(jobs);

        when(jobService.getAllJobs(any(Pageable.class))).thenReturn(jobPage);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobs", hasSize(1)))
                .andExpect(jsonPath("$.jobs[0].title", is("Software Engineer")))
                .andExpect(jsonPath("$.jobs[0].companyName", is("Tech Company")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(jobService, times(1)).getAllJobs(any(Pageable.class));
    }

    /**
     * Test that a specific job can be retrieved by ID without authentication.
     */
    @Test
    void testGetJobByIdPublicAccess() throws Exception {
        when(jobService.getJobById("testJobId")).thenReturn(testJob);

        mockMvc.perform(get("/api/jobs/testJobId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("testJobId")))
                .andExpect(jsonPath("$.title", is("Software Engineer")))
                .andExpect(jsonPath("$.companyName", is("Tech Company")));

        verify(jobService, times(1)).getJobById("testJobId");
    }

    /**
     * Test that a 404 response is returned when a job cannot be found.
     */
    @Test
    void testGetJobByIdNotFound() throws Exception {
        when(jobService.getJobById("nonExistentJobId")).thenReturn(null);

        mockMvc.perform(get("/api/jobs/nonExistentJobId"))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).getJobById("nonExistentJobId");
    }

    /**
     * Test that a job can be created by a user with RECRUITER role.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testCreateJobAsRecruiter() throws Exception {
        when(jobService.createJob(any(JobDto.class))).thenReturn(testJob);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testJobDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("testJobId")))
                .andExpect(jsonPath("$.title", is("Software Engineer")));

        verify(jobService, times(1)).createJob(any(JobDto.class));
    }

    /**
     * Test that unauthenticated users cannot create jobs.
     */
    @Test
    void testCreateJobUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testJobDto)))
                .andExpect(status().isForbidden());

        verify(jobService, never()).createJob(any(JobDto.class));
    }

    /**
     * Test that users with CANDIDATE role cannot create jobs.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testCreateJobAsCandidate() throws Exception {
        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testJobDto)))
                .andExpect(status().isForbidden());

        verify(jobService, never()).createJob(any(JobDto.class));
    }

    /**
     * Test that a job can be updated by a user with RECRUITER role.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testUpdateJobAsRecruiter() throws Exception {
        when(jobService.updateJob(eq("testJobId"), any(JobDto.class))).thenReturn(testJob);

        mockMvc.perform(put("/api/jobs/testJobId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testJobDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("testJobId")))
                .andExpect(jsonPath("$.title", is("Software Engineer")));

        verify(jobService, times(1)).updateJob(eq("testJobId"), any(JobDto.class));
    }

    /**
     * Test that a 404 response is returned when trying to update a non-existent job.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testUpdateJobNotFound() throws Exception {
        when(jobService.updateJob(eq("nonExistentJobId"), any(JobDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/jobs/nonExistentJobId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testJobDto)))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).updateJob(eq("nonExistentJobId"), any(JobDto.class));
    }

    /**
     * Test that a job can be deleted by a user with RECRUITER role.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testDeleteJobAsRecruiter() throws Exception {
        when(jobService.deleteJob("testJobId")).thenReturn(true);

        mockMvc.perform(delete("/api/jobs/testJobId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Job deleted successfully")));

        verify(jobService, times(1)).deleteJob("testJobId");
    }

    /**
     * Test that a 404 response is returned when trying to delete a non-existent job.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testDeleteJobNotFound() throws Exception {
        when(jobService.deleteJob("nonExistentJobId")).thenReturn(false);

        mockMvc.perform(delete("/api/jobs/nonExistentJobId"))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).deleteJob("nonExistentJobId");
    }

    /**
     * Test that jobs can be searched by keyword without authentication.
     */
    @Test
    void testSearchJobsPublicAccess() throws Exception {
        List<Job> jobs = Arrays.asList(testJob);
        Page<Job> jobPage = new PageImpl<>(jobs);

        when(jobService.searchJobs(eq("Java"), any(Pageable.class))).thenReturn(jobPage);

        mockMvc.perform(get("/api/jobs/search")
                        .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobs", hasSize(1)))
                .andExpect(jsonPath("$.jobs[0].title", is("Software Engineer")))
                .andExpect(jsonPath("$.totalItems", is(1)));

        verify(jobService, times(1)).searchJobs(eq("Java"), any(Pageable.class));
    }

    /**
     * Test that jobs can be filtered by category without authentication.
     */
    @Test
    void testGetJobsByCategoryPublicAccess() throws Exception {
        when(jobService.getJobsByCategory("IT")).thenReturn(Arrays.asList(testJob));

        mockMvc.perform(get("/api/jobs/category/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Software Engineer")))
                .andExpect(jsonPath("$[0].category", is("IT")));

        verify(jobService, times(1)).getJobsByCategory("IT");
    }

    /**
     * Test that jobs can be filtered by location without authentication.
     */
    @Test
    void testGetJobsByLocationPublicAccess() throws Exception {
        when(jobService.getJobsByLocation("New York")).thenReturn(Arrays.asList(testJob));

        mockMvc.perform(get("/api/jobs/location/New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Software Engineer")))
                .andExpect(jsonPath("$[0].location", is("New York")));

        verify(jobService, times(1)).getJobsByLocation("New York");
    }

    /**
     * Test that a recruiter can view their own job postings.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetJobsByRecruiter() throws Exception {
        when(jobService.getJobsByRecruiter("recruiterId123")).thenReturn(Arrays.asList(testJob));

        mockMvc.perform(get("/api/jobs/recruiter/recruiterId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testJobId")));

        verify(jobService, times(1)).getJobsByRecruiter("recruiterId123");
    }
}