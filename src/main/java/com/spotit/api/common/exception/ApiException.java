package com.spotit.api.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    private final UUID otpId;
    private final long expiresInSeconds;

    public ApiException(ErrorCode errorCode, String message) {
        this(errorCode, message, null, 0);
    }

    public ApiException(ErrorCode errorCode, String message, UUID otpId, long expiresInSeconds) {
        super(message);
        this.errorCode = errorCode;
        this.otpId = otpId;
        this.expiresInSeconds = expiresInSeconds;
    }

    public ApiException(ErrorCode errorCode) {
        this(errorCode, errorCode.name().toLowerCase(), null, 0);
    }
}
