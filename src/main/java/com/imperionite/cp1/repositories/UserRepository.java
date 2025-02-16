// For persisting and accessing data
package com.imperionite.cp1.repositories;

import com.imperionite.cp1.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Find user by username
    boolean existsByUsername(String username);

}