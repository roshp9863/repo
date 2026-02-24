package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.config.JwtTokenProvider;
import org.connect.dto.*;
import org.connect.entity.EmailVerificationToken;
import org.connect.entity.UserAccount;
import org.connect.entity.ArtistProfile;
import org.connect.repository.EmailVerificationTokenRepository;
import org.connect.repository.UserAccountRepository;
import org.connect.repository.ArtistProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserAccountRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;

    @Value("${app.email.dev-mode:false}")
    private boolean devMode;

    public void signup(AuthSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        UserAccount user = UserAccount.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserAccount.UserRole.valueOf(request.getRole().toUpperCase()))
                .status(UserAccount.UserStatus.PENDING)
                .build();

        user = userRepository.save(user);

        if ("ARTIST".equalsIgnoreCase(request.getRole())) {
            // Validate artist has provided at least one form of proof
            boolean hasProof = (request.getInstagramUrl() != null && !request.getInstagramUrl().isBlank())
                    || (request.getYoutubeUrl() != null && !request.getYoutubeUrl().isBlank())
                    || (request.getCertificateInfo() != null && !request.getCertificateInfo().isBlank());
            if (!hasProof) {
                throw new IllegalArgumentException(
                        "Artists must provide at least one verification proof: Instagram URL, YouTube URL, or certificate/qualification details.");
            }

            ArtistProfile profile = ArtistProfile.builder()
                    .id(UUID.randomUUID())
                    .userId(user.getId())
                    .displayName(request.getDisplayName())
                    .featured(false)
                    .verificationStatus(ArtistProfile.VerificationStatus.PENDING_VERIFICATION)
                    .instagramUrl(request.getInstagramUrl())
                    .youtubeUrl(request.getYoutubeUrl())
                    .certificateInfo(request.getCertificateInfo())
                    .build();
            artistProfileRepository.save(profile);
            log.info("Artist signup pending verification: {} ({})", request.getDisplayName(), user.getEmail());
        }

        // Generate and save verification token
        String verificationToken = UUID.randomUUID().toString();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(verificationToken)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(token);

        // In development mode, auto-verify the email immediately
        if (devMode) {
            log.info("DEV MODE: Auto-verifying email for: {}", user.getEmail());
            user.setStatus(UserAccount.UserStatus.ACTIVE);
            userRepository.save(user);
            token.setVerifiedAt(LocalDateTime.now());
            tokenRepository.save(token);
            log.info("User registered successfully (auto-verified in dev mode): {}", user.getEmail());
            return;
        }

        // In production mode, send verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send verification email to: {}. Error: {}", user.getEmail(), e.getMessage());
        }

        log.info("User registered successfully: {}", user.getEmail());
    }

    public AuthTokenResponse login(AuthLoginRequest request) {
        UserAccount user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() == UserAccount.UserStatus.PENDING) {
            throw new IllegalArgumentException("Email not verified. Please check your email.");
        }

        if (user.getStatus() == UserAccount.UserStatus.SUSPENDED) {
            throw new IllegalArgumentException("Account is suspended");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return AuthTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpirationMs / 1000)
                .user(toUserDTO(user))
                .build();
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        if (verificationToken.getVerifiedAt() != null) {
            throw new IllegalArgumentException("Email already verified");
        }

        UserAccount user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setStatus(UserAccount.UserStatus.ACTIVE);
        userRepository.save(user);

        verificationToken.setVerifiedAt(LocalDateTime.now());
        tokenRepository.save(verificationToken);

        log.info("Email verified for user: {}", user.getEmail());
    }

    public UserDTO getCurrentUser(UUID userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toUserDTO(user);
    }

    private UserDTO toUserDTO(UserAccount user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public String devVerifyAllPendingUsers() {
        if (!devMode) {
            throw new IllegalArgumentException("Dev mode is not enabled");
        }

        java.util.List<UserAccount> pendingUsers = userRepository.findAll().stream()
                .filter(u -> u.getStatus() == UserAccount.UserStatus.PENDING)
                .toList();

        for (UserAccount user : pendingUsers) {
            user.setStatus(UserAccount.UserStatus.ACTIVE);
            userRepository.save(user);

            // Mark all tokens as verified
            java.util.List<EmailVerificationToken> tokens = tokenRepository.findAll().stream()
                    .filter(t -> t.getUserId().equals(user.getId()))
                    .toList();

            for (EmailVerificationToken token : tokens) {
                if (token.getVerifiedAt() == null) {
                    token.setVerifiedAt(LocalDateTime.now());
                    tokenRepository.save(token);
                }
            }
        }

        return "Auto-verified " + pendingUsers.size() + " pending users";
    }

}
