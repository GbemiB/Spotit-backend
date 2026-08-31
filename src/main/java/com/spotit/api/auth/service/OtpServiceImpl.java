package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.mail.OtpEmailTemplate;
import com.spotit.api.configuration.service.ConfigurationDomainService;
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
    private final ConfigurationDomainService configurationDomainService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OtpCode issue(User user, OtpPurpose purpose) {
        long ttlSeconds = configurationDomainService.getOtpTtlSeconds();
        otpCodeRepository.invalidateActive(user.getId(), purpose);
        String code = "%06d".formatted(RANDOM.nextInt(1_000_000));

        log.warn("[OTP] {} code for user {} ({}): {}", purpose, user.getId(), user.getEmail(), code);
        OtpCode otp = OtpCode.builder()
                .userId(user.getId())
                .codeHash(passwordEncoder.encode(code))
                .purpose(purpose)
                .expiresAt(Instant.now().plusSeconds(ttlSeconds))
                .consumed(false)
                .build();
        otp = otpCodeRepository.save(otp);
        sendOtpEmail(user, purpose, code, ttlSeconds);
        return otp;
    }

    private void sendOtpEmail(User user, OtpPurpose purpose, String code, long ttlSeconds) {
        boolean isSignup = purpose == OtpPurpose.signup;
        String subject = isSignup ? "Verify your Spot it account" : "Reset your Spot it password";
        String heading = isSignup ? "Verify your email" : "Reset your password";
        String introLine = isSignup
                ? "here's your verification code to finish creating your Spot it account."
                : "here's your code to reset your Spot it password.";
        String greeting = user.getFirstName() == null || user.getFirstName().isBlank() ? "there" : user.getFirstName();
        long ttlMinutes = ttlSeconds / 60;
        String html = OtpEmailTemplate.html(greeting, code, heading, introLine, ttlMinutes);
        String text = OtpEmailTemplate.text(greeting, code, heading, introLine, ttlMinutes);
        try {
            emailService.send(user.getEmail(), subject, html, text);
            log.info("OTP email sent to user {} for purpose {}", user.getId(), purpose);
        } catch (MailException e) {
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
        return configurationDomainService.getOtpTtlSeconds();
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
