package com.spotit.api.common.dto;

/**
 * Internal-only return type for {@code @ExceptionHandler} methods — never
 * seen by clients. {@link com.spotit.api.common.web.ApiResponseAdvice}
 * recognizes this type and unwraps it into the standard envelope with
 * {@code errorCode} moved into {@code data}.
 */
public record ApiErrorBody(String errorCode, String message) {
}
