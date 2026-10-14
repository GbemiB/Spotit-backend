package com.spotit.api.auth.service;

import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.auth.repository.SignupLeadRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthAttemptRecorder {
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final Duration LOGIN_LOCK_DURATION = Duration.ofMinutes(15);
    public static final int MAX_OTP_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final SignupLeadRepository signupLeadRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean recordLoginFailure(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            int attempts = attemptsOf(user.getFailedLoginAttempts()) + 1;
            boolean locked = attempts >= MAX_LOGIN_ATTEMPTS;
            user.setFailedLoginAttempts(locked ? 0 : attempts);
            if (locked) {
                user.setLockedUntil(Instant.now().plus(LOGIN_LOCK_DURATION));
                log.warn("Account {} locked after {} failed login attempts", userId, MAX_LOGIN_ATTEMPTS);
            }
            userRepository.save(user);
            return locked;
        }).orElse(false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean recordOtpFailure(UUID otpId) {
        return otpCodeRepository.findById(otpId).map(otp -> {
            int attempts = attemptsOf(otp.getFailedAttempts()) + 1;
            boolean exhausted = attempts >= MAX_OTP_ATTEMPTS;
            otp.setFailedAttempts(attempts);
            if (exhausted) {
                otp.setExpiresAt(Instant.now());
                log.warn("OTP {} for user {} expired after {} wrong attempts", otpId, otp.getUserId(), MAX_OTP_ATTEMPTS);
            }
            otpCodeRepository.save(otp);
            return exhausted;
        }).orElse(false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean recordSignupOtpFailure(UUID leadId) {
        return signupLeadRepository.findById(leadId).map(lead -> {
            int attempts = attemptsOf(lead.getOtpFailedAttempts()) + 1;
            boolean exhausted = attempts >= MAX_OTP_ATTEMPTS;
            lead.setOtpFailedAttempts(attempts);
            if (exhausted) {
                lead.setOtpExpiresAt(Instant.now());
                log.warn("Signup code for lead {} expired after {} wrong attempts", leadId, MAX_OTP_ATTEMPTS);
            }
            signupLeadRepository.save(lead);
            return exhausted;
        }).orElse(false);
    }

    private static int attemptsOf(Integer stored) {
        return stored == null ? 0 : stored;
    }
}
