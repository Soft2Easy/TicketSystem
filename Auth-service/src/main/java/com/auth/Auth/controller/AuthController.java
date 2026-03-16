package com.auth.Auth.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.Auth.config.JwtUtil;
import com.auth.Auth.model.RegisterRequest;
import com.auth.Auth.model.Role;
import com.auth.Auth.model.User;
import com.auth.Auth.respository.RoleRepository;
import com.auth.Auth.respository.UserRepository;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles user registration by validating input, checking for existing usernames,
     * encoding the password, assigning roles, and saving the new user to the database.
     * @param request The registration request containing username, password, and optional role.
     * @return A response entity indicating the result of the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role.Roles assignedRole;
        if (request.getRole() == null || request.getRole().isBlank()) {
            assignedRole = Role.Roles.USER; // default role
        } else {
            try {
                assignedRole = Role.Roles.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body("Invalid role: " + request.getRole() + ". Valid roles: USER, ADMIN, ORGANIZER");
            }
        }

        Role roleEntity = roleRepository.findByRole(assignedRole)
                .orElseGet(() -> roleRepository.save(new Role(assignedRole)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(roleEntity));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully as " + assignedRole);
    }

    /**
     * Handles user login by validating credentials and generating a JWT token
     * upon successful authentication.
     * @param user The login request containing username and password.
     * @return A JWT token if authentication is successful, or an error message if it fails
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRoles());
        return token;
    }
}
