package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.config.SpotItProperties;
import com.spotit.api.user.entity.User;
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

    @Override
    @Transactional
    public OtpCode issue(User user, OtpPurpose purpose) {
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
        String subject = purpose == OtpPurpose.signup ? "Verify your Spot it account" : "Reset your Spot it password";
        String greeting = user.getFirstName() == null || user.getFirstName().isBlank() ? "there" : user.getFirstName();
        long ttlMinutes = properties.otp().ttlSeconds() / 60;
        String body = "Hi " + greeting + ",\n\n" +
                "Your Spot it verification code is: " + code + "\n\n" +
                "This code expires in " + ttlMinutes + " minutes. If you didn't request this, you can ignore this email.\n\n" +
                "— Spot it";
        try {
            emailService.send(user.getEmail(), subject, body);
            log.info("OTP email sent to user {} for purpose {}", user.getId(), purpose);
        } catch (MailException e) {
            log.warn("Failed to send OTP email to user {} for purpose {}: {}", user.getId(), purpose, e.getMessage());
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

    private OtpCode checkAndConsume(OtpCode otp, String code) {
        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new ApiException(ErrorCode.OTP_EXPIRED, ErrorMessage.CODE_EXPIRED);
        }
        if (!passwordEncoder.matches(code, otp.getCodeHash())) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.INVALID_OR_USED_CODE);
        }
        otp.setConsumed(true);
        return otpCodeRepository.save(otp);
    }
}
