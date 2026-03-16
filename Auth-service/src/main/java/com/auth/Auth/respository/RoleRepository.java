package com.auth.Auth.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.Auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.Roles role);
    
}
