package com.kbt.controller;

import com.kbt.dto.AuthResponse;
import com.kbt.dto.LoginRequest;
import com.kbt.dto.SignupRequest;
import com.kbt.model.Role;
import com.kbt.model.User;
import com.kbt.repository.UserRepository;
import com.kbt.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {

        if ("K".equalsIgnoreCase(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    AuthResponse.builder().message("That username is reserved.").build());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    AuthResponse.builder().message("Username already taken.").build());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    AuthResponse.builder().message("An account with this email already exists.").build());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .message("Account created successfully.")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        // "emailId" accepts either a normal email OR the reserved username "K"
        Optional<User> userOpt = "K".equalsIgnoreCase(request.getEmailId())
                ? userRepository.findByUsername("K")
                : userRepository.findByEmail(request.getEmailId());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthResponse.builder().message("Invalid credentials.").build());
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .message(user.getRole() == Role.SUPER_ADMIN
                        ? "Welcome back, K. Full control unlocked."
                        : "Login successful.")
                .build());
    }
}
