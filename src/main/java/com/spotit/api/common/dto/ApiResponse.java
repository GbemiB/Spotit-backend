package com.spotit.api.common.dto;

/**
 * Standard envelope every endpoint responds with: {@code code} mirrors the
 * HTTP status, {@code message} is human-readable, and {@code data} is
 * whatever the endpoint actually returns (or null for errors/void actions).
 * Applied automatically by {@link com.spotit.api.common.web.ApiResponseAdvice}
 * — controllers keep returning plain DTOs and don't construct this directly.
 */
public record ApiResponse<T>(int code, String message, T data) {

    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}
