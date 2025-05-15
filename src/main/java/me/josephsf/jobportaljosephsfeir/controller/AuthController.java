package me.josephsf.jobportaljosephsfeir.controller;

import me.josephsf.jobportaljosephsfeir.dto.LoginDto;
import me.josephsf.jobportaljosephsfeir.dto.RegisterDto;
import me.josephsf.jobportaljosephsfeir.dto.JwtResponseDto;
import me.josephsf.jobportaljosephsfeir.dto.ApiResponseDto;
import me.josephsf.jobportaljosephsfeir.model.Role;
import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import me.josephsf.jobportaljosephsfeir.security.JwtUtils;
import me.josephsf.jobportaljosephsfeir.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for handling authentication operations in the Job Portal system.
 * <p>
 * This controller provides endpoints for user authentication operations including
 * user registration (signup) and login (signin). It manages JWT token generation
 * for authenticated users and handles user role assignments during registration.
 * The endpoints in this controller are publicly accessible without authentication.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    /**
     * Constructs an AuthController with the specified dependencies.
     *
     * @param authenticationManager The manager for authenticating user credentials
     * @param userRepository The repository for user data operations
     * @param encoder The password encoder for securely hashing passwords
     * @param jwtUtils Utility for JWT token generation and validation
     */
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Authenticates a user and generates a JWT token.
     * <p>
     * This endpoint processes login requests by validating the provided credentials
     * against the stored user data. Upon successful authentication, it generates a
     * JWT token that can be used for subsequent authorized API requests. The response
     * includes the token, user ID, username, email, and assigned roles.
     * </p>
     *
     * @param loginRequest The DTO containing login credentials (username and password)
     * @return ResponseEntity with JWT token and user information on success
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDto(jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                roles));
    }

    /**
     * Registers a new user in the system.
     * <p>
     * This endpoint handles user registration by validating the request data,
     * checking for existing username or email conflicts, and creating a new user
     * account with the specified roles. If no role is specified, the user is
     * assigned the default CANDIDATE role. The password is securely encoded
     * before storage.
     * </p>
     *
     * @param signUpRequest The DTO containing registration details (username, email, password, roles)
     * @return ResponseEntity with success message or error details
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(false, "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(false, "Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(Role.CANDIDATE);
        } else {
            strRoles.forEach(role -> {
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
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponseDto(true, "User registered successfully!"));
    }
}