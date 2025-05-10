package me.josephsf.jobportaljosephsfeir.repository;

import me.josephsf.jobportaljosephsfeir.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Find user by username (for authentication)
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists
    Boolean existsByUsername(String username);

    // Check if email exists
    Boolean existsByEmail(String email);

    // Delete user by username
    void deleteByUsername(String username);
}