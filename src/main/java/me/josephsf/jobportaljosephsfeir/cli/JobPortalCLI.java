package me.josephsf.jobportaljosephsfeir.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class JobPortalCLI implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private String authToken = null;
    private String currentUserRole = null;
    private String currentUserId = null;
    private String currentUsername = null;
    private final String API_BASE_URL = "https://josephsfeirjobportal.onrender.com/api";

    public JobPortalCLI() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================");
        System.out.println("Welcome to Job Portal CLI!");
        System.out.println("=================================\n");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (authToken == null) {
                // Not logged in
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
                // Logged in
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
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/auth/signin", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                authToken = (String) responseBody.get("accessToken");
                currentUserId = (String) responseBody.get("id");
                currentUsername = (String) responseBody.get("username");

                // Extract role from roles array
                Object rolesObj = responseBody.get("roles");
                if (rolesObj instanceof java.util.List) {
                    java.util.List<?> roles = (java.util.List<?>) rolesObj;
                    if (!roles.isEmpty()) {
                        currentUserRole = roles.get(0).toString().replace("ROLE_", "");
                    }
                }

                System.out.println("\n✓ Login successful!");
                System.out.println("Welcome back, " + currentUsername + "!");
            } else {
                System.out.println("\n✗ Login failed. Please check your credentials.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error during login: " + e.getMessage());
        }
    }

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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = new HashMap<>();
            request.put("username", username);
            request.put("email", email);
            request.put("password", password);
            request.put("role", java.util.Arrays.asList(role));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/auth/signup", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Registration successful! You can now login.");
            } else {
                Map<String, Object> responseBody = response.getBody();
                System.out.println("\n✗ Registration failed: " + responseBody.get("message"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error during registration: " + e.getMessage());
        }
    }
    private void showRecruiterMenu(Scanner scanner) {
        System.out.println("\n=== RECRUITER MENU ===");

        // Check if profile exists with better debugging
        String recruiterId = getRecruiterId();
        if (recruiterId == null) {
            System.out.println("\n⚠  WARNING: You need to create your recruiter profile first!");
            System.out.println("   Most features will not work until you create a profile.");
        } else {
            System.out.println("\n✓ Recruiter profile found (ID: " + recruiterId + ")");
        }

        System.out.println("\n1. Post a Job");
        System.out.println("2. View My Jobs");
        System.out.println("3. View Job Applications");
        System.out.println("4. Update Application Status");
        System.out.println("5. View Job Statistics");
        System.out.println("6. Create/Update Profile" + (recruiterId == null ? " (Required - Do this first!)" : ""));
        System.out.println("7. Logout");
        System.out.print("\nEnter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                if (recruiterId == null) {
                    System.out.println("\n⚠  You must create your profile first! (Choose option 6)");
                } else {
                    postJob(scanner);
                }
                break;
            case "2":
                if (recruiterId == null) {
                    System.out.println("\n⚠  You must create your profile first! (Choose option 6)");
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

    private void showCandidateMenu(Scanner scanner) {
        System.out.println("\n=== CANDIDATE MENU ===");
        System.out.println("1. Browse Jobs");
        System.out.println("2. Search Jobs");
        System.out.println("3. Apply to Job");
        System.out.println("4. View My Applications");
        System.out.println("5. Create/Update Profile");
        System.out.println("6. Update Resume");
        System.out.println("7. View Application Statistics");
        System.out.println("8. Logout");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                browseJobs();
                break;
            case "2":
                searchJobs(scanner);
                break;
            case "3":
                applyToJob(scanner);
                break;
            case "4":
                viewMyApplications();
                break;
            case "5":
                createOrUpdateCandidateProfile(scanner);
                break;
            case "6":
                updateResume(scanner);
                break;
            case "7":
                viewApplicationStatistics();
                break;
            case "8":
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

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

            // Add default requirements and responsibilities
            request.put("requirements", java.util.Arrays.asList("", "")); // Empty for now
            request.put("responsibilities", java.util.Arrays.asList("", "")); // Empty for now

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

            ResponseEntity<java.util.List> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs/recruiter/" + recruiterId, HttpMethod.GET, entity, java.util.List.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                java.util.List<Map<String, Object>> jobs = response.getBody();

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

    private void viewJobApplications(Scanner scanner) {
        System.out.print("\nEnter Job ID to view applications: ");
        String jobId = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/applications/job/" + jobId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                java.util.List<Map<String, Object>> applications = (java.util.List<Map<String, Object>>) responseBody.get("applications");

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

    private void viewJobStatistics(Scanner scanner) {
        System.out.print("\nEnter Job ID for statistics: ");
        String jobId = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
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
    private void createOrUpdateRecruiterProfile(Scanner scanner) {
        System.out.println("\n=== RECRUITER PROFILE ===");

        // First check if profile already exists
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

            System.out.println("\nSending request to: " + API_BASE_URL + "/recruiters");
            System.out.println("User ID: " + currentUserId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response;
            if (isUpdate) {
                System.out.println("Updating existing profile...");
                response = restTemplate.exchange(
                        API_BASE_URL + "/recruiters/" + existingRecruiterId,
                        HttpMethod.PUT,
                        entity,
                        Map.class
                );
            } else {
                System.out.println("Creating new profile...");
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
                System.out.println("  HTTP Status: " + response.getStatusCode());
                if (response.getBody() != null && response.getBody().get("message") != null) {
                    System.out.println("  Error: " + response.getBody().get("message"));
                }
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error with profile: " + e.getMessage());
            e.printStackTrace();
            if (!isUpdate) {
                System.out.println("\nTips:");
                System.out.println("- Make sure your internet connection is working");
                System.out.println("- Verify the server is running");
                System.out.println("- Try logging out and logging in again");
            }
        }
    }

    private void browseJobs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            // No auth required for browsing jobs

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs", HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                java.util.List<Map<String, Object>> jobs = (java.util.List<Map<String, Object>>) responseBody.get("jobs");

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

    private void searchJobs(Scanner scanner) {
        System.out.print("\nEnter keyword to search: ");
        String keyword = scanner.nextLine();

        try {
            HttpHeaders headers = new HttpHeaders();
            // No auth required for searching jobs

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8");
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/jobs/search?keyword=" + encodedKeyword, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                java.util.List<Map<String, Object>> jobs = (java.util.List<Map<String, Object>>) responseBody.get("jobs");

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

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_BASE_URL + "/applications/candidate/" + candidateId, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                java.util.List<Map<String, Object>> applications = (java.util.List<Map<String, Object>>) responseBody.get("applications");

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

    private void createOrUpdateCandidateProfile(Scanner scanner) {
        System.out.println("\n=== CANDIDATE PROFILE ===");
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
        System.out.print("Skills (comma separated): ");
        String skillsStr = scanner.nextLine();
        System.out.print("Resume URL: ");
        String resumeUrl = scanner.nextLine();
        System.out.print("Profile Summary: ");
        String profileSummary = scanner.nextLine();
        System.out.print("LinkedIn URL: ");
        String linkedInUrl = scanner.nextLine();
        System.out.print("Portfolio URL: ");
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
                    System.out.println("Invalid years format. Skipping...");
                }
            }

            if (!skillsStr.isEmpty()) {
                request.put("skills", java.util.Arrays.asList(skillsStr.split(",\\s*")));
            }

            request.put("resumeUrl", resumeUrl);
            request.put("profileSummary", profileSummary);
            request.put("linkedInUrl", linkedInUrl);
            request.put("portfolioUrl", portfolioUrl);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/candidates", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("\n✓ Profile created/updated successfully!");
            } else {
                System.out.println("\n✗ Failed to create/update profile. Profile might already exist.");
                System.out.println("  Try updating your profile through the update endpoint or via the web interface.");
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error with profile: " + e.getMessage());
        }
    }
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

    private void logout() {
        authToken = null;
        currentUserRole = null;
        currentUserId = null;
        currentUsername = null;
        System.out.println("\n✓ Logged out successfully!");
    }
    private String getRecruiterId() {
        if (currentUserId == null) {
            System.out.println("DEBUG: currentUserId is null");
            return null;
        }

        System.out.println("DEBUG: Looking for recruiter profile for user: " + currentUserId);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = API_BASE_URL + "/recruiters/user/" + currentUserId;
            System.out.println("DEBUG: Checking: " + url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> recruiter = response.getBody();
                String recruiterId = (String) recruiter.get("id");
                System.out.println("DEBUG: Found recruiter profile with ID: " + recruiterId);
                return recruiterId;
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("DEBUG: No recruiter profile found for user " + currentUserId);
                return null;
            }
            System.out.println("DEBUG: Error getting recruiter ID: " + e.getStatusCode() + " - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("DEBUG: Error getting recruiter ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
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
                // This is expected when profile doesn't exist
                return null;
            }
            System.out.println("Error getting candidate ID: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error getting candidate ID: " + e.getMessage());
        }
        return null;
    }
}