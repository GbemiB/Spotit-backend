package com.spotit.api.common.dto;

import java.util.UUID;

public record ErrorDetail(String errorCode, UUID otpId, Long expiresInSeconds) {
    public ErrorDetail(String errorCode) {
        this(errorCode, null, null);
    }
}
