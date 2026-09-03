package com.Zest.product_assesment.Config;

import com.Zest.product_assesment.Entity.Role; // Make sure you have this Enum from earlier
import com.Zest.product_assesment.Entity.User;
import com.Zest.product_assesment.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("No users found in database. Creating default Admin user...");

            User defaultAdmin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(defaultAdmin);
            log.info("Default Admin created -> Username: admin | Password: admin123");
        } else {
            log.info("Database already contains users. Skipping initialization.");
        }
    }
}