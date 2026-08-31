package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.entity.PasswordHistory;
import com.spotit.api.auth.entity.RefreshToken;
import com.spotit.api.auth.entity.SignupLead;
import com.spotit.api.auth.repository.PasswordHistoryRepository;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.auth.repository.SignupLeadRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.mail.OtpEmailTemplate;
import com.spotit.api.common.security.JwtService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthWriteServiceImpl implements AuthWriteService {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final SignupLeadRepository signupLeadRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final ConfigurationDomainService configurationDomainService;
    private final Environment environment;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, ErrorMessage.EMAIL_ALREADY_REGISTERED);
        }
        SignupLead lead = signupLeadRepository.findByEmailIgnoreCase(request.email()).orElseGet(SignupLead::new);
        lead.setFirstName(request.firstName());
        lead.setLastName(request.lastName());
        lead.setEmail(request.email().toLowerCase());
        lead.setOtpVerified(false);

        long ttlSeconds = issueLeadOtp(lead);
        return new SignupResponse(lead.getId(), lead.getEmail(), ttlSeconds);
    }

    @Override
    @Transactional
    public SignupOtpVerifiedResponse verifySignupOtp(OtpVerifyRequest request) {
        SignupLead lead = signupLeadRepository.findById(request.otpId())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        if (lead.getOtpExpiresAt().isBefore(Instant.now())) {
            throw new ApiException(ErrorCode.OTP_EXPIRED, ErrorMessage.CODE_EXPIRED);
        }
        if (!passwordEncoder.matches(request.code(), lead.getOtpCodeHash())) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE);
        }
        lead.setOtpVerified(true);
        signupLeadRepository.save(lead);
        return new SignupOtpVerifiedResponse(lead.getId(), lead.getEmail());
    }

    @Override
    @Transactional
    public TokenResponse completeSignup(CompleteSignupRequest request) {
        SignupLead lead = signupLeadRepository.findById(request.leadId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.SIGNUP_NOT_FOUND));
        if (!lead.isOtpVerified()) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.OTP_NOT_VERIFIED);
        }

        if (userRepository.existsByEmailIgnoreCase(lead.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, ErrorMessage.EMAIL_ALREADY_REGISTERED);
        }

        User user = User.builder()
                .firstName(lead.getFirstName())
                .lastName(lead.getLastName())
                .email(lead.getEmail())
                .passwordHash(passwordEncoder.encode(request.password()))
                .emailVerified(true)
                .cycleLength(configurationDomainService.getCycleDefaultLength())
                .periodLength(configurationDomainService.getCycleDefaultPeriodLength())
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
        signupLeadRepository.delete(lead);
        recordPasswordHistory(user.getId(), user.getPasswordHash());

        return issueTokens(user);
    }

    private long issueLeadOtp(SignupLead lead) {
        long ttlSeconds = configurationDomainService.getOtpTtlSeconds();
        String code = "%06d".formatted(RANDOM.nextInt(1_000_000));

        if (environment.matchesProfiles("!prod")) {
            log.info("[DEV OTP] signup code for lead {} ({}): {}", lead.getId(), lead.getEmail(), code);
        }
        lead.setOtpCodeHash(passwordEncoder.encode(code));
        lead.setOtpExpiresAt(Instant.now().plusSeconds(ttlSeconds));
        signupLeadRepository.save(lead);

        String greeting = lead.getFirstName() == null || lead.getFirstName().isBlank() ? "there" : lead.getFirstName();
        long ttlMinutes = ttlSeconds / 60;
        String html = OtpEmailTemplate.html(greeting, code, "Verify your email", "here's your verification code to finish creating your Spot it account.", ttlMinutes);
        String text = OtpEmailTemplate.text(greeting, code, "Verify your email", "here's your verification code to finish creating your Spot it account.", ttlMinutes);
        try {
            emailService.send(lead.getEmail(), "Verify your Spot it account", html, text);
            log.info("Signup OTP email sent to lead {}", lead.getId());
        } catch (MailException e) {
            log.error("Failed to send signup OTP email to lead {}", lead.getId(), e);
        }
        return ttlSeconds;
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS);
        }

        if (!user.isEmailVerified()) {
            OtpCode otp = otpService.issue(user, OtpPurpose.signup);
            throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED, ErrorMessage.EMAIL_NOT_VERIFIED, otp.getId(), otpService.ttlSeconds());
        }
        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse verifyLoginOtp(OtpVerifyRequest request) {
        OtpCode otp = otpService.verify(request.otpId(), request.code(), OtpPurpose.signup);
        User user = userRepository.findById(otp.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        user.setEmailVerified(true);
        userRepository.save(user);
        return issueTokens(user);
    }

    @Override
    @Transactional
    public OtpRequestResponse forgotPassword(EmailRequest request) {
        userRepository.findByEmailIgnoreCase(request.email()).ifPresent(user ->
                otpService.issue(user, OtpPurpose.password_reset));

        return new OtpRequestResponse("If that email exists, a reset code has been sent.", null, otpService.ttlSeconds());
    }

    @Override
    @Transactional
    public OtpRequestResponse resendOtp(OtpResendRequest request) {
        var lead = signupLeadRepository.findById(request.otpId());
        if (lead.isPresent()) {
            long ttlSeconds = issueLeadOtp(lead.get());
            return new OtpRequestResponse("A new code has been sent.", lead.get().getId(), ttlSeconds);
        }
        OtpCode otp = otpService.resend(request.otpId());
        return new OtpRequestResponse("A new code has been sent.", otp.getId(), otpService.ttlSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyResetOtp(ResetOtpVerifyRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        otpService.checkValid(user.getId(), request.code(), OtpPurpose.password_reset);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        otpService.verifyLatest(user.getId(), request.code(), OtpPurpose.password_reset);
        enforcePasswordHistory(user.getId(), request.newPassword());
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(user.getId());
        recordPasswordHistory(user.getId(), user.getPasswordHash());
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
        Instant purgeBy = Instant.now().plus(java.time.Duration.ofDays(configurationDomainService.getAccountPurgeGraceDays()));
        user.setPendingDeletionAt(purgeBy);
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(userId);
        return new AccountDeletionResponse(ErrorMessage.ACCOUNT_DELETION_SCHEDULED, purgeBy);
    }

    private void enforcePasswordHistory(UUID userId, String rawPassword) {
        List<PasswordHistory> recent = passwordHistoryRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
        for (PasswordHistory entry : recent) {
            if (passwordEncoder.matches(rawPassword, entry.getPasswordHash())) {
                throw new ApiException(ErrorCode.PASSWORD_REUSED, ErrorMessage.PASSWORD_REUSED);
            }
        }
    }

    private void recordPasswordHistory(UUID userId, String passwordHash) {
        PasswordHistory entry = PasswordHistory.builder()
                .userId(userId)
                .passwordHash(passwordHash)
                .build();
        passwordHistoryRepository.save(entry);
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
