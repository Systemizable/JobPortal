package me.josephsf.jobportaljosephsfeir.security;

import me.josephsf.jobportaljosephsfeir.model.User;
import me.josephsf.jobportaljosephsfeir.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Spring Security's UserDetailsService for authentication.
 * <p>
 * This service provides the core user authentication functionality by loading user-specific
 * data. It implements the standard Spring Security UserDetailsService interface which is
 * required by the authentication provider. When a user attempts to authenticate, this
 * service is used to load the user's details from the database.
 * </p>
 *
 * <p>The implementation:</p>
 * <ul>
 *   <li>Retrieves a user from the database based on the provided username</li>
 *   <li>Converts the domain User object to a Spring Security UserDetails object</li>
 *   <li>Throws an exception if the user is not found</li>
 * </ul>
 *
 * <p>This service acts as a bridge between the Spring Security authentication system
 * and the application's user database, enabling username/password authentication
 * against the MongoDB user repository.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see me.josephsf.jobportaljosephsfeir.repository.UserRepository
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Repository for accessing user data from the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor with dependency injection.
     *
     * @param userRepository the repository for accessing user data
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by username.
     * <p>
     * This method is called by Spring Security during the authentication process.
     * It retrieves the user from the database and converts it to a UserDetails object
     * that Spring Security can use for authentication and authorization.
     * </p>
     *
     * @param username the username identifying the user whose data is required
     * @return a fully populated UserDetails object
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserPrincipal.build(user);
    }
}