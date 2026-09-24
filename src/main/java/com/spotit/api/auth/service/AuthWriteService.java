package com.spotit.api.auth.service;

import com.spotit.api.auth.dto.*;

import java.util.UUID;

public interface AuthWriteService {
    SignupResponse signup(SignupRequest request);

    SignupOtpVerifiedResponse verifySignupOtp(OtpVerifyRequest request);

    TokenResponse completeSignup(CompleteSignupRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse verifyLoginOtp(OtpVerifyRequest request);

    OtpRequestResponse forgotPassword(EmailRequest request);

    OtpRequestResponse resendOtp(OtpResendRequest request);

    void verifyResetOtp(ResetOtpVerifyRequest request);

    void resetPassword(ResetPasswordRequest request);

    AccessTokenResponse refresh(RefreshTokenRequest request);

    void logout(UUID userId);

    AccountDeletionResponse scheduleAccountDeletion(UUID userId);
}
