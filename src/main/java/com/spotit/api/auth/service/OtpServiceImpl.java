package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.config.SpotItProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Transactional
    public OtpCode issue(UUID userId, OtpPurpose purpose) {
        String code = "%06d".formatted(RANDOM.nextInt(1_000_000));
        OtpCode otp = OtpCode.builder()
                .userId(userId)
                .codeHash(passwordEncoder.encode(code))
                .purpose(purpose)
                .expiresAt(Instant.now().plusSeconds(properties.otp().ttlSeconds()))
                .consumed(false)
                .build();
        otp = otpCodeRepository.save(otp);
        log.info("[STUB email/SMS provider] OTP for user {} ({}): {}", userId, purpose, code);
        return otp;
    }

    @Override
    @Transactional
    public OtpCode verify(UUID otpId, String code, OtpPurpose expectedPurpose) {
        OtpCode otp = otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CODE, "Invalid or already-used code."));

        if (otp.getPurpose() != expectedPurpose) {
            throw new ApiException(ErrorCode.INVALID_CODE, "Invalid or already-used code.");
        }
        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new ApiException(ErrorCode.OTP_EXPIRED, "This code has expired.");
        }
        if (!passwordEncoder.matches(code, otp.getCodeHash())) {
            throw new ApiException(ErrorCode.INVALID_CODE, "Invalid or already-used code.");
        }
        otp.setConsumed(true);
        return otpCodeRepository.save(otp);
    }
}
