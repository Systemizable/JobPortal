package me.josephsf.jobportaljosephsfeirtest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for Jackson ObjectMapper in test environment.
 * <p>
 * This class registers the JavaTimeModule to properly handle Java 8 date and time types
 * like LocalDateTime, LocalDate, etc. in test context. It ensures that our controller tests
 * can properly serialize and deserialize date/time objects.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
@Configuration
public class TestConfig {

    /**
     * Creates and configures the ObjectMapper with support for Java 8 date/time types for testing.
     * <p>
     * This bean is marked as @Primary to ensure it overrides any other ObjectMapper
     * beans defined by Spring Boot's auto-configuration in the test context.
     * </p>
     *
     * @return A configured ObjectMapper instance with JavaTimeModule registered
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule for handling java.time.* types
        objectMapper.registerModule(new JavaTimeModule());

        // Configure ObjectMapper to not write dates as timestamps (numeric values)
        // but as ISO-8601 strings
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }
}