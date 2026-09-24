package com.spotit.api.auth.controller;

import com.spotit.api.auth.dto.AccessTokenResponse;
import com.spotit.api.auth.dto.AccountDeletionResponse;
import com.spotit.api.auth.dto.CompleteSignupRequest;
import com.spotit.api.auth.dto.EmailRequest;
import com.spotit.api.auth.dto.LoginRequest;
import com.spotit.api.auth.dto.OtpRequestResponse;
import com.spotit.api.auth.dto.OtpResendRequest;
import com.spotit.api.auth.dto.OtpVerifyRequest;
import com.spotit.api.auth.dto.RefreshTokenRequest;
import com.spotit.api.auth.dto.ResetOtpVerifyRequest;
import com.spotit.api.auth.dto.ResetPasswordRequest;
import com.spotit.api.auth.dto.SignupOtpVerifiedResponse;
import com.spotit.api.auth.dto.SignupRequest;
import com.spotit.api.auth.dto.SignupResponse;
import com.spotit.api.auth.dto.TokenResponse;
import com.spotit.api.auth.service.AuthWriteService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock AuthWriteService authWriteService;

    MockMvc mvc;
    UUID otpId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new AuthController(authWriteService));
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    private TokenResponse tokens() {
        return new TokenResponse("access", "refresh", 900, new TokenResponse.UserSummary(USER_ID, true));
    }

    @Test
    void signupReturns201WithTheOtpHandle() throws Exception {
        SignupRequest request = new SignupRequest("Jane", "Doe", "jane@example.com");
        when(authWriteService.signup(request)).thenReturn(new SignupResponse(otpId, "jane@example.com", 600));

        mvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.data.otpId").value(otpId.toString()))
                .andExpect(jsonPath("$.data.expiresInSeconds").value(600));
    }

    @Test
    void signupRejectsAnInvalidEmailWithoutCallingTheService() throws Exception {
        mvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SignupRequest("Jane", "Doe", "not-an-email"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.data.errorCode").value("validation_error"));

        verifyNoInteractions(authWriteService);
    }

    @Test
    void signupRejectsMissingNames() throws Exception {
        mvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SignupRequest("", null, "jane@example.com"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(authWriteService);
    }

    @Test
    void signupWithAnAlreadyRegisteredEmailMapsTo409() throws Exception {
        when(authWriteService.signup(any())).thenThrow(new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, "Email taken"));

        mvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SignupRequest("Jane", "Doe", "jane@example.com"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("Email taken"))
                .andExpect(jsonPath("$.data.errorCode").value("email_already_registered"));
    }

    @Test
    void malformedJsonIsA400() throws Exception {
        mvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.errorCode").value("bad_request"));
    }

    @Test
    void verifyOtpReturnsTheLead() throws Exception {
        UUID leadId = UUID.randomUUID();
        OtpVerifyRequest request = new OtpVerifyRequest(otpId, "123456");
        when(authWriteService.verifySignupOtp(request)).thenReturn(new SignupOtpVerifiedResponse(leadId, "jane@example.com"));

        mvc.perform(post("/api/v1/auth/otp/verify").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.leadId").value(leadId.toString()));
    }

    @Test
    void verifyOtpRequiresACode() throws Exception {
        mvc.perform(post("/api/v1/auth/otp/verify").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new OtpVerifyRequest(otpId, " "))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(authWriteService);
    }

    @Test
    void completeSignupIssuesTokens() throws Exception {
        CompleteSignupRequest request = new CompleteSignupRequest(UUID.randomUUID(), "s3cret-pass");
        when(authWriteService.completeSignup(request)).thenReturn(tokens());

        mvc.perform(post("/api/v1/auth/signup/complete").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("access"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh"))
                .andExpect(jsonPath("$.data.user.userId").value(USER_ID.toString()));
    }

    @Test
    void completeSignupRejectsAShortPassword() throws Exception {
        mvc.perform(post("/api/v1/auth/signup/complete").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CompleteSignupRequest(UUID.randomUUID(), "short"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("password must be at least 8 characters"));

        verifyNoInteractions(authWriteService);
    }

    @Test
    void resendOtpReturnsTheNewHandle() throws Exception {
        OtpResendRequest request = new OtpResendRequest(otpId);
        when(authWriteService.resendOtp(request)).thenReturn(new OtpRequestResponse("Code sent.", otpId, 600));

        mvc.perform(post("/api/v1/auth/otp/resend").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.otpId").value(otpId.toString()));
    }

    @Test
    void loginIssuesTokens() throws Exception {
        LoginRequest request = new LoginRequest("jane@example.com", "s3cret-pass");
        when(authWriteService.login(request)).thenReturn(tokens());

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("access"));
    }

    @Test
    void loginWithWrongCredentialsIs401() throws Exception {
        when(authWriteService.login(any())).thenThrow(new ApiException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS));

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("jane@example.com", "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data.errorCode").value("invalid_credentials"));
    }

    @Test
    void loginForAnUnverifiedAccountCarriesTheFreshOtpId() throws Exception {
        when(authWriteService.login(any()))
                .thenThrow(new ApiException(ErrorCode.EMAIL_NOT_VERIFIED, "Verify your email", otpId, 600));

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("jane@example.com", "s3cret-pass"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.data.errorCode").value("email_not_verified"))
                .andExpect(jsonPath("$.data.otpId").value(otpId.toString()))
                .andExpect(jsonPath("$.data.expiresInSeconds").value(600));
    }

    @Test
    void verifyLoginOtpIssuesTokens() throws Exception {
        OtpVerifyRequest request = new OtpVerifyRequest(otpId, "123456");
        when(authWriteService.verifyLoginOtp(request)).thenReturn(tokens());

        mvc.perform(post("/api/v1/auth/login/verify-otp").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.refreshToken").value("refresh"));
    }

    @Test
    void forgotPasswordReturnsTheOtpHandle() throws Exception {
        EmailRequest request = new EmailRequest("jane@example.com");
        when(authWriteService.forgotPassword(request)).thenReturn(new OtpRequestResponse("If registered, a code was sent.", otpId, 600));

        mvc.perform(post("/api/v1/auth/forgot-password").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("If registered, a code was sent."));
    }

    @Test
    void verifyResetOtpReturnsAMessageEnvelope() throws Exception {
        ResetOtpVerifyRequest request = new ResetOtpVerifyRequest("jane@example.com", "123456");

        mvc.perform(post("/api/v1/auth/reset-password/verify-otp").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ErrorMessage.CODE_VERIFIED))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(authWriteService).verifyResetOtp(request);
    }

    @Test
    void verifyResetOtpWithAnExpiredCodeIs410() throws Exception {
        ResetOtpVerifyRequest request = new ResetOtpVerifyRequest("jane@example.com", "123456");
        org.mockito.Mockito.doThrow(new ApiException(ErrorCode.OTP_EXPIRED, "Code expired"))
                .when(authWriteService).verifyResetOtp(request);

        mvc.perform(post("/api/v1/auth/reset-password/verify-otp").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.data.errorCode").value("otp_expired"));
    }

    @Test
    void resetPasswordUpdatesThePassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("jane@example.com", "123456", "n3w-password");

        mvc.perform(post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated."));

        verify(authWriteService).resetPassword(request);
    }

    @Test
    void resetPasswordRejectsAShortNewPassword() throws Exception {
        mvc.perform(post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new ResetPasswordRequest("jane@example.com", "123456", "short"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(authWriteService);
    }

    @Test
    void refreshReturnsANewAccessToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh");
        when(authWriteService.refresh(request)).thenReturn(new AccessTokenResponse("new-access", 900));

        mvc.perform(post("/api/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("new-access"))
                .andExpect(jsonPath("$.data.expiresIn").value(900));
    }

    @Test
    void logoutSignsOutTheCurrentUser() throws Exception {
        authenticate();

        mvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signed out."));

        verify(authWriteService).logout(USER_ID);
    }

    @Test
    void deleteAccountSchedulesDeletionForTheCurrentUser() throws Exception {
        authenticate();
        Instant purgeBy = Instant.parse("2026-10-24T00:00:00Z");
        when(authWriteService.scheduleAccountDeletion(USER_ID)).thenReturn(new AccountDeletionResponse("Scheduled.", purgeBy));

        mvc.perform(delete("/api/v1/auth/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Scheduled."))
                .andExpect(jsonPath("$.data.purgeBy").value("2026-10-24T00:00:00Z"));
    }

    @Test
    void anUnexpectedFailureIsA500WithoutLeakingDetails() throws Exception {
        when(authWriteService.refresh(any())).thenThrow(new IllegalStateException("db exploded"));

        mvc.perform(post("/api/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RefreshTokenRequest("refresh"))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data.errorCode").value("internal_error"))
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_ERROR));
    }
}
