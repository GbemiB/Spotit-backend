package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.entity.RefreshToken;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.security.JwtService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.settings.service.AppSettingsService;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthWriteServiceImpl implements AuthWriteService {

    private static final long ACCOUNT_PURGE_GRACE_DAYS = 30;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final AppSettingsService appSettingsService;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, ErrorMessage.EMAIL_ALREADY_REGISTERED);
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .emailVerified(false)
                .cycleLength(appSettingsService.getActiveSettings().cycleDefaultLength())
                .periodLength(appSettingsService.getActiveSettings().cycleDefaultPeriodLength())
                .themePref(ThemePref.system)
                .onboarded(false)
                .notifPeriod(true)
                .notifOvulation(true)
                .notifDailyLog(true)
                .notifDigest(false)
                .points(0)
                .streak(0)
                .longestStreak(0)
                .premium(false)
                .build();
        user = userRepository.save(user);

        OtpCode otp = otpService.issue(user, OtpPurpose.signup);
        return new SignupResponse(user.getId(), user.getEmail(), true, otp.getId(), otpService.ttlSeconds());
    }

    @Override
    @Transactional
    public TokenResponse verifySignupOtp(OtpVerifyRequest request) {
        OtpCode otp = otpService.verify(request.otpId(), request.code(), OtpPurpose.signup);
        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        user.setEmailVerified(true);
        userRepository.save(user);
        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS);
        }
        return issueTokens(user);
    }

    @Override
    @Transactional
    public OtpRequestResponse forgotPassword(EmailRequest request) {
        userRepository.findByEmailIgnoreCase(request.email()).ifPresent(user ->
                otpService.issue(user, OtpPurpose.password_reset));
        // Same response whether or not the email exists, to avoid account enumeration.
        return new OtpRequestResponse("If that email exists, a reset code has been sent.", null, otpService.ttlSeconds());
    }

    @Override
    @Transactional
    public OtpRequestResponse resendOtp(OtpResendRequest request) {
        OtpCode otp = otpService.resend(request.otpId());
        return new OtpRequestResponse("A new code has been sent.", otp.getId(), otpService.ttlSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyResetOtp(ResetOtpVerifyRequest request) {
        // Same error whether the email doesn't exist or the code is wrong, to avoid account enumeration.
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        otpService.checkValid(user.getId(), request.code(), OtpPurpose.password_reset);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Same error whether the email doesn't exist or the code is wrong, to avoid account enumeration.
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        otpService.verifyLatest(user.getId(), request.code(), OtpPurpose.password_reset);
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(user.getId());
    }

    @Override
    @Transactional
    public AccessTokenResponse refresh(RefreshTokenRequest request) {
        UUID userId = jwtService.parseRefreshToken(request.refreshToken())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, ErrorMessage.INVALID_REFRESH_TOKEN));

        String hash = TokenHasher.sha256Hex(request.refreshToken());
        RefreshToken stored = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, ErrorMessage.INVALID_REFRESH_TOKEN));
        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, ErrorMessage.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, ErrorMessage.INVALID_REFRESH_TOKEN));
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.isPremium());
        return new AccessTokenResponse(accessToken, jwtService.accessTokenTtlSeconds());
    }

    @Override
    @Transactional
    public void logout(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public AccountDeletionResponse scheduleAccountDeletion(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Instant purgeBy = Instant.now().plus(java.time.Duration.ofDays(ACCOUNT_PURGE_GRACE_DAYS));
        user.setPendingDeletionAt(purgeBy);
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(userId);
        return new AccountDeletionResponse(ErrorMessage.ACCOUNT_DELETION_SCHEDULED, purgeBy);
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.isPremium());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken stored = RefreshToken.builder()
                .userId(user.getId())
                .tokenHash(TokenHasher.sha256Hex(refreshToken))
                .expiresAt(Instant.now().plusSeconds(jwtService.refreshTokenTtlSeconds()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(stored);

        return new TokenResponse(
                accessToken,
                refreshToken,
                jwtService.accessTokenTtlSeconds(),
                new TokenResponse.UserSummary(user.getId(), user.isOnboarded())
        );
    }
}
