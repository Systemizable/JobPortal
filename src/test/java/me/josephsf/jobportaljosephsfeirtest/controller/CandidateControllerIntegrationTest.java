package me.josephsf.jobportaljosephsfeirtest.controller;

import me.josephsf.jobportaljosephsfeir.JobPortalJosephSfeirApplication;
import me.josephsf.jobportaljosephsfeir.controller.CandidateController;
import me.josephsf.jobportaljosephsfeir.controller.JobController;
import me.josephsf.jobportaljosephsfeir.dto.CandidateDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Candidate;
import me.josephsf.jobportaljosephsfeir.model.Education;
import me.josephsf.jobportaljosephsfeir.model.Experience;
import me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter;
import me.josephsf.jobportaljosephsfeir.security.JwtUtils;
import me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl;
import me.josephsf.jobportaljosephsfeir.service.CandidateService;
import me.josephsf.jobportaljosephsfeirtest.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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

/**
 * Integration tests for CandidateController.
 * These tests validate that the candidate profile endpoints work correctly.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@WebMvcTest(CandidateController.class)
@ContextConfiguration(classes = {JobPortalJosephSfeirApplication.class})
@Import(TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)

public class CandidateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Candidate testCandidate;
    private CandidateDto testCandidateDto;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test candidate
        testCandidate = new Candidate();
        testCandidate.setId("candidateId123");
        testCandidate.setUserId("userId123");
        testCandidate.setFirstName("John");
        testCandidate.setLastName("Doe");
        testCandidate.setPhoneNumber("+1234567890");
        testCandidate.setLocation("New York");
        testCandidate.setCurrentTitle("Software Developer");
        testCandidate.setExperienceLevel("MID");
        testCandidate.setYearsOfExperience(5);
        testCandidate.setSkills(Arrays.asList("Java", "Spring Boot", "MongoDB"));
        testCandidate.setExperience(List.of(new Experience()));
        testCandidate.setEducation(List.of(new Education()));
        testCandidate.setResumeUrl("https://example.com/resume.pdf");
        testCandidate.setProfileSummary("Experienced software developer");
        testCandidate.setIsAvailable(true);
        testCandidate.setCreatedAt(LocalDateTime.now());
        testCandidate.setUpdatedAt(LocalDateTime.now());

        // Set up test DTO
        testCandidateDto = new CandidateDto();
        testCandidateDto.setUserId("userId123");
        testCandidateDto.setFirstName("John");
        testCandidateDto.setLastName("Doe");
        testCandidateDto.setPhoneNumber("+1234567890");
        testCandidateDto.setLocation("New York");
        testCandidateDto.setCurrentTitle("Software Developer");
        testCandidateDto.setExperienceLevel("MID");
        testCandidateDto.setYearsOfExperience(5);
        testCandidateDto.setSkills(Arrays.asList("Java", "Spring Boot", "MongoDB"));
        testCandidateDto.setExperience(List.of(new Experience()));
        testCandidateDto.setEducation(List.of(new Education()));
        testCandidateDto.setResumeUrl("https://example.com/resume.pdf");
        testCandidateDto.setProfileSummary("Experienced software developer");
        testCandidateDto.setIsAvailable(true);
    }

    /**
     * Test retrieving a candidate profile by ID.
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER", "ADMIN"})
    void testGetCandidateById() throws Exception {
        when(candidateService.getCandidateById("candidateId123")).thenReturn(testCandidate);

        mockMvc.perform(get("/api/candidates/candidateId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.location", is("New York")));

        verify(candidateService, times(1)).getCandidateById("candidateId123");
    }

    /**
     * Test that a 404 response is returned when a candidate cannot be found.
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "RECRUITER", "ADMIN"})
    void testGetCandidateByIdNotFound() throws Exception {
        when(candidateService.getCandidateById("nonExistentId")).thenReturn(null);

        mockMvc.perform(get("/api/candidates/nonExistentId"))
                .andExpect(status().isNotFound());

        verify(candidateService, times(1)).getCandidateById("nonExistentId");
    }

    /**
     * Test retrieving a candidate profile by user ID.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testGetCandidateByUserId() throws Exception {
        when(candidateService.getCandidateByUserId("userId123")).thenReturn(testCandidate);

        mockMvc.perform(get("/api/candidates/user/userId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(candidateService, times(1)).getCandidateByUserId("userId123");
    }

    /**
     * Test creating a new candidate profile.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testCreateCandidateProfile() throws Exception {
        when(candidateService.createCandidateProfile(any(CandidateDto.class))).thenReturn(testCandidate);

        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCandidateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(candidateService, times(1)).createCandidateProfile(any(CandidateDto.class));
    }

    /**
     * Test handling of duplicate profile creation.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testCreateCandidateProfileDuplicate() throws Exception {
        when(candidateService.createCandidateProfile(any(CandidateDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCandidateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already exists")));

        verify(candidateService, times(1)).createCandidateProfile(any(CandidateDto.class));
    }

    /**
     * Test updating a candidate profile.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testUpdateCandidateProfile() throws Exception {
        when(candidateService.updateCandidateProfile(eq("candidateId123"), any(CandidateDto.class)))
                .thenReturn(testCandidate);

        mockMvc.perform(put("/api/candidates/candidateId123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCandidateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(candidateService, times(1))
                .updateCandidateProfile(eq("candidateId123"), any(CandidateDto.class));
    }

    /**
     * Test deleting a candidate profile.
     */
    @Test
    @WithMockUser(roles = {"CANDIDATE", "ADMIN"})
    void testDeleteCandidateProfile() throws Exception {
        when(candidateService.deleteCandidateProfile("candidateId123")).thenReturn(true);

        mockMvc.perform(delete("/api/candidates/candidateId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));

        verify(candidateService, times(1)).deleteCandidateProfile("candidateId123");
    }

    /**
     * Test searching for candidates by various criteria.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testSearchCandidates() throws Exception {
        when(candidateService.searchCandidates(
                eq(Arrays.asList("Java")), eq(3), eq("New York"), eq("MID")))
                .thenReturn(Arrays.asList(testCandidate));

        mockMvc.perform(get("/api/candidates/search")
                        .param("skills", "Java")
                        .param("minExperience", "3")
                        .param("location", "New York")
                        .param("experienceLevel", "MID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].location", is("New York")));

        verify(candidateService, times(1))
                .searchCandidates(eq(Arrays.asList("Java")), eq(3), eq("New York"), eq("MID"));
    }

    /**
     * Test finding candidates by skill.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetCandidatesBySkill() throws Exception {
        when(candidateService.getCandidatesBySkill("Java"))
                .thenReturn(Arrays.asList(testCandidate));

        mockMvc.perform(get("/api/candidates/skills/Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("candidateId123")))
                .andExpect(jsonPath("$[0].skills", contains("Java", "Spring Boot", "MongoDB")));

        verify(candidateService, times(1)).getCandidatesBySkill("Java");
    }

    /**
     * Test updating a candidate's resume URL.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testUpdateResume() throws Exception {
        when(candidateService.updateResume("candidateId123", "https://example.com/new-resume.pdf"))
                .thenReturn(testCandidate);

        mockMvc.perform(put("/api/candidates/candidateId123/resume")
                        .param("resumeUrl", "https://example.com/new-resume.pdf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")));

        verify(candidateService, times(1))
                .updateResume("candidateId123", "https://example.com/new-resume.pdf");
    }

    /**
     * Test updating a candidate's availability status.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testUpdateAvailability() throws Exception {
        when(candidateService.updateAvailability("candidateId123", false))
                .thenReturn(testCandidate);

        mockMvc.perform(put("/api/candidates/candidateId123/availability")
                        .param("isAvailable", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("candidateId123")));

        verify(candidateService, times(1))
                .updateAvailability("candidateId123", false);
    }

    /**
     * Test getting available candidates.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetAvailableCandidates() throws Exception {
        when(candidateService.getAvailableCandidates())
                .thenReturn(Arrays.asList(testCandidate));

        mockMvc.perform(get("/api/candidates/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("candidateId123")))
                .andExpect(jsonPath("$[0].isAvailable", is(true)));

        verify(candidateService, times(1)).getAvailableCandidates();
    }

    /**
     * Test getting candidate statistics.
     */
    @Test
    @WithMockUser(roles = "CANDIDATE")
    void testGetCandidateStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalApplications", 10L);
        stats.put("appliedApplications", 5L);
        stats.put("reviewingApplications", 3L);
        stats.put("shortlistedApplications", 1L);
        stats.put("acceptedApplications", 1L);
        stats.put("skillsCount", 3);

        when(candidateService.getCandidateStats("candidateId123")).thenReturn(stats);

        mockMvc.perform(get("/api/candidates/candidateId123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalApplications", is(10)))
                .andExpect(jsonPath("$.appliedApplications", is(5)))
                .andExpect(jsonPath("$.reviewingApplications", is(3)))
                .andExpect(jsonPath("$.shortlistedApplications", is(1)))
                .andExpect(jsonPath("$.acceptedApplications", is(1)))
                .andExpect(jsonPath("$.skillsCount", is(3)));

        verify(candidateService, times(1)).getCandidateStats("candidateId123");
    }
}