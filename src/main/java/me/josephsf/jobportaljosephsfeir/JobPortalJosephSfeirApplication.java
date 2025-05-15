package me.josephsf.jobportaljosephsfeir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Job Portal system.
 *
 * <p>This is the entry point for the Spring Boot application that provides a
 * MongoDB-based job portal with REST API capabilities. It initializes the
 * Spring application context and starts the embedded web server.</p>
 *
 * <p>On startup, the application logs the MongoDB URI environment variable
 * to help with troubleshooting database connectivity issues.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@SpringBootApplication
public class JobPortalJosephSfeirApplication {

    /**
     * Constructs a new JobPortalJosephSfeirApplication instance.
     * <p>
     * This default constructor is used by Spring Boot to instantiate
     * the application class. It doesn't require any specific initialization.
     * </p>
     */
    public JobPortalJosephSfeirApplication() {
        // Default constructor for Spring Boot
    }

    /**
     * Main method that starts the Job Portal application.
     *
     * <p>This method initializes and runs the Spring Boot application.
     * It also logs the MongoDB URI from environment variables to aid
     * in debugging database connectivity.</p>
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        // Log the MongoDB URI at startup
        System.out.println("=== MONGODB_URI Environment Variable ===");
        System.out.println(System.getenv("MONGODB_URI"));
        System.out.println("======================================");

        SpringApplication.run(JobPortalJosephSfeirApplication.class, args);
    }
}