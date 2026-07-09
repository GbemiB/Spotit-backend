package com.spotit.api.auth.controller;

import com.spotit.api.auth.dto.*;
import com.spotit.api.auth.service.AuthWriteService;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthWriteService authWriteService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        return authWriteService.signup(request);
    }

    @PostMapping("/otp/verify")
    public TokenResponse verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return authWriteService.verifySignupOtp(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authWriteService.login(request);
    }

    @PostMapping("/forgot-password")
    public OtpRequestResponse forgotPassword(@Valid @RequestBody EmailRequest request) {
        return authWriteService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authWriteService.resetPassword(request);
        return new MessageResponse("Password updated.");
    }

    @PostMapping("/refresh")
    public AccessTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authWriteService.refresh(request);
    }

    @PostMapping("/logout")
    public MessageResponse logout(@CurrentUserId UUID userId) {
        authWriteService.logout(userId);
        return new MessageResponse("Signed out.");
    }

    @DeleteMapping("/account")
    public ResponseEntity<AccountDeletionResponse> deleteAccount(@CurrentUserId UUID userId) {
        return ResponseEntity.ok(authWriteService.scheduleAccountDeletion(userId));
    }
}
