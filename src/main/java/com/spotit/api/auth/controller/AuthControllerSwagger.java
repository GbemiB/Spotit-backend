package com.spotit.api.auth.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link AuthController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class AuthControllerSwagger {

    private AuthControllerSwagger() {
    }

    static final String SIGNUP_SUMMARY = "Sign up (step 1: name + email)";
    static final String SIGNUP_DESCRIPTION = "Creates or refreshes a signup lead (no account yet) and issues a one-time code to verify the "
            + "email address. Call /otp/verify with the returned otpId, then /signup/complete with a password to create the account.";
    static final String SIGNUP_REQUEST_EXAMPLE = """
            {
              "firstName": "Amara",
              "lastName": "Okafor",
              "email": "amara@example.com"
            }
            """;
    static final String SIGNUP_201_EXAMPLE = """
            {
              "code": 201,
              "message": "Created",
              "data": {
                "otpId": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
                "email": "amara@example.com",
                "expiresInSeconds": 600
              }
            }
            """;
    static final String SIGNUP_409_EXAMPLE = """
            {
              "code": 409,
              "message": "An account with this email already exists.",
              "data": { "errorCode": "email_already_registered" }
            }
            """;

    static final String VERIFY_OTP_SUMMARY = "Verify signup OTP (step 2)";
    static final String VERIFY_OTP_DESCRIPTION = "Confirms the code sent at signup. No account or session exists yet — call /signup/complete "
            + "with the returned leadId and a password to actually create the account.";
    static final String VERIFY_OTP_REQUEST_EXAMPLE = """
            {
              "otpId": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
              "code": "482913"
            }
            """;
    static final String VERIFY_OTP_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "leadId": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
                "email": "amara@example.com"
              }
            }
            """;

    static final String COMPLETE_SIGNUP_SUMMARY = "Complete signup (step 3: create password)";
    static final String COMPLETE_SIGNUP_DESCRIPTION = "Creates the account and issues tokens. Only allowed once the OTP for this leadId has "
            + "been verified via /otp/verify — this is the only place a password is ever set for a new account.";
    static final String COMPLETE_SIGNUP_REQUEST_EXAMPLE = """
            {
              "leadId": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
              "password": "SuperSecret123"
            }
            """;
    static final String COMPLETE_SIGNUP_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0In0.abc123",
                "refreshToken": "8f14e45fceea167a5a36dedd4bea2543",
                "expiresIn": 3600,
                "user": { "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6", "onboarded": false }
              }
            }
            """;
    static final String INVALID_CODE_400_EXAMPLE = """
            {
              "code": 400,
              "message": "Invalid or already-used code.",
              "data": { "errorCode": "invalid_code" }
            }
            """;
    static final String OTP_EXPIRED_410_EXAMPLE = """
            {
              "code": 410,
              "message": "This code has expired.",
              "data": { "errorCode": "otp_expired" }
            }
            """;

    static final String RESEND_OTP_SUMMARY = "Resend OTP";
    static final String RESEND_OTP_DESCRIPTION = "Issues a fresh one-time code for the same user and purpose as the given otpId, invalidating the previous code.";
    static final String RESEND_OTP_REQUEST_EXAMPLE = """
            {
              "otpId": "9d1e2b0a-1234-4c56-9abc-1234567890ab"
            }
            """;
    static final String RESEND_OTP_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "message": "A new code has been sent.",
                "otpId": "1a2b3c4d-5678-4c56-9abc-0987654321cd",
                "expiresInSeconds": 600
              }
            }
            """;

    static final String LOGIN_SUMMARY = "Log in";
    static final String LOGIN_DESCRIPTION = "Authenticates with email and password and issues access/refresh tokens.";
    static final String LOGIN_REQUEST_EXAMPLE = """
            {
              "email": "amara@example.com",
              "password": "SuperSecret123"
            }
            """;
    static final String LOGIN_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0In0.abc123",
                "refreshToken": "8f14e45fceea167a5a36dedd4bea2543",
                "expiresIn": 3600,
                "user": { "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6", "onboarded": true }
              }
            }
            """;
    static final String INVALID_CREDENTIALS_401_EXAMPLE = """
            {
              "code": 401,
              "message": "Invalid email or password.",
              "data": { "errorCode": "invalid_credentials" }
            }
            """;

    static final String FORGOT_PASSWORD_SUMMARY = "Request a password reset";
    static final String FORGOT_PASSWORD_DESCRIPTION = "Issues a one-time code to reset the password. Always returns the same message, whether or not the email is registered, to avoid account enumeration.";
    static final String FORGOT_PASSWORD_REQUEST_EXAMPLE = """
            {
              "email": "amara@example.com"
            }
            """;
    static final String FORGOT_PASSWORD_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "message": "If that email exists, a reset code has been sent.",
                "otpId": null,
                "expiresInSeconds": 600
              }
            }
            """;

    static final String VERIFY_RESET_OTP_SUMMARY = "Verify a password-reset code";
    static final String VERIFY_RESET_OTP_DESCRIPTION = "Confirms the code sent to the given email is correct and unexpired, without consuming it or changing the password — lets the client gate the new-password form on a valid code before asking for it.";
    static final String VERIFY_RESET_OTP_REQUEST_EXAMPLE = """
            {
              "email": "amara@example.com",
              "code": "482913"
            }
            """;
    static final String VERIFY_RESET_OTP_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Code verified.",
              "data": null
            }
            """;

    static final String RESET_PASSWORD_SUMMARY = "Reset password";
    static final String RESET_PASSWORD_DESCRIPTION = "Confirms the password-reset OTP sent to the given email and sets a new password. Revokes all existing refresh tokens.";
    static final String RESET_PASSWORD_REQUEST_EXAMPLE = """
            {
              "email": "amara@example.com",
              "code": "482913",
              "newPassword": "EvenBetterSecret456"
            }
            """;
    static final String RESET_PASSWORD_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Password updated.",
              "data": null
            }
            """;

    static final String REFRESH_SUMMARY = "Refresh access token";
    static final String REFRESH_DESCRIPTION = "Exchanges a valid, unrevoked refresh token for a new access token.";
    static final String REFRESH_REQUEST_EXAMPLE = """
            {
              "refreshToken": "8f14e45fceea167a5a36dedd4bea2543"
            }
            """;
    static final String REFRESH_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0In0.def456",
                "expiresIn": 3600
              }
            }
            """;
    static final String INVALID_REFRESH_TOKEN_401_EXAMPLE = """
            {
              "code": 401,
              "message": "Invalid or expired refresh token.",
              "data": { "errorCode": "invalid_refresh_token" }
            }
            """;

    static final String LOGOUT_SUMMARY = "Log out";
    static final String LOGOUT_DESCRIPTION = "Revokes all refresh tokens for the current user.";
    static final String LOGOUT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Signed out.",
              "data": null
            }
            """;

    static final String DELETE_ACCOUNT_SUMMARY = "Schedule account deletion";
    static final String DELETE_ACCOUNT_DESCRIPTION = "Schedules the current user's account for deletion after a 30-day grace period and revokes all refresh tokens.";
    static final String DELETE_ACCOUNT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "message": "Account deletion scheduled.",
                "purgeBy": "2026-08-09T00:00:00Z"
              }
            }
            """;
}
