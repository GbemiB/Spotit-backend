package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.entity.RefreshToken;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.security.JwtService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.config.SpotItProperties;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthWriteServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock OtpService otpService;
    @Mock SpotItProperties properties;

    AuthWriteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthWriteServiceImpl(userRepository, refreshTokenRepository, passwordEncoder, jwtService, otpService, properties);
    }

    private User existingUser(UUID id) {
        return User.builder().id(id).firstName("Jane").lastName("Doe")
                .email("jane@example.com").passwordHash("hashed").onboarded(false).premium(false).build();
    }

    // --- signup ---

    @Test
    void signupRejectsAnAlreadyRegisteredEmail() {
        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(true);
        SignupRequest request = new SignupRequest("Jane", "Doe", "jane@example.com", "password123");

        assertThatThrownBy(() -> service.signup(request))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_REGISTERED);
        verify(userRepository, never()).save(any());
    }

    @Test
    void signupCreatesAnUnverifiedUserAndIssuesAnOtp() {
        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(false);
        when(properties.cycle()).thenReturn(new SpotItProperties.Cycle(28, 5));
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });
        OtpCode otp = OtpCode.builder().id(UUID.randomUUID()).build();
        when(otpService.issue(any(User.class), eq(OtpPurpose.signup))).thenReturn(otp);
        when(otpService.ttlSeconds()).thenReturn(600L);

        SignupRequest request = new SignupRequest("Jane", "Doe", "jane@example.com", "password123");
        SignupResponse response = service.signup(request);

        assertThat(response.email()).isEqualTo("jane@example.com");
        assertThat(response.otpRequired()).isTrue();
        assertThat(response.otpId()).isEqualTo(otp.getId());
        verify(userRepository).save(argThat(u ->
                !u.isEmailVerified() && u.getCycleLength() == 28 && u.getPeriodLength() == 5 && u.getPoints() == 0));
    }

    // --- login ---

    @Test
    void loginRejectsAnUnknownEmail() {
        when(userRepository.findByEmailIgnoreCase("nobody@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(new LoginRequest("nobody@example.com", "whatever")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    }

    @Test
    void loginRejectsAWrongPassword() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(existingUser(id)));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.login(new LoginRequest("jane@example.com", "wrong")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    }

    @Test
    void loginWithCorrectCredentialsIssuesTokens() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(existingUser(id)));
        when(passwordEncoder.matches("correct", "hashed")).thenReturn(true);
        stubTokenIssuance(id);

        TokenResponse response = service.login(new LoginRequest("jane@example.com", "correct"));

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.user().userId()).isEqualTo(id);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    // --- refresh ---

    @Test
    void refreshRejectsATokenThatDoesNotParseAsARefreshToken() {
        when(jwtService.parseRefreshToken("garbage")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refresh(new RefreshTokenRequest("garbage")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Test
    void refreshRejectsATokenNotFoundInTheStore() {
        UUID userId = UUID.randomUUID();
        when(jwtService.parseRefreshToken("token")).thenReturn(Optional.of(userId));
        when(refreshTokenRepository.findByTokenHashAndRevokedFalse(TokenHasher.sha256Hex("token")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refresh(new RefreshTokenRequest("token")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Test
    void refreshRejectsAnExpiredStoredToken() {
        UUID userId = UUID.randomUUID();
        when(jwtService.parseRefreshToken("token")).thenReturn(Optional.of(userId));
        RefreshToken stored = RefreshToken.builder().userId(userId).expiresAt(Instant.now().minusSeconds(10)).build();
        when(refreshTokenRepository.findByTokenHashAndRevokedFalse(TokenHasher.sha256Hex("token")))
                .thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.refresh(new RefreshTokenRequest("token")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Test
    void refreshWithAValidStoredTokenIssuesANewAccessToken() {
        UUID userId = UUID.randomUUID();
        when(jwtService.parseRefreshToken("token")).thenReturn(Optional.of(userId));
        RefreshToken stored = RefreshToken.builder().userId(userId).expiresAt(Instant.now().plusSeconds(1000)).build();
        when(refreshTokenRepository.findByTokenHashAndRevokedFalse(TokenHasher.sha256Hex("token")))
                .thenReturn(Optional.of(stored));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser(userId)));
        when(jwtService.generateAccessToken(userId, "jane@example.com", false)).thenReturn("new-access-token");
        when(jwtService.accessTokenTtlSeconds()).thenReturn(3600L);

        AccessTokenResponse response = service.refresh(new RefreshTokenRequest("token"));

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.expiresIn()).isEqualTo(3600L);
    }

    // --- logout / account deletion ---

    @Test
    void logoutRevokesAllRefreshTokensForTheUser() {
        UUID userId = UUID.randomUUID();

        service.logout(userId);

        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    @Test
    void schedulingAccountDeletionSetsAFutureGracePeriodAndRevokesTokens() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser(userId)));

        AccountDeletionResponse response = service.scheduleAccountDeletion(userId);

        assertThat(response.purgeBy()).isAfter(Instant.now().plus(java.time.Duration.ofDays(29)));
        verify(userRepository).save(argThat(u -> u.getPendingDeletionAt() != null));
        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    private void stubTokenIssuance(UUID userId) {
        when(jwtService.generateAccessToken(eq(userId), any(), anyBoolean())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userId)).thenReturn("refresh-token");
        when(jwtService.accessTokenTtlSeconds()).thenReturn(3600L);
        when(properties.jwt()).thenReturn(new SpotItProperties.Jwt("secret", 3600, 2_592_000));
    }
}
