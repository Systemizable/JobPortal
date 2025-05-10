package me.josephsf.jobportaljosephsfeir.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @EventListener(ApplicationReadyEvent.class)
    public void logMongoUri() {
        System.out.println("=== ACTUAL MONGODB URI BEING USED ===");
        System.out.println(mongoUri);
        System.out.println("====================================");
    }
}