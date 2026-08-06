package com.kbt.config;

import com.kbt.model.Role;
import com.kbt.model.User;
import com.kbt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * On every startup, makes sure the special super-admin account "K" exists.
 * "K" is not a normal signup - its password comes from configuration
 * (application.properties / environment variable), never from the public signup form.
 * Logging in as "K" returns role SUPER_ADMIN, which unlocks the admin endpoints
 * used to edit any content on the site.
 */
@Component
@RequiredArgsConstructor
public class SuperAdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${kbt.super-admin.username}")
    private String superAdminUsername;

    @Value("${kbt.super-admin.password}")
    private String superAdminPassword;

    @Value("${kbt.super-admin.email}")
    private String superAdminEmail;

    @Override
    public void run(String... args) {
        userRepository.findByUsername(superAdminUsername).ifPresentOrElse(
            existing -> {
                // Keep the password in sync with configuration in case it was rotated
                existing.setPassword(passwordEncoder.encode(superAdminPassword));
                existing.setRole(Role.SUPER_ADMIN);
                userRepository.save(existing);
            },
            () -> {
                User superAdmin = User.builder()
                        .username(superAdminUsername)
                        .email(superAdminEmail)
                        .password(passwordEncoder.encode(superAdminPassword))
                        .role(Role.SUPER_ADMIN)
                        .build();
                userRepository.save(superAdmin);
                System.out.println("Super admin '" + superAdminUsername + "' created.");
            }
        );
    }
}
