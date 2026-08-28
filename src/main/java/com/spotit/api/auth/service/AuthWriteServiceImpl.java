package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.entity.RefreshToken;
import com.spotit.api.auth.entity.SignupLead;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.auth.repository.SignupLeadRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.mail.OtpEmailTemplate;
import com.spotit.api.common.security.JwtService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.settings.service.AppSettingsService;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthWriteServiceImpl implements AuthWriteService {

    private static final long ACCOUNT_PURGE_GRACE_DAYS = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SignupLeadRepository signupLeadRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final AppSettingsService appSettingsService;

    // Signup is a two-step process: (1) SignupRequest captures name/email and creates/refreshes
    // a SignupLead — no password, no User row yet, so there's nothing a bypass could log into.
    // (2) Only after the OTP on that lead is verified does completeSignup() take a password and
    // create the real User. A lead that never verifies is left in signup_leads (not deleted) so
    // it can be followed up on (e.g. re-engagement email/ads) instead of silently vanishing.
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
        // Don't save here — otp_code_hash/otp_expires_at are NOT NULL columns, and a brand-new
        // lead doesn't have them set yet at this point. issueLeadOtp() sets both and saves;
        // saving before that (as this used to) violated the not-null constraint on every
        // first-time signup, since only a re-signup on an *existing* lead row happened to
        // already have non-null values left over from before.
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
        // Re-check uniqueness: another signup for the same email could have completed first.
        if (userRepository.existsByEmailIgnoreCase(lead.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, ErrorMessage.EMAIL_ALREADY_REGISTERED);
        }

        User user = User.builder()
                .firstName(lead.getFirstName())
                .lastName(lead.getLastName())
                .email(lead.getEmail())
                .passwordHash(passwordEncoder.encode(request.password()))
                .emailVerified(true)
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
        signupLeadRepository.delete(lead);

        return issueTokens(user);
    }

    private long issueLeadOtp(SignupLead lead) {
        long ttlSeconds = appSettingsService.getActiveSettings().otpTtlSeconds();
        String code = "%06d".formatted(RANDOM.nextInt(1_000_000));
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
            // Swallowed on purpose (a mail outage shouldn't block signup) but logged with the
            // full stack trace — this lead now just sits unverified in signup_leads until a
            // resend succeeds or it's picked up for follow-up. See EmailServiceImpl/SmtpSettingsService.
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
        // Defense in depth: a User is only ever created with emailVerified=true (see
        // completeSignup), so this should be unreachable — but if it ever isn't, login must
        // not silently grant a session to an unverified account.
        if (!user.isEmailVerified()) {
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
        // Only used to resend a signup-verification code (password-reset resend goes through
        // forgotPassword instead), so this resends against the SignupLead.
        SignupLead lead = signupLeadRepository.findById(request.otpId())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        long ttlSeconds = issueLeadOtp(lead);
        return new OtpRequestResponse("A new code has been sent.", lead.getId(), ttlSeconds);
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
