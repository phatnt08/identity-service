package com.ntp.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ntp.identity_service.entity.User;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository // Indicates that this interface is a repository component in the Spring application context
public interface IUserRepository extends JpaRepository<User, String> {

    /**
     * Finds a User entity by its username.
     * 
     * @param username the username of the user
     * @return an Optional containing the User entity if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a User entity exists by its username.
     * 
     * @param username the username to check
     * @return true if a User entity with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
