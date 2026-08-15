package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.mail.OtpEmailTemplate;
import com.spotit.api.config.SpotItProperties;
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
public class OtpServiceImpl implements OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpCodeRepository otpCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpotItProperties properties;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OtpCode issue(User user, OtpPurpose purpose) {
        otpCodeRepository.invalidateActive(user.getId(), purpose);
        String code = "%06d".formatted(RANDOM.nextInt(1_000_000));
        OtpCode otp = OtpCode.builder()
                .userId(user.getId())
                .codeHash(passwordEncoder.encode(code))
                .purpose(purpose)
                .expiresAt(Instant.now().plusSeconds(properties.otp().ttlSeconds()))
                .consumed(false)
                .build();
        otp = otpCodeRepository.save(otp);
        sendOtpEmail(user, purpose, code);
        return otp;
    }

    private void sendOtpEmail(User user, OtpPurpose purpose, String code) {
        boolean isSignup = purpose == OtpPurpose.signup;
        String subject = isSignup ? "Verify your Spot it account" : "Reset your Spot it password";
        String heading = isSignup ? "Verify your email" : "Reset your password";
        String introLine = isSignup
                ? "here's your verification code to finish creating your Spot it account."
                : "here's your code to reset your Spot it password.";
        String greeting = user.getFirstName() == null || user.getFirstName().isBlank() ? "there" : user.getFirstName();
        long ttlMinutes = properties.otp().ttlSeconds() / 60;
        String html = OtpEmailTemplate.html(greeting, code, heading, introLine, ttlMinutes);
        String text = OtpEmailTemplate.text(greeting, code, heading, introLine, ttlMinutes);
        try {
            emailService.send(user.getEmail(), subject, html, text);
            log.info("OTP email sent to user {} for purpose {}", user.getId(), purpose);
        } catch (MailException e) {
            // Swallowed on purpose (a mail outage shouldn't block signup/login), but logged at
            // error with the full stack trace — this is the first place to look when a user
            // reports never receiving a code. Common causes: wrong active Spring profile (the
            // "dev" profile points at a local Mailpit catcher, not a real inbox) or missing/bad
            // MAIL_USERNAME / MAIL_PASSWORD for the configured SMTP host.
            log.error("Failed to send OTP email to user {} for purpose {}", user.getId(), purpose, e);
        }
    }

    @Override
    @Transactional
    public OtpCode verify(UUID otpId, String code, OtpPurpose expectedPurpose) {
        OtpCode otp = otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));

        if (otp.getPurpose() != expectedPurpose) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE);
        }
        return checkAndConsume(otp, code);
    }

    @Override
    @Transactional
    public OtpCode verifyLatest(UUID userId, String code, OtpPurpose expectedPurpose) {
        OtpCode otp = otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(userId, expectedPurpose)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        return checkAndConsume(otp, code);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkValid(UUID userId, String code, OtpPurpose expectedPurpose) {
        OtpCode otp = otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(userId, expectedPurpose)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        assertValid(otp, code);
    }

    @Override
    @Transactional
    public OtpCode resend(UUID otpId) {
        OtpCode existing = otpCodeRepository.findById(otpId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE));
        User user = userRepository.findById(existing.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        return issue(user, existing.getPurpose());
    }

    @Override
    public long ttlSeconds() {
        return properties.otp().ttlSeconds();
    }

    private OtpCode checkAndConsume(OtpCode otp, String code) {
        assertValid(otp, code);
        otp.setConsumed(true);
        return otpCodeRepository.save(otp);
    }

    private void assertValid(OtpCode otp, String code) {
        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new ApiException(ErrorCode.OTP_EXPIRED, ErrorMessage.CODE_EXPIRED);
        }
        if (!passwordEncoder.matches(code, otp.getCodeHash())) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE);
        }
    }
}
