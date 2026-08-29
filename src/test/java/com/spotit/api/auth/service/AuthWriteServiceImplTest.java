package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.entity.RefreshToken;
import com.spotit.api.auth.entity.SignupLead;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.auth.repository.SignupLeadRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.security.JwtService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.settings.service.AppSettingsService;
import com.spotit.api.settings.service.ResolvedAppSettings;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
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
    @Mock SignupLeadRepository signupLeadRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock OtpService otpService;
    @Mock EmailService emailService;
    @Mock AppSettingsService appSettingsService;
    @Mock Environment environment;

    AuthWriteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthWriteServiceImpl(userRepository, refreshTokenRepository, signupLeadRepository,
                passwordEncoder, jwtService, otpService, emailService, appSettingsService, environment);
    }

    private static ResolvedAppSettings settingsWithCycleDefaults(int cycleLength, int periodLength) {
        return new ResolvedAppSettings("test-jwt-secret", 3600, 2_592_000, 600, 5, cycleLength, periodLength, 50, 100);
    }

    private User existingUser(UUID id) {
        return User.builder().id(id).firstName("Jane").lastName("Doe")
                .email("jane@example.com").passwordHash("hashed").emailVerified(true).onboarded(false).premium(false).build();
    }

    private SignupLead existingLead(UUID id, boolean otpVerified) {
        return SignupLead.builder().id(id).firstName("Jane").lastName("Doe").email("jane@example.com")
                .otpCodeHash("hashed-code").otpExpiresAt(Instant.now().plusSeconds(300)).otpVerified(otpVerified).build();
    }

    // --- signup (step 1: creates/refreshes a lead, no account yet) ---

    @Test
    void signupRejectsAnAlreadyRegisteredEmail() {
        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(true);
        SignupRequest request = new SignupRequest("Jane", "Doe", "jane@example.com");

        assertThatThrownBy(() -> service.signup(request))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_REGISTERED);
        verify(userRepository, never()).save(any());
        verify(signupLeadRepository, never()).save(any());
    }

    @Test
    void signupCreatesAnUnverifiedLeadAndIssuesAnOtp() {
        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(false);
        when(signupLeadRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.empty());
        when(appSettingsService.getActiveSettings()).thenReturn(settingsWithCycleDefaults(28, 5));
        when(passwordEncoder.encode(any())).thenReturn("hashed-code");
        when(signupLeadRepository.save(any(SignupLead.class))).thenAnswer(inv -> {
            SignupLead l = inv.getArgument(0);
            if (l.getId() == null) l.setId(UUID.randomUUID());
            return l;
        });

        SignupRequest request = new SignupRequest("Jane", "Doe", "jane@example.com");
        SignupResponse response = service.signup(request);

        assertThat(response.email()).isEqualTo("jane@example.com");
        assertThat(response.expiresInSeconds()).isEqualTo(600L);
        assertThat(response.otpId()).isNotNull();
        verify(signupLeadRepository, atLeastOnce()).save(argThat(l -> !l.isOtpVerified() && "jane@example.com".equals(l.getEmail())));
        verify(userRepository, never()).save(any());
    }

    // --- verifySignupOtp (step 2) ---

    @Test
    void verifySignupOtpRejectsAnUnknownLead() {
        UUID leadId = UUID.randomUUID();
        when(signupLeadRepository.findById(leadId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.verifySignupOtp(new OtpVerifyRequest(leadId, "482913")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void verifySignupOtpRejectsAnExpiredCode() {
        UUID leadId = UUID.randomUUID();
        SignupLead lead = existingLead(leadId, false);
        lead.setOtpExpiresAt(Instant.now().minusSeconds(10));
        when(signupLeadRepository.findById(leadId)).thenReturn(Optional.of(lead));

        assertThatThrownBy(() -> service.verifySignupOtp(new OtpVerifyRequest(leadId, "482913")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.OTP_EXPIRED);
    }

    @Test
    void verifySignupOtpMarksTheLeadVerifiedWithoutCreatingAUser() {
        UUID leadId = UUID.randomUUID();
        SignupLead lead = existingLead(leadId, false);
        when(signupLeadRepository.findById(leadId)).thenReturn(Optional.of(lead));
        when(passwordEncoder.matches("482913", "hashed-code")).thenReturn(true);

        SignupOtpVerifiedResponse response = service.verifySignupOtp(new OtpVerifyRequest(leadId, "482913"));

        assertThat(response.leadId()).isEqualTo(leadId);
        verify(signupLeadRepository).save(argThat(SignupLead::isOtpVerified));
        verify(userRepository, never()).save(any());
    }

    // --- completeSignup (step 3: only place a password/User is ever created) ---

    @Test
    void completeSignupRejectsAnUnverifiedLead() {
        UUID leadId = UUID.randomUUID();
        when(signupLeadRepository.findById(leadId)).thenReturn(Optional.of(existingLead(leadId, false)));

        assertThatThrownBy(() -> service.completeSignup(new CompleteSignupRequest(leadId, "password123")))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
        verify(userRepository, never()).save(any());
    }

    @Test
    void completeSignupCreatesAVerifiedUserAndRemovesTheLead() {
        UUID leadId = UUID.randomUUID();
        SignupLead lead = existingLead(leadId, true);
        when(signupLeadRepository.findById(leadId)).thenReturn(Optional.of(lead));
        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(false);
        when(appSettingsService.getActiveSettings()).thenReturn(settingsWithCycleDefaults(28, 5));
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });
        stubTokenIssuance(null);

        TokenResponse response = service.completeSignup(new CompleteSignupRequest(leadId, "password123"));

        assertThat(response.accessToken()).isEqualTo("access-token");
        verify(userRepository).save(argThat(u -> u.isEmailVerified() && u.getCycleLength() == 28 && u.getPoints() == 0));
        verify(signupLeadRepository).delete(lead);
    }

    // --- verifyResetOtp ---

    @Test
    void verifyResetOtpRejectsAnUnknownEmailWithoutRevealingThat() {
        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.empty());
        ResetOtpVerifyRequest request = new ResetOtpVerifyRequest("jane@example.com", "482913");

        assertThatThrownBy(() -> service.verifyResetOtp(request))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
        verify(otpService, never()).checkValid(any(), any(), any());
    }

    @Test
    void verifyResetOtpChecksTheCodeWithoutConsumingIt() {
        UUID userId = UUID.randomUUID();
        User user = existingUser(userId);
        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(user));
        ResetOtpVerifyRequest request = new ResetOtpVerifyRequest("jane@example.com", "482913");

        service.verifyResetOtp(request);

        verify(otpService).checkValid(userId, "482913", OtpPurpose.password_reset);
        verify(userRepository, never()).save(any());
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

    @Test
    void loginWithCorrectPasswordButUnverifiedEmailIssuesAnOtpInsteadOfRejectingOutright() {
        UUID id = UUID.randomUUID();
        User user = existingUser(id);
        user.setEmailVerified(false);
        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct", "hashed")).thenReturn(true);
        UUID otpId = UUID.randomUUID();
        com.spotit.api.auth.entity.OtpCode otp = com.spotit.api.auth.entity.OtpCode.builder().id(otpId).build();
        when(otpService.issue(user, OtpPurpose.signup)).thenReturn(otp);

        assertThatThrownBy(() -> service.login(new LoginRequest("jane@example.com", "correct")))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException apiEx = (ApiException) e;
                    assertThat(apiEx.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_VERIFIED);
                    assertThat(apiEx.getOtpId()).isEqualTo(otpId);
                });
        verify(refreshTokenRepository, never()).save(any());
    }

    // --- verifyLoginOtp ---

    @Test
    void verifyLoginOtpMarksTheUserVerifiedAndIssuesTokens() {
        UUID id = UUID.randomUUID();
        UUID otpId = UUID.randomUUID();
        User user = existingUser(id);
        user.setEmailVerified(false);
        com.spotit.api.auth.entity.OtpCode otp = com.spotit.api.auth.entity.OtpCode.builder().id(otpId).userId(id).build();
        when(otpService.verify(otpId, "482913", OtpPurpose.signup)).thenReturn(otp);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        stubTokenIssuance(id);

        TokenResponse response = service.verifyLoginOtp(new OtpVerifyRequest(otpId, "482913"));

        assertThat(response.accessToken()).isEqualTo("access-token");
        verify(userRepository).save(argThat(User::isEmailVerified));
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

    // userId is only used to pin the stub to a specific id when the caller already knows it
    // (login/refresh); pass null to match any id, e.g. when the User is created inside the
    // call under test and its id isn't known until then (completeSignup).
    private void stubTokenIssuance(UUID userId) {
        when(jwtService.generateAccessToken(userId == null ? any() : eq(userId), any(), anyBoolean())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userId == null ? any() : eq(userId))).thenReturn("refresh-token");
        when(jwtService.accessTokenTtlSeconds()).thenReturn(3600L);
        when(jwtService.refreshTokenTtlSeconds()).thenReturn(2_592_000L);
    }
}
