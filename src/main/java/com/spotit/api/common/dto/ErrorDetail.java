package com.spotit.api.common.dto;

import java.util.UUID;

/**
 * Carried as the envelope's {@code data} on error responses, so the short machine-readable code
 * survives alongside the human message. {@code otpId} is only populated for errors that hand the
 * client something to act on next (see {@link com.spotit.api.common.exception.ApiException}) —
 * null otherwise.
 */
public record ErrorDetail(String errorCode, UUID otpId, Long expiresInSeconds) {
    public ErrorDetail(String errorCode) {
        this(errorCode, null, null);
    }
}
