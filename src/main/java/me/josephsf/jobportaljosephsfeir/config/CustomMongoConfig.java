package me.josephsf.jobportaljosephsfeir.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.lang.NonNull;

/**
 * Custom MongoDB configuration for the Job Portal application.
 *
 * <p>This configuration class extends Spring Data's AbstractMongoClientConfiguration
 * to provide custom settings for connecting to MongoDB. It is responsible for setting
 * up the MongoDB client with the appropriate connection string from application properties
 * and configuring the database name.</p>
 *
 * <p>The class is annotated with @Primary to ensure it takes precedence over any
 * auto-configured MongoDB connections. It logs the connection details during startup
 * to aid in troubleshooting database connectivity issues.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Configuration
@Primary
public class CustomMongoConfig extends AbstractMongoClientConfiguration {

    /**
     * MongoDB connection URI from application properties.
     */
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    /**
     * Constructs a new CustomMongoConfig instance.
     * <p>
     * This default constructor initializes the configuration class.
     * The mongoUri field will be injected by Spring after construction.
     * </p>
     */
    public CustomMongoConfig() {
        // Default constructor - mongoUri will be injected by Spring
    }

    /**
     * Specifies the name of the MongoDB database to use.
     * <p>
     * This method is required by AbstractMongoClientConfiguration and
     * provides the name of the database that will be used by the application.
     * It logs the database name during initialization for debugging purposes.
     * </p>
     *
     * @return The name of the MongoDB database ("job_portal")
     */
    @Override
    @NonNull
    protected String getDatabaseName() {
        System.out.println("=== DATABASE NAME FROM CONFIG ===");
        return "job_portal";
    }

    /**
     * Creates and configures the MongoDB client instance.
     * <p>
     * This method creates a MongoClient with the connection string from application
     * properties. It logs the URI being used to help with debugging connection issues.
     * </p>
     *
     * @return A configured MongoClient instance
     */
    @Override
    @NonNull
    public MongoClient mongoClient() {
        System.out.println("=== CREATING MONGO CLIENT WITH URI ===");
        System.out.println(mongoUri);

        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(settings);
    }
}