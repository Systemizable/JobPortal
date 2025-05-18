package me.josephsf.jobportaljosephsfeir.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * JobPortalCLI - A command-line client for interacting with the Job Portal API.
 *
 * <p>This class implements a text-based interface for users to interact with the
 * Job Portal system. It provides functionality for both candidates (job seekers)
 * and recruiters. The client communicates with the Job Portal RESTful API to
 * perform CRUD operations on the MongoDB database.</p>
 *
 * <p>Features include:</p>
 * <ul>
 *   <li>User authentication (login/register)</li>
 *   <li>Profile management for candidates and recruiters</li>
 *   <li>Job posting and application management</li>
 *   <li>Job searching and browsing</li>
 *   <li>Application status tracking and updating</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@Component
public class JobPortalCLI implements CommandLineRunner {

    /** RestTemplate instance for making HTTP requests to the Job Portal API */
    private final RestTemplate restTemplate;

    /** ObjectMapper for JSON serialization and deserialization */
    private final ObjectMapper objectMapper;

    /** JWT authentication token for the current user session */
    private String authToken = null;

    /** Role of the current logged-in user (CANDIDATE or RECRUITER) */
    private String currentUserRole = null;

    /** MongoDB user ID of the current logged-in user */
    private String currentUserId = null;

    /** Username of the current logged-in user */
    private String currentUsername = null;

    /** Base URL for the Job Portal API endpoints */
    private final String API_BASE_URL;

    /**
     * Constructs a new JobPortalCLI instance.
     * Initializes the REST client and configures the API base URL.
     * The base URL can be overridden using the system property "api.base.url".
     */
    public JobPortalCLI() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        this.API_BASE_URL = System.getProperty("api.base.url", "http://localhost:8080/api");
    }

    /**
     * Entry point for the command-line client.
     * This method is called by Spring Boot when the application starts.
     * It displays the main menu and handles user input in a loop until exit.
     *
     * @param args Command line arguments (not used)
     * @throws Exception If an error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {

        if (isTestEnvironment()) {
            return;
        }

        System.out.println("=================================");
        System.out.println("Welcome to Job Portal CLI!");
        System.out.println("=================================\n");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (authToken == null) {

                System.out.println("\nPlease choose an option:");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        login(scanner);
                        break;
                    case "2":
                        register(scanner);
                        break;
                    case "3":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {

                System.out.println("\n=================================");
                System.out.println("Logged in as: " + currentUsername + " (" + currentUserRole + ")");
                System.out.println("=================================");

                if ("RECRUITER".equalsIgnoreCase(currentUserRole)) {
                    showRecruiterMenu(scanner);
                } else if ("CANDIDATE".equalsIgnoreCase(currentUserRole)) {
                    showCandidateMenu(scanner);
                } else {
                    System.out.println("Unknown role. Logging out...");
                    logout();
                }
            }
        }

        System.out.println("\nThank you for using Job Portal CLI!");
        scanner.close();
    }

    /**
     * Checks if the application is running in a test environment.
     *
     * @return true if running in a test environment, false otherwise
     */
    private boolean isTestEnvironment() {

        String activeProfiles = System.getProperty("spring.profiles.active", "");
        if (activeProfiles.contains("test")) {
            return true;
        }

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().contains("junit") ||
                    element.getClassName().contains("test")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Authenticates a user with the Job Portal API.
     * Prompts for username and password, then sends a login request.
     * On successful login, stores the JWT token and user information.
     *
     * @param scanner Scanner object to read user input
     */
    private void login(Scanner scanner) {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> request = new HashMap<>();
            request.put("username", username);
            request.put("password", password);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            System.out.println("Attempting to login...");
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/auth/signin", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                authToken = (String) responseBody.get("accessToken");
                currentUserId = (String) responseBody.get("id");
                currentUsername = (String) responseBody.get("username");

                List<?> roles = (List<?>) responseBody.get("roles");
                if (roles != null && !roles.isEmpty()) {
                    String roleWithPrefix = roles.get(0).toString();
                    currentUserRole = roleWithPrefix.replace("ROLE_", "");
                }

                System.out.println("\n✓ Login successful!");
                System.out.println("Welcome back, " + currentUsername + "!");

                if ("RECRUITER".equalsIgnoreCase(currentUserRole)) {
                    String recruiterId = getRecruiterId();
                    if (recruiterId == null) {
                        System.out.println("\n⚠ You don't have a recruiter profile yet.");
                        System.out.println("Would you like to create one now? (y/n): ");
                        String createNow = scanner.nextLine();
                        if (createNow.equalsIgnoreCase("y")) {
                            createOrUpdateRecruiterProfile(scanner);
                        }
                    }
                } else if ("CANDIDATE".equalsIgnoreCase(currentUserRole)) {
                    String candidateId = getCandidateId();
                    if (candidateId == null) {
                        System.out.println("\n⚠ You don't have a candidate profile yet.");
                        System.out.println("Would you like to create one now? (y/n): ");
                        String createNow = scanner.nextLine();
                        if (createNow.equalsIgnoreCase("y")) {
                            createOrUpdateCandidateProfile(scanner);
                        }
                    }
                }
            } else {
                System.out.println("\n✗ Login failed. Please check your credentials.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error during login: " + e.getMessage());
            System.out.println("Please make sure the server is running at: " + API_BASE_URL);
        }
    }

    /**
     * Registers a new user with the Job Portal API.
     * Collects user information including username, email, password, and role.
     * After successful registration, offers to log the user in automatically.
     *
     * @param scanner Scanner object to read user input
     */
    private void register(Scanner scanner) {
        System.out.println("\n=== REGISTER ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Account Type:");
        System.out.println("1. Candidate (Job Seeker)");
        System.out.println("2. Recruiter");
        System.out.print("Enter choice (1 or 2): ");
        String roleChoice = scanner.nextLine();

        String role = roleChoice.equals("2") ? "recruiter" : "candidate";

        try {

            System.out.println("Checking if username is available...");
            try {
                restTemplate.getForEntity(API_BASE_URL + "/auth/check-username/" + username, Object.class);
                System.out.println("\n✗ Username already exists. Please choose another one.");
                return;
            } catch (Exception e) {

                System.out.println("Username is available.");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("username", username);
            request.put("email", email);
            request.put("password", password);
            request.put("role", java.util.Arrays.asList(role));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            System.out.println("Creating account...");
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/auth/signup", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Registration successful!");

                System.out.println("Would you like to login now? (y/n): ");
                String loginNow = scanner.nextLine();
                if (loginNow.equalsIgnoreCase("y")) {
                    Map<String, String> loginRequest = new HashMap<>();
                    loginRequest.put("username", username);
                    loginRequest.put("password", password);

                    HttpEntity<Map<String, String>> loginEntity = new HttpEntity<>(loginRequest, headers);
                    ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                            API_BASE_URL + "/auth/signin", loginEntity, Map.class);

                    if (loginResponse.getStatusCode() == HttpStatus.OK) {
                        Map<String, Object> responseBody = loginResponse.getBody();
                        authToken = (String) responseBody.get("accessToken");
                        currentUserId = (String) responseBody.get("id");
                        currentUsername = username;

                        List<?> roles = (List<?>) responseBody.get("roles");
                        if (roles != null && !roles.isEmpty()) {
                            String roleWithPrefix = roles.get(0).toString();
                            currentUserRole = roleWithPrefix.replace("ROLE_", "");
                        }

                        System.out.println("\n✓ Login successful!");

                        System.out.println("Would you like to create your profile now? (y/n): ");
                        String createProfile = scanner.nextLine();
                        if (createProfile.equalsIgnoreCase("y")) {
                            if ("RECRUITER".equalsIgnoreCase(currentUserRole)) {
                                createOrUpdateRecruiterProfile(scanner);
                            } else if ("CANDIDATE".equalsIgnoreCase(currentUserRole)) {
                                createOrUpdateCandidateProfile(scanner);
                            }
                        }
                    }
                }
            } else {
                Map<String, Object> responseBody = response.getBody();
                System.out.println("\n✗ Registration failed: " + responseBody.get("message"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error during registration: " + e.getMessage());
            System.out.println("Please make sure the server is running at: " + API_BASE_URL);
        }
    }

    /**
     * Displays the menu for recruiter users and processes their selections.
     * Checks if the recruiter has created a profile before allowing certain actions.
     *
     * @param scanner Scanner object to read user input
     */
    private void showRecruiterMenu(Scanner scanner) {

        String recruiterId = getRecruiterId();
        boolean hasProfile = (recruiterId != null);

        System.out.println("\n=== RECRUITER MENU ===");

        if (!hasProfile) {
            System.out.println("\n⚠ WARNING: You need to create your recruiter profile first!");
        }

        System.out.println("1. Post a Job" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("2. View My Jobs" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("3. View Job Applications");
        System.out.println("4. Update Application Status");
        System.out.println("5. View Job Statistics");
        System.out.println("6. Create/Update Profile" + (!hasProfile ? " ⚠ (Required!)" : ""));
        System.out.println("7. Logout");
        System.out.print("\nEnter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 6)");
                } else {
                    postJob(scanner);
                }
                break;
            case "2":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 6)");
                } else {
                    viewMyJobs();
                }
                break;
            case "3":
                viewJobApplications(scanner);
                break;
            case "4":
                updateApplicationStatus(scanner);
                break;
            case "5":
                viewJobStatistics(scanner);
                break;
            case "6":
                createOrUpdateRecruiterProfile(scanner);
                break;
            case "7":
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Displays the menu for candidate users and processes their selections.
     * Checks if the candidate has created a profile before allowing certain actions.
     *
     * @param scanner Scanner object to read user input
     */
    private void showCandidateMenu(Scanner scanner) {

        String candidateId = getCandidateId();
        boolean hasProfile = (candidateId != null);

        System.out.println("\n=== CANDIDATE MENU ===");

        if (!hasProfile) {
            System.out.println("\n⚠ WARNING: You need to create your candidate profile first!");
        }

        System.out.println("1. Browse Jobs");
        System.out.println("2. Search Jobs");
        System.out.println("3. Apply to Job" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("4. View My Applications" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("5. Create/Update Profile" + (!hasProfile ? " ⚠ (Required!)" : ""));
        System.out.println("6. Update Resume" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("7. View Application Statistics" + (!hasProfile ? " (requires profile)" : ""));
        System.out.println("8. Logout");
        System.out.print("\nEnter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                browseJobs();
                break;
            case "2":
                searchJobs(scanner);
                break;
            case "3":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 5)");
                } else {
                    applyToJob(scanner);
                }
                break;
            case "4":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 5)");
                } else {
                    viewMyApplications();
                }
                break;
            case "5":
                createOrUpdateCandidateProfile(scanner);
                break;
            case "6":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 5)");
                } else {
                    updateResume(scanner);
                }
                break;
            case "7":
                if (!hasProfile) {
                    System.out.println("\n⚠ You must create your profile first! (Choose option 5)");
                } else {
                    viewApplicationStatistics();
                }
                break;
            case "8":
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Creates a new job posting in the MongoDB database through the API.
     * Collects all job details from user input and submits to the server.
     * Requires an existing recruiter profile.
     *
     * @param scanner Scanner object to read user input
     */
    private void postJob(Scanner scanner) {
        System.out.println("\n=== POST A JOB ===");
        System.out.print("Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Category (e.g., IT, Marketing): ");
        String category = scanner.nextLine();
        System.out.print("Employment Type (FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP): ");
        String employmentType = scanner.nextLine();
        System.out.print("Salary (optional, press enter to skip): ");
        String salaryStr = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);

            String recruiterId = getRecruiterId();
            if (recruiterId == null) {
                System.out.println("\n✗ Please create your recruiter profile first.");
                return;
            }

            Map<String, Object> request = new HashMap<>();
            request.put("title", title);
            request.put("companyName", companyName);
            request.put("location", location);
            request.put("description", description);
            request.put("category", category);
            request.put("employmentType", employmentType);
            request.put("recruiterId", recruiterId);

            if (!salaryStr.isEmpty()) {
                try {
                    request.put("salary", Double.parseDouble(salaryStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid salary format. Skipping salary field.");
                }
            }

            System.out.println("\nEnter job requirements (enter blank line to finish):");
            java.util.List<String> requirements = new java.util.ArrayList<>();
            while (true) {
                System.out.print("Requirement " + (requirements.size() + 1) + ": ");
                String req = scanner.nextLine();
                if (req.trim().isEmpty() && !requirements.isEmpty()) {
                    break;
                } else if (!req.trim().isEmpty()) {
                    requirements.add(req);
                }
            }
            request.put("requirements", requirements);

            System.out.println("\nEnter job responsibilities (enter blank line to finish):");
            java.util.List<String> responsibilities = new java.util.ArrayList<>();
            while (true) {
                System.out.print("Responsibility " + (responsibilities.size() + 1) + ": ");
                String resp = scanner.nextLine();
                if (resp.trim().isEmpty() && !responsibilities.isEmpty()) {
                    break;
                } else if (!resp.trim().isEmpty()) {
                    responsibilities.add(resp);
                }
            }
            request.put("responsibilities", responsibilities);

            System.out.println("\nPosting job...");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/jobs", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Job posted successfully!");
                Map<String, Object> job = response.getBody();
                System.out.println("Job ID: " + job.get("id"));
            } else {
                System.out.println("\n✗ Failed to post job.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error posting job: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays all jobs posted by the current recruiter.
     * Fetches job data from MongoDB through the API.
     */
    private void viewMyJobs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String recruiterId = getRecruiterId();
            if (recruiterId == null) {
                System.out.println("\n✗ Please create your recruiter profile first.");
                return;
            }

            System.out.println("\nFetching your jobs...");
            ResponseEntity<List> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs/recruiter/" + recruiterId, HttpMethod.GET, entity, List.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> jobs = response.getBody();

                if (jobs.isEmpty()) {
                    System.out.println("\nYou haven't posted any jobs yet.");
                } else {
                    System.out.println("\n=== MY JOBS ===");
                    for (Map<String, Object> job : jobs) {
                        System.out.println("\nJob ID: " + job.get("id"));
                        System.out.println("Title: " + job.get("title"));
                        System.out.println("Company: " + job.get("companyName"));
                        System.out.println("Location: " + job.get("location"));
                        System.out.println("Status: " + (Boolean.TRUE.equals(job.get("isActive")) ? "Active" : "Inactive"));
                        System.out.println("Posted: " + job.get("postedDate"));
                        System.out.println("----------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching jobs: " + e.getMessage());
        }
    }

    /**
     * Views applications for a specific job posting.
     * Prompts for a job ID and displays all applications received for that job.
     *
     * @param scanner Scanner object to read user input
     */
    private void viewJobApplications(Scanner scanner) {
        System.out.print("\nEnter Job ID to view applications: ");
        String jobId = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            System.out.println("\nFetching applications...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/applications/job/" + jobId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> applications = (List<Map<String, Object>>) responseBody.get("applications");

                if (applications.isEmpty()) {
                    System.out.println("\nNo applications for this job yet.");
                } else {
                    System.out.println("\n=== JOB APPLICATIONS ===");
                    for (Map<String, Object> app : applications) {
                        System.out.println("\nApplication ID: " + app.get("id"));
                        System.out.println("Candidate ID: " + app.get("candidateId"));
                        System.out.println("Status: " + app.get("status"));
                        System.out.println("Applied Date: " + app.get("applicationDate"));
                        System.out.println("Cover Letter: " + app.get("coverLetter"));
                        System.out.println("----------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching applications: " + e.getMessage());
        }
    }

    /**
     * Updates the status of a job application.
     * Allows recruiters to mark applications as reviewing, shortlisted, rejected, or accepted.
     * Optionally adds review notes to the application.
     *
     * @param scanner Scanner object to read user input
     */
    private void updateApplicationStatus(Scanner scanner) {
        System.out.print("\nEnter Application ID: ");
        String applicationId = scanner.nextLine();
        System.out.println("Choose status:");
        System.out.println("1. REVIEWING");
        System.out.println("2. SHORTLISTED");
        System.out.println("3. REJECTED");
        System.out.println("4. ACCEPTED");
        System.out.print("Enter choice: ");
        String statusChoice = scanner.nextLine();

        String status;
        switch (statusChoice) {
            case "1": status = "REVIEWING"; break;
            case "2": status = "SHORTLISTED"; break;
            case "3": status = "REJECTED"; break;
            case "4": status = "ACCEPTED"; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.print("Add review notes (optional): ");
        String reviewNotes = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            String url = API_BASE_URL + "/applications/" + applicationId + "/status?status=" + status;
            if (!reviewNotes.isEmpty()) {
                url += "&reviewNotes=" + java.net.URLEncoder.encode(reviewNotes, "UTF-8");
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            System.out.println("\nUpdating application status...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Application status updated successfully!");
            } else {
                System.out.println("\n✗ Failed to update application status.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error updating application: " + e.getMessage());
        }
    }

    /**
     * Views statistics for a specific job posting.
     * Shows counts of applications by status (applied, reviewing, shortlisted, etc.).
     *
     * @param scanner Scanner object to read user input
     */
    private void viewJobStatistics(Scanner scanner) {
        System.out.print("\nEnter Job ID for statistics: ");
        String jobId = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            System.out.println("\nFetching job statistics...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/applications/stats/job/" + jobId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Long> stats = response.getBody();
                System.out.println("\n=== JOB STATISTICS ===");
                System.out.println("Total Applications: " + stats.get("total"));
                System.out.println("Applied: " + stats.get("applied"));
                System.out.println("Reviewing: " + stats.get("reviewing"));
                System.out.println("Shortlisted: " + stats.get("shortlisted"));
                System.out.println("Rejected: " + stats.get("rejected"));
                System.out.println("Accepted: " + stats.get("accepted"));
                System.out.println("============================");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching statistics: " + e.getMessage());
        }
    }
    /**
     * Creates or updates a recruiter profile in the MongoDB database.
     * If a profile already exists, updates it with new information.
     * If no profile exists, creates a new one.
     *
     * @param scanner Scanner object to read user input
     */
    private void createOrUpdateRecruiterProfile(Scanner scanner) {
        System.out.println("\n=== RECRUITER PROFILE ===");

        String existingRecruiterId = getRecruiterId();
        boolean isUpdate = existingRecruiterId != null;

        if (isUpdate) {
            System.out.println("✓ Existing profile found. You can update your information below.");
        } else {
            System.out.println("No profile found. Let's create one!");
        }

        System.out.print("Company Name: ");
        String companyName = scanner.nextLine();
        System.out.print("Company Size (STARTUP, SMALL, MEDIUM, LARGE, ENTERPRISE): ");
        String companySize = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Industry: ");
        String industry = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Your Position: ");
        String position = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Company Website: ");
        String companyWebsite = scanner.nextLine();
        System.out.print("Company Description: ");
        String companyDescription = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);

            Map<String, Object> request = new HashMap<>();
            request.put("userId", currentUserId);
            request.put("companyName", companyName);
            request.put("companySize", companySize);
            request.put("location", location);
            request.put("industry", industry);
            request.put("department", department);
            request.put("position", position);
            request.put("phoneNumber", phoneNumber);
            request.put("companyWebsite", companyWebsite);
            request.put("companyDescription", companyDescription);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response;
            System.out.println("\n" + (isUpdate ? "Updating" : "Creating") + " profile...");

            if (isUpdate) {
                response = restTemplate.exchange(
                        API_BASE_URL + "/recruiters/" + existingRecruiterId,
                        HttpMethod.PUT,
                        entity,
                        Map.class
                );
            } else {
                response = restTemplate.postForEntity(
                        API_BASE_URL + "/recruiters",
                        entity,
                        Map.class
                );
            }

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Profile " + (isUpdate ? "updated" : "created") + " successfully!");
                System.out.println("  You can now post jobs and access all recruiter features.");
            } else {
                System.out.println("\n✗ Failed to " + (isUpdate ? "update" : "create") + " profile.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error with profile: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays all available jobs from the MongoDB database.
     * Jobs are fetched through the API and displayed with key details.
     */
    private void browseJobs() {
        try {
            HttpHeaders headers = new HttpHeaders();


            HttpEntity<Void> entity = new HttpEntity<>(headers);

            System.out.println("\nFetching available jobs...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs", HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> jobs = (List<Map<String, Object>>) responseBody.get("jobs");

                if (jobs.isEmpty()) {
                    System.out.println("\nNo jobs available at the moment.");
                } else {
                    System.out.println("\n=== AVAILABLE JOBS ===");
                    for (Map<String, Object> job : jobs) {
                        System.out.println("\nJob ID: " + job.get("id"));
                        System.out.println("Title: " + job.get("title"));
                        System.out.println("Company: " + job.get("companyName"));
                        System.out.println("Location: " + job.get("location"));
                        System.out.println("Category: " + job.get("category"));
                        System.out.println("Type: " + job.get("employmentType"));
                        if (job.get("salary") != null) {
                            System.out.println("Salary: $" + job.get("salary"));
                        }
                        System.out.println("----------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching jobs: " + e.getMessage());
        }
    }

    /**
     * Searches for jobs based on a keyword.
     * Sends the search query to the API which performs a MongoDB search.
     *
     * @param scanner Scanner object to read user input
     */
    private void searchJobs(Scanner scanner) {
        System.out.print("\nEnter keyword to search: ");
        String keyword = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();


            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8");

            System.out.println("\nSearching for jobs matching: " + keyword);
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs/search?keyword=" + encodedKeyword, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> jobs = (List<Map<String, Object>>) responseBody.get("jobs");

                if (jobs.isEmpty()) {
                    System.out.println("\nNo jobs found matching your search.");
                } else {
                    System.out.println("\n=== SEARCH RESULTS ===");
                    for (Map<String, Object> job : jobs) {
                        System.out.println("\nJob ID: " + job.get("id"));
                        System.out.println("Title: " + job.get("title"));
                        System.out.println("Company: " + job.get("companyName"));
                        System.out.println("Location: " + job.get("location"));
                        System.out.println("Category: " + job.get("category"));
                        System.out.println("----------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error searching jobs: " + e.getMessage());
        }
    }

    /**
     * Submits a job application to the MongoDB database through the API.
     * Collects application details including cover letter and resume URL.
     * Requires an existing candidate profile.
     *
     * @param scanner Scanner object to read user input
     */
    private void applyToJob(Scanner scanner) {
        System.out.print("\nEnter Job ID to apply: ");
        String jobId = scanner.nextLine();
        System.out.print("Cover Letter: ");
        String coverLetter = scanner.nextLine();
        System.out.print("Resume URL (optional): ");
        String resumeUrl = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);

            String candidateId = getCandidateId();
            if (candidateId == null) {
                System.out.println("\n✗ Please create your candidate profile first.");
                return;
            }

            Map<String, Object> request = new HashMap<>();
            request.put("jobId", jobId);
            request.put("candidateId", candidateId);
            request.put("coverLetter", coverLetter);
            if (!resumeUrl.isEmpty()) {
                request.put("resumeUrl", resumeUrl);
            }

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            System.out.println("\nSubmitting application...");
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/applications", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Application submitted successfully!");
            } else {
                Map<String, Object> responseBody = response.getBody();
                System.out.println("\n✗ Failed to apply: " + responseBody.get("message"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error applying to job: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays all job applications submitted by the current candidate.
     * Applications are fetched from MongoDB through the API.
     */
    private void viewMyApplications() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String candidateId = getCandidateId();
            if (candidateId == null) {
                System.out.println("\n✗ Please create your candidate profile first.");
                return;
            }

            System.out.println("\nFetching your applications...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/applications/candidate/" + candidateId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> applications = (List<Map<String, Object>>) responseBody.get("applications");

                if (applications.isEmpty()) {
                    System.out.println("\nYou haven't applied to any jobs yet.");
                } else {
                    System.out.println("\n=== MY APPLICATIONS ===");
                    for (Map<String, Object> app : applications) {
                        System.out.println("\nApplication ID: " + app.get("id"));
                        System.out.println("Job ID: " + app.get("jobId"));
                        System.out.println("Status: " + app.get("status"));
                        System.out.println("Applied Date: " + app.get("applicationDate"));
                        System.out.println("Cover Letter: " + app.get("coverLetter"));
                        if (app.get("reviewNotes") != null) {
                            System.out.println("Review Notes: " + app.get("reviewNotes"));
                        }
                        System.out.println("----------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching applications: " + e.getMessage());
        }
    }

    /**
     * Creates or updates a candidate profile in the MongoDB database.
     * If a profile already exists, updates it with new information.
     * If no profile exists, creates a new one.
     *
     * @param scanner Scanner object to read user input
     */
    private void createOrUpdateCandidateProfile(Scanner scanner) {
        System.out.println("\n=== CANDIDATE PROFILE ===");

        String existingCandidateId = getCandidateId();
        boolean isUpdate = existingCandidateId != null;

        if (isUpdate) {
            System.out.println("✓ Existing profile found. You can update your information below.");
        } else {
            System.out.println("No profile found. Let's create one!");
        }

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Current Title: ");
        String currentTitle = scanner.nextLine();
        System.out.print("Experience Level (ENTRY, JUNIOR, MID, SENIOR, EXECUTIVE): ");
        String experienceLevel = scanner.nextLine();
        System.out.print("Years of Experience: ");
        String yearsOfExperience = scanner.nextLine();

        System.out.println("\nEnter your skills (comma separated or one by one)");
        System.out.println("Examples: Java, Python, JavaScript, SQL, etc.");
        System.out.print("Skills: ");
        String skillsStr = scanner.nextLine();

        System.out.print("Resume URL: ");
        String resumeUrl = scanner.nextLine();
        System.out.print("Profile Summary (short bio): ");
        String profileSummary = scanner.nextLine();
        System.out.print("LinkedIn URL (optional): ");
        String linkedInUrl = scanner.nextLine();
        System.out.print("Portfolio URL (optional): ");
        String portfolioUrl = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);

            Map<String, Object> request = new HashMap<>();
            request.put("userId", currentUserId);
            request.put("firstName", firstName);
            request.put("lastName", lastName);
            request.put("phoneNumber", phoneNumber);
            request.put("location", location);
            request.put("currentTitle", currentTitle);
            request.put("experienceLevel", experienceLevel);

            if (!yearsOfExperience.isEmpty()) {
                try {
                    request.put("yearsOfExperience", Integer.parseInt(yearsOfExperience));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid years format. Using default value of 0.");
                    request.put("yearsOfExperience", 0);
                }
            }

            if (!skillsStr.isEmpty()) {
                List<String> skills = new java.util.ArrayList<>();

                for (String skill : skillsStr.split(",")) {
                    String trimmedSkill = skill.trim();
                    if (!trimmedSkill.isEmpty()) {
                        skills.add(trimmedSkill);
                    }
                }
                request.put("skills", skills);
            }

            request.put("resumeUrl", resumeUrl);
            request.put("profileSummary", profileSummary);
            request.put("linkedInUrl", linkedInUrl);
            request.put("portfolioUrl", portfolioUrl);

            request.put("education", new java.util.ArrayList<>());
            request.put("experience", new java.util.ArrayList<>());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response;

            System.out.println("\n" + (isUpdate ? "Updating" : "Creating") + " profile...");

            if (isUpdate) {
                response = restTemplate.exchange(
                        API_BASE_URL + "/candidates/" + existingCandidateId,
                        HttpMethod.PUT,
                        entity,
                        Map.class
                );
            } else {
                response = restTemplate.postForEntity(
                        API_BASE_URL + "/candidates",
                        entity,
                        Map.class
                );
            }

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Profile " + (isUpdate ? "updated" : "created") + " successfully!");
                System.out.println("  You can now apply to jobs and access all candidate features.");
            } else {
                System.out.println("\n✗ Failed to " + (isUpdate ? "update" : "create") + " profile.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error with profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the resume URL in the candidate's profile.
     * Sends an update request to the API which modifies the MongoDB document.
     *
     * @param scanner Scanner object to read user input
     */
    private void updateResume(Scanner scanner) {
        System.out.print("\nEnter new resume URL: ");
        String resumeUrl = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            String candidateId = getCandidateId();
            if (candidateId == null) {
                System.out.println("\n✗ Please create your candidate profile first.");
                return;
            }

            String url = API_BASE_URL + "/candidates/" + candidateId + "/resume?resumeUrl=" +
                    java.net.URLEncoder.encode(resumeUrl, "UTF-8");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            System.out.println("\nUpdating resume...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Resume updated successfully!");
            } else {
                System.out.println("\n✗ Failed to update resume.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error updating resume: " + e.getMessage());
        }
    }

    /**
     * Views application statistics for the current candidate.
     * Shows counts of applications by status and other profile metrics.
     */
    private void viewApplicationStatistics() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String candidateId = getCandidateId();
            if (candidateId == null) {
                System.out.println("\n✗ Please create your candidate profile first.");
                return;
            }

            System.out.println("\nFetching application statistics...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/candidates/" + candidateId + "/stats", HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> stats = response.getBody();
                System.out.println("\n=== APPLICATION STATISTICS ===");
                System.out.println("Total Applications: " + stats.get("totalApplications"));
                System.out.println("Applied: " + stats.get("appliedApplications"));
                System.out.println("Under Review: " + stats.get("reviewingApplications"));
                System.out.println("Shortlisted: " + stats.get("shortlistedApplications"));
                System.out.println("Accepted: " + stats.get("acceptedApplications"));
                System.out.println("Skills Count: " + stats.get("skillsCount"));
                System.out.println("Experience Count: " + stats.get("experienceCount"));
                System.out.println("Education Count: " + stats.get("educationCount"));
                System.out.println("============================");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error fetching statistics: " + e.getMessage());
        }
    }

    /**
     * Logs out the current user by clearing authentication data.
     * Removes the JWT token and user information from memory.
     */
    private void logout() {
        authToken = null;
        currentUserRole = null;
        currentUserId = null;
        currentUsername = null;
        System.out.println("\n✓ Logged out successfully!");
    }

    /**
     * Retrieves the recruiter profile ID for the current user.
     * Makes an API request to find the recruiter profile associated with the user ID.
     *
     * @return The MongoDB recruiter document ID if found, null otherwise
     */
    private String getRecruiterId() {
        if (currentUserId == null) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = API_BASE_URL + "/recruiters/user/" + currentUserId;

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> recruiter = response.getBody();
                return (String) recruiter.get("id");
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {

                return null;
            }

            System.out.println("Error checking recruiter profile: " + e.getMessage());
        } catch (Exception e) {

            System.out.println("Error checking recruiter profile: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the candidate profile ID for the current user.
     * Makes an API request to find the candidate profile associated with the user ID.
     *
     * @return The MongoDB candidate document ID if found, null otherwise
     */
    private String getCandidateId() {
        if (currentUserId == null) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/candidates/user/" + currentUserId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> candidate = response.getBody();
                return (String) candidate.get("id");
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {

                return null;
            }
            System.out.println("Error checking candidate profile: " + e.getMessage());
        } catch (Exception e) {

            System.out.println("Error checking candidate profile: " + e.getMessage());
        }
        return null;
    }
}