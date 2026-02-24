package org.connect.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IP-based rate limiter using Caffeine cache.
 * - Auth endpoints (/api/v1/auth/**): strict limit per IP per minute
 * - All other endpoints: global limit per IP per minute
 * - Localhost/loopback is excluded in dev mode (for load testing)
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);
    private static final Set<String> LOOPBACK = Set.of("127.0.0.1", "0:0:0:0:0:0:0:1", "::1");

    @Value("${app.rate-limit.auth:60}")
    private int authLimit;

    @Value("${app.rate-limit.global:600}")
    private int globalLimit;

    @Value("${app.rate-limit.skip-localhost:true}")
    private boolean skipLocalhost;

    private final Cache<String, AtomicInteger> authBucket = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private final Cache<String, AtomicInteger> globalBucket = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(50_000)
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = getClientIp(request);

        // Skip rate limiting for localhost in dev/local mode
        if (skipLocalhost && LOOPBACK.contains(clientIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();

        // Rate-limit auth endpoints more aggressively
        if (uri.startsWith("/api/v1/auth/")) {
            AtomicInteger counter = authBucket.get(clientIp, k -> new AtomicInteger(0));
            if (counter.incrementAndGet() > authLimit) {
                log.warn("Rate limit exceeded for auth endpoint. IP={}, URI={}", clientIp, uri);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
                return;
            }
        }

        // Global rate limit
        AtomicInteger globalCounter = globalBucket.get(clientIp, k -> new AtomicInteger(0));
        if (globalCounter.incrementAndGet() > globalLimit) {
            log.warn("Global rate limit exceeded. IP={}, URI={}", clientIp, uri);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

