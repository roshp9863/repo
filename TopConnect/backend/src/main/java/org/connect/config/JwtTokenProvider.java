package org.connect.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration:2592000000}")
    private long refreshTokenExpirationMs;

    // Lazily initialized secret key
    private SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            try {
                // Decode Base64-encoded secret
                byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
                // Ensure key is at least 512 bits (64 bytes) for HS512
                if (decodedKey.length < 64) {
                    throw new IllegalArgumentException(
                            "JWT secret key must be at least 512 bits (64 bytes) for HS512 algorithm. " +
                            "Current key size: " + (decodedKey.length * 8) + " bits"
                    );
                }
                secretKey = Keys.hmacShaKeyFor(decodedKey);
                log.info("JWT Secret Key initialized successfully. Key size: {} bits", decodedKey.length * 8);
            } catch (IllegalArgumentException e) {
                log.error("Invalid Base64 JWT secret or insufficient key size: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize JWT secret key", e);
            }
        }
        return secretKey;
    }

    public String generateAccessToken(UUID userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);
        return createToken(claims, userId.toString(), jwtExpirationMs);
    }

    public String generateRefreshToken(UUID userId) {
        return createToken(new HashMap<>(), userId.toString(), refreshTokenExpirationMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return UUID.fromString(claims.getSubject());
    }

    public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.debug("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

