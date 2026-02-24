package org.connect.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.entity.UserAccount;
import org.connect.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Auto-creates a default ADMIN account on first startup if no admin exists.
 *
 * Credentials are configurable via environment variables:
 *   ADMIN_EMAIL    (default: admin@topconnect.com)
 *   ADMIN_PASSWORD (default: Admin@123)
 *
 * In production, always override the default password via env variable.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrap implements ApplicationRunner {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@topconnect.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@123}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        // Only create if no ADMIN user exists at all
        boolean adminExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getRole() == UserAccount.UserRole.ADMIN);

        if (!adminExists) {
            UserAccount admin = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(UserAccount.UserRole.ADMIN)
                    .status(UserAccount.UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);

            log.info("╔══════════════════════════════════════════════════════╗");
            log.info("║  DEFAULT ADMIN ACCOUNT CREATED                      ║");
            log.info("║  Email:    {}                       ║", adminEmail);
            log.info("║  Password: {}                              ║", adminPassword);
            log.info("║                                                      ║");
            log.info("║  ⚠️  CHANGE THIS PASSWORD IN PRODUCTION!             ║");
            log.info("╚══════════════════════════════════════════════════════╝");
        } else {
            log.info("Admin account already exists — skipping bootstrap.");
        }
    }
}

