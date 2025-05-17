package me.josephsf.jobportaljosephsfeirtest.controller;

import me.josephsf.jobportaljosephsfeir.controller.RecruiterController;
import me.josephsf.jobportaljosephsfeir.dto.RecruiterDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Recruiter;
import me.josephsf.jobportaljosephsfeir.security.JwtAuthTokenFilter;
import me.josephsf.jobportaljosephsfeir.security.JwtUtils;
import me.josephsf.jobportaljosephsfeir.security.UserDetailsServiceImpl;
import me.josephsf.jobportaljosephsfeir.service.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

/**
 * Integration tests for RecruiterController.
 * These tests validate that the recruiter profile endpoints work correctly.
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@WebMvcTest(RecruiterController.class)
@ActiveProfiles("test")
public class RecruiterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruiterService recruiterService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Recruiter testRecruiter;
    private RecruiterDto testRecruiterDto;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Set up test recruiter
        testRecruiter = new Recruiter();
        testRecruiter.setId("recruiterId123");
        testRecruiter.setUserId("userId123");
        testRecruiter.setCompanyName("Tech Corporation");
        testRecruiter.setCompanySize("LARGE");
        testRecruiter.setLocation("San Francisco");
        testRecruiter.setIndustry("Technology");
        testRecruiter.setDepartment("Engineering");
        testRecruiter.setPosition("HR Manager");
        testRecruiter.setPhoneNumber("+1234567890");
        testRecruiter.setCompanyWebsite("https://techcorp.com");
        testRecruiter.setLinkedInUrl("https://linkedin.com/in/recruiter");
        testRecruiter.setCompanyDescription("Leading technology company");
        testRecruiter.setIsVerified(true);
        testRecruiter.setCreatedAt(LocalDateTime.now());
        testRecruiter.setUpdatedAt(LocalDateTime.now());

        // Set up test DTO
        testRecruiterDto = new RecruiterDto();
        testRecruiterDto.setUserId("userId123");
        testRecruiterDto.setCompanyName("Tech Corporation");
        testRecruiterDto.setCompanySize("LARGE");
        testRecruiterDto.setLocation("San Francisco");
        testRecruiterDto.setIndustry("Technology");
        testRecruiterDto.setDepartment("Engineering");
        testRecruiterDto.setPosition("HR Manager");
        testRecruiterDto.setPhoneNumber("+1234567890");
        testRecruiterDto.setCompanyWebsite("https://techcorp.com");
        testRecruiterDto.setLinkedInUrl("https://linkedin.com/in/recruiter");
        testRecruiterDto.setCompanyDescription("Leading technology company");
    }

    /**
     * Test retrieving a recruiter profile by ID.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testGetRecruiterById() throws Exception {
        when(recruiterService.getRecruiterById("recruiterId123")).thenReturn(testRecruiter);

        mockMvc.perform(get("/api/recruiters/recruiterId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("recruiterId123")))
                .andExpect(jsonPath("$.companyName", is("Tech Corporation")))
                .andExpect(jsonPath("$.location", is("San Francisco")));

        verify(recruiterService, times(1)).getRecruiterById("recruiterId123");
    }

    /**
     * Test that a 404 response is returned when a recruiter cannot be found.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testGetRecruiterByIdNotFound() throws Exception {
        when(recruiterService.getRecruiterById("nonExistentId")).thenReturn(null);

        mockMvc.perform(get("/api/recruiters/nonExistentId"))
                .andExpect(status().isNotFound());

        verify(recruiterService, times(1)).getRecruiterById("nonExistentId");
    }

    /**
     * Test retrieving a recruiter profile by user ID.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetRecruiterByUserId() throws Exception {
        when(recruiterService.getRecruiterByUserId("userId123")).thenReturn(testRecruiter);

        mockMvc.perform(get("/api/recruiters/user/userId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("recruiterId123")))
                .andExpect(jsonPath("$.companyName", is("Tech Corporation")));

        verify(recruiterService, times(1)).getRecruiterByUserId("userId123");
    }

    /**
     * Test creating a new recruiter profile.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testCreateRecruiterProfile() throws Exception {
        when(recruiterService.createRecruiterProfile(any(RecruiterDto.class))).thenReturn(testRecruiter);

        mockMvc.perform(post("/api/recruiters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRecruiterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("recruiterId123")))
                .andExpect(jsonPath("$.companyName", is("Tech Corporation")))
                .andExpect(jsonPath("$.industry", is("Technology")));

        verify(recruiterService, times(1)).createRecruiterProfile(any(RecruiterDto.class));
    }

    /**
     * Test handling of duplicate profile creation.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testCreateRecruiterProfileDuplicate() throws Exception {
        when(recruiterService.createRecruiterProfile(any(RecruiterDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/recruiters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRecruiterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already exists")));

        verify(recruiterService, times(1)).createRecruiterProfile(any(RecruiterDto.class));
    }

    /**
     * Test updating a recruiter profile.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testUpdateRecruiterProfile() throws Exception {
        when(recruiterService.updateRecruiterProfile(eq("recruiterId123"), any(RecruiterDto.class)))
                .thenReturn(testRecruiter);

        mockMvc.perform(put("/api/recruiters/recruiterId123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRecruiterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("recruiterId123")))
                .andExpect(jsonPath("$.companyName", is("Tech Corporation")));

        verify(recruiterService, times(1))
                .updateRecruiterProfile(eq("recruiterId123"), any(RecruiterDto.class));
    }

    /**
     * Test deleting a recruiter profile.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testDeleteRecruiterProfile() throws Exception {
        when(recruiterService.deleteRecruiterProfile("recruiterId123")).thenReturn(true);

        mockMvc.perform(delete("/api/recruiters/recruiterId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));

        verify(recruiterService, times(1)).deleteRecruiterProfile("recruiterId123");
    }

    /**
     * Test retrieving recruiters by company name.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testGetRecruitersByCompany() throws Exception {
        when(recruiterService.getRecruitersByCompany("Tech Corporation"))
                .thenReturn(Arrays.asList(testRecruiter));

        mockMvc.perform(get("/api/recruiters/company/Tech Corporation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("recruiterId123")))
                .andExpect(jsonPath("$[0].companyName", is("Tech Corporation")));

        verify(recruiterService, times(1)).getRecruitersByCompany("Tech Corporation");
    }

    /**
     * Test searching for recruiters by various criteria.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testSearchRecruiters() throws Exception {
        when(recruiterService.searchRecruiters(
                eq("Tech Corporation"), eq("San Francisco"), eq("Technology")))
                .thenReturn(Arrays.asList(testRecruiter));

        mockMvc.perform(get("/api/recruiters/search")
                        .param("companyName", "Tech Corporation")
                        .param("location", "San Francisco")
                        .param("industry", "Technology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("recruiterId123")))
                .andExpect(jsonPath("$[0].companyName", is("Tech Corporation")));

        verify(recruiterService, times(1))
                .searchRecruiters(eq("Tech Corporation"), eq("San Francisco"), eq("Technology"));
    }

    /**
     * Test verifying a recruiter profile by an admin.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerifyRecruiter() throws Exception {
        when(recruiterService.verifyRecruiter("recruiterId123")).thenReturn(testRecruiter);

        mockMvc.perform(put("/api/recruiters/recruiterId123/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("recruiterId123")))
                .andExpect(jsonPath("$.isVerified", is(true)));

        verify(recruiterService, times(1)).verifyRecruiter("recruiterId123");
    }

    /**
     * Test that a non-admin cannot verify a recruiter.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testVerifyRecruiterAsNonAdmin() throws Exception {
        mockMvc.perform(put("/api/recruiters/recruiterId123/verify"))
                .andExpect(status().isForbidden());

        verify(recruiterService, never()).verifyRecruiter(anyString());
    }

    /**
     * Test retrieving verified recruiters.
     */
    @Test
    @WithMockUser(roles = {"RECRUITER", "ADMIN"})
    void testGetVerifiedRecruiters() throws Exception {
        when(recruiterService.getVerifiedRecruiters())
                .thenReturn(Arrays.asList(testRecruiter));

        mockMvc.perform(get("/api/recruiters/verified"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("recruiterId123")))
                .andExpect(jsonPath("$[0].isVerified", is(true)));

        verify(recruiterService, times(1)).getVerifiedRecruiters();
    }

    /**
     * Test getting recruiter statistics.
     */
    @Test
    @WithMockUser(roles = "RECRUITER")
    void testGetRecruiterStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", 10L);
        stats.put("activeJobs", 8L);
        stats.put("recentJobs", Arrays.asList());

        when(recruiterService.getRecruiterStats("recruiterId123")).thenReturn(stats);

        mockMvc.perform(get("/api/recruiters/recruiterId123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalJobs", is(10)))
                .andExpect(jsonPath("$.activeJobs", is(8)))
                .andExpect(jsonPath("$.recentJobs", hasSize(0)));

        verify(recruiterService, times(1)).getRecruiterStats("recruiterId123");
    }
}