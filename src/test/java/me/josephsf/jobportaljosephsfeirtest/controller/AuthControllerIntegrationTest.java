/**
 * Integration tests for AuthController.
 * <p>
 * This test class validates the authentication functionality of the Job Portal system,
 * including user registration and login. It uses Spring Boot's MockMvc to simulate
 * HTTP requests and verify responses, connecting to a test MongoDB instance to ensure
 * proper database interactions.
 * </p>
 * <p>
 * The tests cover:
 * </p>
 * <ul>
 *   <li>User registration with validation of successful database persistence</li>
 *   <li>User authentication and JWT token generation</li>
 *   <li>Duplicate username handling during registration</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
package me.josephsf.jobportaljosephsfeirtest.controller;

import me.josephsf.jobportaljosephsfeir.dto.LoginDto;
import me.josephsf.jobportaljosephsfeir.dto.RegisterDto;
import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb+srv://JosephSfeir:Sfeir7705@job-portal-cluster.uihqk1x.mongodb.net/test_db?retryWrites=true&w=majority"
})
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";

    /**
     * Set up test environment before each test.
     * <p>
     * This method ensures that the test user doesn't exist in the database
     * before running each test to avoid conflicts with previous test runs.
     * </p>
     */
    @BeforeEach
    public void setup() {
        // Make sure the test user doesn't exist
        userRepository.deleteByUsername(TEST_USERNAME);
    }

    /**
     * Clean up test environment after each test.
     * <p>
     * This method removes any test users created during test execution
     * to leave the database in a clean state for subsequent tests.
     * </p>
     */
    @AfterEach
    public void cleanup() {
        // Clean up after tests
        userRepository.deleteByUsername(TEST_USERNAME);
    }

    /**
     * Tests user registration functionality.
     * <p>
     * This test verifies that:
     * </p>
     * <ul>
     *   <li>Users can successfully register with valid credentials</li>
     *   <li>The system returns a success response</li>
     *   <li>The user record is properly persisted in the database</li>
     * </ul>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testRegisterUser() throws Exception {
        // Create registration request
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(TEST_USERNAME);
        registerDto.setEmail(TEST_EMAIL);
        registerDto.setPassword(TEST_PASSWORD);
        registerDto.setRole(new HashSet<>(Collections.singletonList("candidate")));

        // Perform registration request
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Verify user was created in database
        User user = userRepository.findByUsername(TEST_USERNAME).orElse(null);
        assertNotNull(user, "User should be created in the database");
        assertEquals(TEST_EMAIL, user.getEmail(), "Email should match");
    }

    /**
     * Tests user login functionality.
     * <p>
     * This test verifies that:
     * </p>
     * <ul>
     *   <li>Users can log in with valid credentials</li>
     *   <li>The system returns a JWT token upon successful authentication</li>
     *   <li>User details are correctly included in the response</li>
     * </ul>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testLoginUser() throws Exception {
        // First register a user
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(TEST_USERNAME);
        registerDto.setEmail(TEST_EMAIL);
        registerDto.setPassword(TEST_PASSWORD);
        registerDto.setRole(new HashSet<>(Collections.singletonList("candidate")));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Create login request
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(TEST_USERNAME);
        loginDto.setPassword(TEST_PASSWORD);

        // Perform login request
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andReturn();

        // Verify JWT token is returned
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("accessToken"), "Response should contain JWT token");
    }

    /**
     * Tests handling of duplicate username during registration.
     * <p>
     * This test verifies that:
     * </p>
     * <ul>
     *   <li>The system prevents users from registering with an already taken username</li>
     *   <li>The system returns an appropriate error response</li>
     * </ul>
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testRegisterDuplicateUsername() throws Exception {
        // First register a user
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(TEST_USERNAME);
        registerDto.setEmail(TEST_EMAIL);
        registerDto.setPassword(TEST_PASSWORD);
        registerDto.setRole(new HashSet<>(Collections.singletonList("candidate")));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Try to register with the same username
        RegisterDto duplicateDto = new RegisterDto();
        duplicateDto.setUsername(TEST_USERNAME);
        duplicateDto.setEmail("another@example.com");
        duplicateDto.setPassword(TEST_PASSWORD);
        duplicateDto.setRole(new HashSet<>(Collections.singletonList("candidate")));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }
}