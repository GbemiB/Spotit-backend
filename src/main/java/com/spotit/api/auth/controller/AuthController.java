package com.spotit.api.auth.controller;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.service.AuthWriteService;
import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Auth", description = "Signup, login, OTP verification, password reset, and session/token lifecycle.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthWriteService authWriteService;

    @Operation(summary = AuthControllerSwagger.SIGNUP_SUMMARY, description = AuthControllerSwagger.SIGNUP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = SignupRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.SIGNUP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created; verification OTP issued.", content = @Content(
                    schema = @Schema(implementation = SignupResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.SIGNUP_201_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "An account with this email already exists.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.SIGNUP_409_EXAMPLE)))
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        return authWriteService.signup(request);
    }

    @Operation(summary = AuthControllerSwagger.VERIFY_OTP_SUMMARY, description = AuthControllerSwagger.VERIFY_OTP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = OtpVerifyRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.VERIFY_OTP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP verified; call /signup/complete with this leadId to finish creating the account.", content = @Content(
                    schema = @Schema(implementation = SignupOtpVerifiedResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.VERIFY_OTP_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Invalid or already-used code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE))),
            @ApiResponse(responseCode = "410", description = "This code has expired.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.OTP_EXPIRED_410_EXAMPLE)))
    })
    @PostMapping("/otp/verify")
    public SignupOtpVerifiedResponse verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return authWriteService.verifySignupOtp(request);
    }

    @Operation(summary = AuthControllerSwagger.COMPLETE_SIGNUP_SUMMARY, description = AuthControllerSwagger.COMPLETE_SIGNUP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = CompleteSignupRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.COMPLETE_SIGNUP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account created; tokens issued.", content = @Content(
                    schema = @Schema(implementation = TokenResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.COMPLETE_SIGNUP_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "The OTP for this signup hasn't been verified yet.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "An account with this email already exists.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.SIGNUP_409_EXAMPLE)))
    })
    @PostMapping("/signup/complete")
    public TokenResponse completeSignup(@Valid @RequestBody CompleteSignupRequest request) {
        return authWriteService.completeSignup(request);
    }

    @Operation(summary = AuthControllerSwagger.RESEND_OTP_SUMMARY, description = AuthControllerSwagger.RESEND_OTP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = OtpResendRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.RESEND_OTP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A new code was issued and emailed.", content = @Content(
                    schema = @Schema(implementation = OtpRequestResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.RESEND_OTP_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Invalid or already-used code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE)))
    })
    @PostMapping("/otp/resend")
    public OtpRequestResponse resendOtp(@Valid @RequestBody OtpResendRequest request) {
        return authWriteService.resendOtp(request);
    }

    @Operation(summary = AuthControllerSwagger.LOGIN_SUMMARY, description = AuthControllerSwagger.LOGIN_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = LoginRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.LOGIN_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful; tokens issued.", content = @Content(
                    schema = @Schema(implementation = TokenResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.LOGIN_200_EXAMPLE))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CREDENTIALS_401_EXAMPLE))),
            @ApiResponse(responseCode = "403", description = "Correct credentials, but this account never finished email verification — a fresh code was just issued; data.otpId is where to verify it.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.EMAIL_NOT_VERIFIED_403_EXAMPLE)))
    })
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authWriteService.login(request);
    }

    @Operation(summary = AuthControllerSwagger.LOGIN_VERIFY_OTP_SUMMARY, description = AuthControllerSwagger.LOGIN_VERIFY_OTP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = OtpVerifyRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.VERIFY_OTP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verified; tokens issued.", content = @Content(
                    schema = @Schema(implementation = TokenResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.COMPLETE_SIGNUP_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Invalid or already-used code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE))),
            @ApiResponse(responseCode = "410", description = "This code has expired.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.OTP_EXPIRED_410_EXAMPLE)))
    })
    @PostMapping("/login/verify-otp")
    public TokenResponse verifyLoginOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return authWriteService.verifyLoginOtp(request);
    }

    @Operation(summary = AuthControllerSwagger.FORGOT_PASSWORD_SUMMARY, description = AuthControllerSwagger.FORGOT_PASSWORD_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = EmailRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.FORGOT_PASSWORD_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset code sent (or silently ignored if the email isn't registered).", content = @Content(
                    schema = @Schema(implementation = OtpRequestResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.FORGOT_PASSWORD_200_EXAMPLE)))
    })
    @PostMapping("/forgot-password")
    public OtpRequestResponse forgotPassword(@Valid @RequestBody EmailRequest request) {
        return authWriteService.forgotPassword(request);
    }

    @Operation(summary = AuthControllerSwagger.VERIFY_RESET_OTP_SUMMARY, description = AuthControllerSwagger.VERIFY_RESET_OTP_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = ResetOtpVerifyRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.VERIFY_RESET_OTP_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Code is valid.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.VERIFY_RESET_OTP_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Invalid or already-used code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE))),
            @ApiResponse(responseCode = "410", description = "This code has expired.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.OTP_EXPIRED_410_EXAMPLE)))
    })
    @PostMapping("/reset-password/verify-otp")
    public MessageResponse verifyResetOtp(@Valid @RequestBody ResetOtpVerifyRequest request) {
        authWriteService.verifyResetOtp(request);
        return new MessageResponse(ErrorMessage.CODE_VERIFIED);
    }

    @Operation(summary = AuthControllerSwagger.RESET_PASSWORD_SUMMARY, description = AuthControllerSwagger.RESET_PASSWORD_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = ResetPasswordRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.RESET_PASSWORD_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.RESET_PASSWORD_200_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Invalid or already-used code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_CODE_400_EXAMPLE))),
            @ApiResponse(responseCode = "410", description = "This code has expired.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.OTP_EXPIRED_410_EXAMPLE)))
    })
    @PostMapping("/reset-password")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authWriteService.resetPassword(request);
        return new MessageResponse("Password updated.");
    }

    @Operation(summary = AuthControllerSwagger.REFRESH_SUMMARY, description = AuthControllerSwagger.REFRESH_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = RefreshTokenRequest.class),
            examples = @ExampleObject(value = AuthControllerSwagger.REFRESH_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New access token issued.", content = @Content(
                    schema = @Schema(implementation = AccessTokenResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.REFRESH_200_EXAMPLE))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.INVALID_REFRESH_TOKEN_401_EXAMPLE)))
    })
    @PostMapping("/refresh")
    public AccessTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authWriteService.refresh(request);
    }

    @Operation(summary = AuthControllerSwagger.LOGOUT_SUMMARY, description = AuthControllerSwagger.LOGOUT_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Signed out.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.LOGOUT_200_EXAMPLE)))
    })
    @PostMapping("/logout")
    public MessageResponse logout(@CurrentUserId UUID userId) {
        authWriteService.logout(userId);
        return new MessageResponse("Signed out.");
    }

    @Operation(summary = AuthControllerSwagger.DELETE_ACCOUNT_SUMMARY, description = AuthControllerSwagger.DELETE_ACCOUNT_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deletion scheduled.", content = @Content(
                    schema = @Schema(implementation = AccountDeletionResponse.class),
                    examples = @ExampleObject(value = AuthControllerSwagger.DELETE_ACCOUNT_200_EXAMPLE)))
    })
    @DeleteMapping("/account")
    public ResponseEntity<AccountDeletionResponse> deleteAccount(@CurrentUserId UUID userId) {
        return ResponseEntity.ok(authWriteService.scheduleAccountDeletion(userId));
    }
}
