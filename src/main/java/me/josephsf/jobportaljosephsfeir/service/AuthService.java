package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.RegisterDto;
import me.josephsf.jobportaljosephsfeir.model.Role;
import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class for authentication and user registration in the Job Portal system.
 *
 * <p>This service provides methods for user registration, password management, and basic
 * authentication-related functionality. It interacts with the MongoDB database through
 * the UserRepository and handles secure password storage using Spring Security's
 * PasswordEncoder.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new AuthService with required dependencies.
     *
     * @param userRepository Repository for user data operations
     * @param passwordEncoder Encoder for securely hashing passwords
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks if a username is available for registration.
     *
     * @param username The username to check
     * @return true if the username is available, false if it already exists
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email is available for registration.
     *
     * @param email The email to check
     * @return true if the email is available, false if it already exists
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Registers a new user in the system.
     * Validates that the username and email are available before registration.
     * Securely hashes the password and assigns appropriate roles.
     *
     * @param registrationRequest The data transfer object containing registration information
     * @return The newly created user account
     * @throws RuntimeException if the username or email is already taken
     */
    public User registerUser(RegisterDto registrationRequest) {
        if (!isUsernameAvailable(registrationRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (!isEmailAvailable(registrationRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword())
        );

        // Set roles
        Set<String> strRoles = registrationRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(Role.CANDIDATE);
        } else {
            for (String role : strRoles) {
                switch (role) {
                    case "admin":
                        roles.add(Role.ADMIN);
                        break;
                    case "recruiter":
                        roles.add(Role.RECRUITER);
                        break;
                    default:
                        roles.add(Role.CANDIDATE);
                }
            }
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Changes a user's password.
     * Verifies the old password before allowing the change.
     *
     * @param userId The ID of the user whose password is being changed
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return The updated user account or null if the user is not found
     * @throws RuntimeException if the old password is invalid
     */
    public User changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    /**
     * Deletes a user account.
     *
     * @param userId The ID of the user to delete
     */
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}