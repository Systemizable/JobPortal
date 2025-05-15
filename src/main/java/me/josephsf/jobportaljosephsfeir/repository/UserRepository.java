package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and manipulating User documents in MongoDB.
 * <p>
 * This repository provides methods for CRUD operations on User entities, as well as
 * custom queries for user authentication and management. It leverages Spring Data
 * MongoDB's query method naming conventions for defining custom finder methods.
 * </p>
 *
 * <p>As a Spring Data repository, it automatically provides standard operations like:</p>
 * <ul>
 *   <li>Saving and updating user accounts</li>
 *   <li>Deleting user accounts</li>
 *   <li>Finding user accounts by ID</li>
 *   <li>Retrieving all user accounts</li>
 * </ul>
 *
 * <p>The interface also defines custom queries specifically for user authentication,
 * email verification, and username verification. These methods are essential for
 * the security and user management aspects of the application.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see me.josephsf.jobportaljosephsfeir.model.User
 * @see org.springframework.data.mongodb.repository.MongoRepository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by username.
     * This method is primarily used for authentication and login.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email address.
     * This method can be used for password reset functionality or email verification.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a username already exists in the database.
     * This is used during registration to ensure username uniqueness.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if an email address already exists in the database.
     * This is used during registration to ensure email uniqueness.
     *
     * @param email the email address to check
     * @return true if the email exists, false otherwise
     */
    Boolean existsByEmail(String email);

    /**
     * Deletes a user by username.
     * This method provides an alternative way to delete users when the ID is not available.
     *
     * @param username the username of the user to delete
     */
    void deleteByUsername(String username);
}