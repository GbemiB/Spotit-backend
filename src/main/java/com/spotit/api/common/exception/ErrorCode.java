package com.spotit.api.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT),
    WEAK_PASSWORD(HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_CODE(HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(HttpStatus.GONE),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY),
    CYCLE_LENGTH_OUT_OF_RANGE(HttpStatus.UNPROCESSABLE_ENTITY),
    PERIOD_LENGTH_OUT_OF_RANGE(HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_GOAL(HttpStatus.UNPROCESSABLE_ENTITY),
    LEVEL_TOO_LOW(HttpStatus.FORBIDDEN),
    PREMIUM_REQUIRED(HttpStatus.FORBIDDEN),
    INSUFFICIENT_POINTS(HttpStatus.PAYMENT_REQUIRED),
    AD_NOT_VERIFIED(HttpStatus.BAD_REQUEST),
    DAILY_AD_LIMIT_REACHED(HttpStatus.TOO_MANY_REQUESTS),
    NOT_YET_COMPLETE(HttpStatus.CONFLICT),
    ALREADY_CLAIMED(HttpStatus.CONFLICT),
    RECEIPT_INVALID(HttpStatus.PAYMENT_REQUIRED),
    ALREADY_SUBSCRIBED(HttpStatus.CONFLICT),
    NO_PURCHASE_FOUND(HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
