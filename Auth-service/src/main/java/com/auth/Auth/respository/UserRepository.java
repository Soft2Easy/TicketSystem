package com.auth.Auth.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.Auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
}
