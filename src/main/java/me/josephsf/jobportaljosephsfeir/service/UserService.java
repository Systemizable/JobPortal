package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user accounts in the Job Portal system.
 *
 * <p>This service provides methods for creating, retrieving, updating, and deleting user accounts.
 * It interacts with the MongoDB database through the UserRepository and handles basic user
 * management operations separate from authentication concerns.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with required dependencies.
     *
     * @param userRepository Repository for user data operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return List of all user accounts
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The unique identifier of the user
     * @return The user if found, null otherwise
     */
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for
     * @return The user if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address to search for
     * @return The user if found, null otherwise
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Saves a user to the database.
     * Can be used for both creating new users and updating existing ones.
     *
     * @param user The user to save
     * @return The saved user with updated metadata
     */
    public User saveUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Updates an existing user's information.
     * Does not update password - separate methods should be used for that.
     *
     * @param id The unique identifier of the user to update
     * @param userDetails The user object containing updated information
     * @return The updated user or null if the user is not found
     */
    public User updateUser(String id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            // Don't update password here - handle separately
            user.setRoles(userDetails.getRoles());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The unique identifier of the user to delete
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Checks if a username already exists in the system.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email address already exists in the system.
     *
     * @param email The email address to check
     * @return true if the email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}