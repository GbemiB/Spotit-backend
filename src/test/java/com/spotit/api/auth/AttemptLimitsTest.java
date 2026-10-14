package com.spotit.api.auth;

import com.spotit.api.auth.dto.LoginRequest;
import com.spotit.api.auth.dto.ResetOtpVerifyRequest;
import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.auth.service.AuthAttemptRecorder;
import com.spotit.api.auth.service.AuthWriteService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AttemptLimitsTest {
    private static final String PASSWORD = "correct-horse";

    @Autowired AuthWriteService authWriteService;
    @Autowired UserRepository userRepository;
    @Autowired OtpCodeRepository otpCodeRepository;
    @Autowired PasswordEncoder passwordEncoder;

    String email;
    UUID userId;

    @BeforeEach
    void createUser() {
        email = "limits-" + UUID.randomUUID() + "@example.com";
        userId = userRepository.save(User.builder().firstName("Jane").lastName("Doe").email(email)
                .passwordHash(passwordEncoder.encode(PASSWORD)).emailVerified(true).cycleLength(28).periodLength(5)
                .themePref(ThemePref.dark).build()).getId();
    }

    private ErrorCode loginError(String password) {
        try {
            authWriteService.login(new LoginRequest(email, password));
            return null;
        } catch (ApiException e) {
            return e.getErrorCode();
        }
    }

    @Test
    void theFifthWrongPasswordLocksTheAccountEvenForTheRightPassword() {
        for (int i = 1; i < AuthAttemptRecorder.MAX_LOGIN_ATTEMPTS; i++) {
            assertThat(loginError("wrong-" + i)).isEqualTo(ErrorCode.INVALID_CREDENTIALS);
        }
        assertThat(userRepository.findById(userId).orElseThrow().getFailedLoginAttempts())
                .isEqualTo(AuthAttemptRecorder.MAX_LOGIN_ATTEMPTS - 1);

        assertThat(loginError("wrong-again")).isEqualTo(ErrorCode.ACCOUNT_LOCKED);
        assertThat(loginError(PASSWORD)).isEqualTo(ErrorCode.ACCOUNT_LOCKED);
        assertThat(userRepository.findById(userId).orElseThrow().getLockedUntil()).isAfter(Instant.now());
    }

    @Test
    void aSuccessfulLoginResetsTheCount() {
        assertThat(loginError("wrong")).isEqualTo(ErrorCode.INVALID_CREDENTIALS);
        assertThat(loginError(PASSWORD)).isNull();
        assertThat(userRepository.findById(userId).orElseThrow().getFailedLoginAttempts()).isZero();
    }

    @Test
    void theLockLiftsOnceItsTimeHasPassed() {
        User user = userRepository.findById(userId).orElseThrow();
        user.setLockedUntil(Instant.now().minusSeconds(1));
        userRepository.save(user);

        assertThat(loginError(PASSWORD)).isNull();
        assertThat(userRepository.findById(userId).orElseThrow().getLockedUntil()).isNull();
    }

    @Test
    void theFifthWrongCodeExpiresIt() {
        UUID otpId = otpCodeRepository.save(OtpCode.builder().userId(userId).codeHash(passwordEncoder.encode("123456"))
                .purpose(OtpPurpose.password_reset).expiresAt(Instant.now().plusSeconds(600)).consumed(false).build()).getId();

        for (int i = 1; i < AuthAttemptRecorder.MAX_OTP_ATTEMPTS; i++) {
            assertThatThrownBy(() -> authWriteService.verifyResetOtp(new ResetOtpVerifyRequest(email, "000000")))
                    .extracting(e -> ((ApiException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CODE);
        }
        assertThatThrownBy(() -> authWriteService.verifyResetOtp(new ResetOtpVerifyRequest(email, "000000")))
                .extracting(e -> ((ApiException) e).getErrorCode()).isEqualTo(ErrorCode.OTP_EXPIRED);

        assertThat(otpCodeRepository.findById(otpId).orElseThrow().getFailedAttempts()).isEqualTo(AuthAttemptRecorder.MAX_OTP_ATTEMPTS);
        assertThatThrownBy(() -> authWriteService.verifyResetOtp(new ResetOtpVerifyRequest(email, "123456")))
                .extracting(e -> ((ApiException) e).getErrorCode()).isEqualTo(ErrorCode.OTP_EXPIRED);
    }
}
