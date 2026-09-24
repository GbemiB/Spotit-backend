package com.spotit.api.common.dto;

import java.util.UUID;

public record ApiErrorBody(String errorCode, String message, UUID otpId, Long expiresInSeconds) {
    public ApiErrorBody(String errorCode, String message) {
        this(errorCode, message, null, null);
    }

    public ApiErrorBody(String errorCode, String message, UUID otpId, long expiresInSeconds) {
        this(errorCode, message, otpId, expiresInSeconds == 0 ? null : Long.valueOf(expiresInSeconds));
    }
}
