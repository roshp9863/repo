package com.project.socialmedia.controller;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final CqlSession session;

    HealthController(CqlSession session) {
        this.session = session;
    }

    @GetMapping("/health")
    public String health() {
        var row = session.execute(
                "SELECT release_version FROM system.local").one();
        return "App up. Cassandra: " + row.getString("release_version");
    }
}