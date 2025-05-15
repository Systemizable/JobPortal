package me.josephsf.jobportaljosephsfeir.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.josephsf.jobportaljosephsfeir.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * <p>
 * This class serves as an adapter between the application's User domain model and
 * Spring Security's UserDetails interface. It wraps a User entity and provides the
 * necessary methods required by Spring Security for authentication and authorization,
 * such as credentials, authorities (roles), and account status flags.
 * </p>
 *
 * <p>UserPrincipal contains:</p>
 * <ul>
 *   <li>User identification information (ID, username, email)</li>
 *   <li>Authentication credentials (password)</li>
 *   <li>Granted authorities (roles converted to Spring Security's GrantedAuthority)</li>
 *   <li>Account status flags (account expiration, lock status, credentials expiration, enabled status)</li>
 * </ul>
 *
 * <p>This class is used by the authentication system to represent the currently
 * authenticated user and to check permissions for secured operations.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see me.josephsf.jobportaljosephsfeir.model.User
 */
public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * The user's unique identifier.
     */
    private String id;

    /**
     * The user's username for authentication.
     */
    private String username;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's password for authentication.
     * Marked with @JsonIgnore to prevent it from being serialized.
     */
    @JsonIgnore
    private String password;

    /**
     * Collection of authorities (roles) granted to the user.
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a UserPrincipal with the specified details.
     *
     * @param id the user's unique identifier
     * @param username the user's username
     * @param email the user's email
     * @param password the user's encoded password
     * @param authorities the authorities granted to the user
     */
    public UserPrincipal(String id, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Creates a UserPrincipal from a User entity.
     * <p>
     * This factory method converts the application's User model to a Spring Security
     * UserDetails implementation. It transforms the User's roles into GrantedAuthority
     * objects with the "ROLE_" prefix required by Spring Security.
     * </p>
     *
     * @param user the User entity to convert
     * @return a UserPrincipal representing the specified user
     */
    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Returns the authorities granted to the user.
     * <p>
     * These authorities (roles) are used by Spring Security for authorization
     * checks, such as method security or URL-based security.
     * </p>
     *
     * @return the authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user's email address.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's username.
     *
     * @return the user's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired.
     * <p>
     * An expired account cannot be authenticated.
     * </p>
     *
     * @return true if the account is not expired (always true in this implementation)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * <p>
     * A locked account cannot be authenticated.
     * </p>
     *
     * @return true if the account is not locked (always true in this implementation)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * <p>
     * Expired credentials prevent authentication.
     * </p>
     *
     * @return true if the credentials are not expired (always true in this implementation)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     * <p>
     * A disabled user cannot be authenticated.
     * </p>
     *
     * @return true if the user is enabled (always true in this implementation)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}