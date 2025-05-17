/**
 * MongoDB configuration class for testing environments.
 * <p>
 * This configuration class provides a test-specific MongoDB setup that uses an embedded
 * or local MongoDB instance for integration tests. It is activated only when the "test"
 * profile is active, ensuring it doesn't interfere with production configurations.
 * </p>
 * <p>
 * The configuration:
 * </p>
 * <ul>
 *   <li>Creates an isolated test database to prevent affecting production data</li>
 *   <li>Configures MongoDB clients and templates specifically for testing</li>
 *   <li>Enables MongoDB repositories for automatic interface implementation</li>
 *   <li>Defines mapping base packages for entity scanning</li>
 * </ul>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
package me.josephsf.jobportaljosephsfeirtest.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@Profile("test")
@EnableMongoRepositories(basePackages = "me.josephsf.jobportaljosephsfeir.repository")
public class TestMongoConfig extends AbstractMongoClientConfiguration {

    private static final String DATABASE_NAME = "test_job_portal";

    /**
     * Specifies the name of the MongoDB database to use for tests.
     * <p>
     * This method defines a separate database name for the testing environment
     * to ensure that tests don't interfere with development or production data.
     * </p>
     *
     * @return the name of the test database
     */
    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    /**
     * Creates and configures a MongoDB client for testing.
     * <p>
     * This method sets up a MongoDB client that connects to a local MongoDB instance
     * running on the default port. For integration tests, this is typically connected
     * to an embedded MongoDB instance provided by libraries like Flapdoodle.
     * </p>
     *
     * @return a configured MongoDB client for testing
     */
    @Override
    @Bean
    @Primary
    public MongoClient mongoClient() {
        // Use localhost with default MongoDB port for testing with embedded MongoDB
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + DATABASE_NAME);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    /**
     * Creates a MongoDB template for testing operations.
     * <p>
     * This template is configured to use the test database and client, providing
     * a convenient way to perform MongoDB operations during tests.
     * </p>
     *
     * @return a MongoTemplate configured for the test environment
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    /**
     * Defines the base packages to scan for MongoDB entity mappings.
     * <p>
     * This method specifies which package contains the model classes that
     * should be mapped to MongoDB collections.
     * </p>
     *
     * @return a collection of package names to scan for entity mappings
     */
    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("me.josephsf.jobportaljosephsfeir.model");
    }
}