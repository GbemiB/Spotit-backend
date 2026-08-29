package com.spotit.api.common.dto;

import java.util.UUID;

/**
 * Internal-only return type for {@code @ExceptionHandler} methods — never
 * seen by clients. {@link com.spotit.api.common.web.ApiResponseAdvice}
 * recognizes this type and unwraps it into the standard envelope with
 * {@code errorCode} (and, where set, {@code otpId}) moved into {@code data}.
 */
public record ApiErrorBody(String errorCode, String message, UUID otpId, Long expiresInSeconds) {
    public ApiErrorBody(String errorCode, String message) {
        this(errorCode, message, null, null);
    }

    public ApiErrorBody(String errorCode, String message, UUID otpId, long expiresInSeconds) {
        this(errorCode, message, otpId, expiresInSeconds == 0 ? null : Long.valueOf(expiresInSeconds));
    }
}
