package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.configuration.service.ConfigurationDomainService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {
    @Mock OtpCodeRepository otpCodeRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock ConfigurationDomainService configurationDomainService;
    @Mock EmailService emailService;
    @Mock UserRepository userRepository;
    @Mock Environment environment;

    OtpServiceImpl service;
    User user;

    @BeforeEach
    void setUp() {
        service = new OtpServiceImpl(otpCodeRepository, passwordEncoder, configurationDomainService, emailService, userRepository, environment);
        user = User.builder().id(UUID.randomUUID()).firstName("Jane").email("jane@example.com").build();
    }

    @Test
    void issueInvalidatesOlderCodesAndSendsAnEmail() {
        when(configurationDomainService.getOtpTtlSeconds()).thenReturn(600L);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(otpCodeRepository.save(any(OtpCode.class))).thenAnswer(inv -> inv.getArgument(0));

        OtpCode result = service.issue(user, OtpPurpose.signup);

        verify(otpCodeRepository).invalidateActive(user.getId(), OtpPurpose.signup);
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getPurpose()).isEqualTo(OtpPurpose.signup);
        assertThat(result.isConsumed()).isFalse();
        verify(emailService).send(eq("jane@example.com"), anyString(), anyString(), anyString());
    }

    @Test
    void issueSwallowsMailFailuresRatherThanFailingTheRequest() {
        when(configurationDomainService.getOtpTtlSeconds()).thenReturn(600L);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(otpCodeRepository.save(any(OtpCode.class))).thenAnswer(inv -> inv.getArgument(0));
        doThrow(new org.springframework.mail.MailSendException("smtp down"))
                .when(emailService).send(anyString(), anyString(), anyString(), anyString());

        OtpCode result = service.issue(user, OtpPurpose.signup);

        assertThat(result).isNotNull();
    }

    @Test
    void verifyRejectsAnUnknownOrAlreadyConsumedCode() {
        UUID otpId = UUID.randomUUID();
        when(otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.verify(otpId, "123456", OtpPurpose.signup))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void verifyRejectsAPurposeMismatch() {
        UUID otpId = UUID.randomUUID();
        OtpCode otp = OtpCode.builder().id(otpId).purpose(OtpPurpose.password_reset)
                .expiresAt(Instant.now().plusSeconds(60)).build();
        when(otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)).thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> service.verify(otpId, "123456", OtpPurpose.signup))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void verifyRejectsAnExpiredCode() {
        UUID otpId = UUID.randomUUID();
        OtpCode otp = OtpCode.builder().id(otpId).purpose(OtpPurpose.signup)
                .expiresAt(Instant.now().minusSeconds(1)).codeHash("hashed").build();
        when(otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)).thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> service.verify(otpId, "123456", OtpPurpose.signup))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.OTP_EXPIRED);
    }

    @Test
    void verifyRejectsAWrongCode() {
        UUID otpId = UUID.randomUUID();
        OtpCode otp = OtpCode.builder().id(otpId).purpose(OtpPurpose.signup)
                .expiresAt(Instant.now().plusSeconds(60)).codeHash("hashed").build();
        when(otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)).thenReturn(Optional.of(otp));
        when(passwordEncoder.matches("000000", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.verify(otpId, "000000", OtpPurpose.signup))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void verifyWithTheCorrectCodeConsumesIt() {
        UUID otpId = UUID.randomUUID();
        OtpCode otp = OtpCode.builder().id(otpId).purpose(OtpPurpose.signup)
                .expiresAt(Instant.now().plusSeconds(60)).codeHash("hashed").build();
        when(otpCodeRepository.findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(otpId)).thenReturn(Optional.of(otp));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);
        when(otpCodeRepository.save(any(OtpCode.class))).thenAnswer(inv -> inv.getArgument(0));

        OtpCode result = service.verify(otpId, "123456", OtpPurpose.signup);

        assertThat(result.isConsumed()).isTrue();
    }

    @Test
    void checkValidAcceptsACorrectUnexpiredCodeWithoutConsumingIt() {
        OtpCode otp = OtpCode.builder().id(UUID.randomUUID()).userId(user.getId()).purpose(OtpPurpose.password_reset)
                .expiresAt(Instant.now().plusSeconds(60)).codeHash("hashed").consumed(false).build();
        when(otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(user.getId(), OtpPurpose.password_reset))
                .thenReturn(Optional.of(otp));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);

        service.checkValid(user.getId(), "123456", OtpPurpose.password_reset);

        assertThat(otp.isConsumed()).isFalse();
        verify(otpCodeRepository, never()).save(any(OtpCode.class));
    }

    @Test
    void checkValidRejectsAWrongCode() {
        OtpCode otp = OtpCode.builder().id(UUID.randomUUID()).userId(user.getId()).purpose(OtpPurpose.password_reset)
                .expiresAt(Instant.now().plusSeconds(60)).codeHash("hashed").build();
        when(otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(user.getId(), OtpPurpose.password_reset))
                .thenReturn(Optional.of(otp));
        when(passwordEncoder.matches("000000", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.checkValid(user.getId(), "000000", OtpPurpose.password_reset))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void checkValidRejectsAnExpiredCode() {
        OtpCode otp = OtpCode.builder().id(UUID.randomUUID()).userId(user.getId()).purpose(OtpPurpose.password_reset)
                .expiresAt(Instant.now().minusSeconds(1)).codeHash("hashed").build();
        when(otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(user.getId(), OtpPurpose.password_reset))
                .thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> service.checkValid(user.getId(), "123456", OtpPurpose.password_reset))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.OTP_EXPIRED);
    }

    @Test
    void resendRejectsAnUnknownOtpId() {
        UUID otpId = UUID.randomUUID();
        when(otpCodeRepository.findById(otpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resend(otpId))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_CODE);
    }

    @Test
    void resendIssuesANewCodeForTheSamePurpose() {
        UUID otpId = UUID.randomUUID();
        OtpCode existing = OtpCode.builder().id(otpId).userId(user.getId()).purpose(OtpPurpose.password_reset).build();
        when(otpCodeRepository.findById(otpId)).thenReturn(Optional.of(existing));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(configurationDomainService.getOtpTtlSeconds()).thenReturn(600L);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(otpCodeRepository.save(any(OtpCode.class))).thenAnswer(inv -> inv.getArgument(0));

        OtpCode result = service.resend(otpId);

        assertThat(result.getPurpose()).isEqualTo(OtpPurpose.password_reset);
        verify(otpCodeRepository).invalidateActive(user.getId(), OtpPurpose.password_reset);
    }
}
