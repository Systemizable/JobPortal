package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.RegisterDto;
import me.josephsf.jobportaljosephsfeir.model.Role;
import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

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

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}