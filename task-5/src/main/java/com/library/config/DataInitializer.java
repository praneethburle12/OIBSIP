package com.library.config;

import com.library.model.LibrarySettings;
import com.library.model.User;
import com.library.repository.LibrarySettingsRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LibrarySettingsRepository settingsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running DataInitializer to ensure core users and settings...");

        // Ensure Settings
        if (settingsRepository.count() == 0) {
            System.out.println("Initializing default library settings...");
            LibrarySettings settings = new LibrarySettings();
            settings.setId(1L);
            settings.setAllowedDays(14);
            settings.setFinePerDay(5.0);
            settings.setMaxBooksPerUser(3);
            settingsRepository.save(settings);
        }

        // Ensure Admin
        ensureUser("admin", "admin123", "System Administrator", "admin@library.com", "ADMIN");

        // Ensure Sample User
        ensureUser("john_doe", "user123", "John Doe", "john@example.com", "USER");

        System.out.println("DataInitializer completed.");
    }

    private void ensureUser(String username, String password, String fullName, String email, String role) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            System.out.println("Updating existing user: " + username);
        } else {
            user = new User();
            user.setUsername(username);
            System.out.println("Creating new user: " + username);
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(true);

        userRepository.save(user);
    }
}
