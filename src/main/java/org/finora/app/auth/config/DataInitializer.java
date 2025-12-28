package org.finora.app.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.finora.app.auth.entity.Role;
import org.finora.app.auth.entity.User;
import org.finora.app.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Starting data seeding...");
            createAndSaveUser("superadmin@finora.org", "superadmin123", "Super", "Admin", Role.SUPER_ADMIN);
            createAndSaveUser("admin@finora.org", "admin123", "Admin", "User", Role.ADMIN);
            createAndSaveUser("employee@finora.org", "employee123", "Employee", "User", Role.EMPLOYEE);
            createAndSaveUser("user@finora.org", "user123", "User", "User", Role.USER);
            // Seed a user compatible with Postman Login request
            createAndSaveUser("testuser@finora.org", "password123", "Test", "User", Role.USER);
            log.info("Data seeding completed.");
        }
    }

    private void createAndSaveUser(String email, String password, String firstName, String lastName, Role role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
        userRepository.save(user);
        log.info("Seeded user: {}", email);
    }
}
