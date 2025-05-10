package me.josephsf.jobportaljosephsfeir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobPortalJosephSfeirApplication {

    public static void main(String[] args) {
        // Log the MongoDB URI at startup
        System.out.println("=== MONGODB_URI Environment Variable ===");
        System.out.println(System.getenv("MONGODB_URI"));
        System.out.println("======================================");

        SpringApplication.run(JobPortalJosephSfeirApplication.class, args);
    }
}
