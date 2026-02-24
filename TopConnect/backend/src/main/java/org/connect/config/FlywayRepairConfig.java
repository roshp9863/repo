package org.connect.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration that auto-repairs failed migrations before running migrate.
 * This handles the case where a migration previously failed with a syntax error
 * and has been fixed - Flyway needs to repair its history first.
 */
@Configuration
public class FlywayRepairConfig {

    private static final Logger log = LoggerFactory.getLogger(FlywayRepairConfig.class);

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            log.info("Running Flyway repair before migration to clean up any failed entries...");
            flyway.repair();
            log.info("Flyway repair complete. Running migrations...");
            flyway.migrate();
            log.info("Flyway migrations complete.");
        };
    }
}

