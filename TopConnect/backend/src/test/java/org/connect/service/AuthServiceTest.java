package org.connect.service;

import org.connect.config.JwtTokenProvider;
import org.connect.dto.AuthLoginRequest;
import org.connect.dto.AuthSignupRequest;
import org.connect.dto.AuthTokenResponse;
import org.connect.entity.UserAccount;
import org.connect.repository.EmailVerificationTokenRepository;
import org.connect.repository.UserAccountRepository;
import org.connect.repository.ArtistProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserAccountRepository userRepository;

    @Mock
    private ArtistProfileRepository artistProfileRepository;

    @Mock
    private EmailVerificationTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, artistProfileRepository, tokenRepository,
                jwtTokenProvider, passwordEncoder, emailService);
    }

    @Test
    void testSignupSuccess() {
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .role("USER")
                .build();

        UserAccount savedUser = UserAccount.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(UserAccount.UserRole.USER)
                .status(UserAccount.UserStatus.PENDING)
                .build();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(UserAccount.class))).thenReturn(savedUser);
        when(tokenRepository.save(any())).thenReturn(null);

        authService.signup(request);

        verify(userRepository).save(any(UserAccount.class));
        verify(emailService).sendVerificationEmail(eq("test@example.com"), any());
    }

    @Test
    void testSignupWithDuplicateEmail() {
        AuthSignupRequest request = AuthSignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .role("USER")
                .build();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
    }

    @Test
    void testLoginSuccess() {
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = passwordEncoder.encode(password);
        UUID userId = UUID.randomUUID();

        AuthLoginRequest request = AuthLoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        UserAccount user = UserAccount.builder()
                .id(userId)
                .email(email)
                .passwordHash(hashedPassword)
                .role(UserAccount.UserRole.USER)
                .status(UserAccount.UserStatus.ACTIVE)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(userId, email, "USER"))
                .thenReturn("mock-access-token");
        when(jwtTokenProvider.generateRefreshToken(userId))
                .thenReturn("mock-refresh-token");

        AuthTokenResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("mock-refresh-token", response.getRefreshToken());
        assertEquals(email, response.getUser().getEmail());
    }

    @Test
    void testLoginWithInvalidPassword() {
        String email = "test@example.com";
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";

        AuthLoginRequest request = AuthLoginRequest.builder()
                .email(email)
                .password(wrongPassword)
                .build();

        UserAccount user = UserAccount.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(correctPassword))
                .role(UserAccount.UserRole.USER)
                .status(UserAccount.UserStatus.ACTIVE)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

}

