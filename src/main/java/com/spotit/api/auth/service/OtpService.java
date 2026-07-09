package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;

import java.util.UUID;

/**
 * Issues and verifies one-time codes. No email/SMS provider is wired up yet —
 * {@link OtpServiceImpl#issue} logs the code at INFO level as an explicit
 * stand-in for a real delivery integration (see the API spec's "replaces
 * UI-only" note).
 */
public interface OtpService {

    OtpCode issue(UUID userId, OtpPurpose purpose);

    OtpCode verify(UUID otpId, String code, OtpPurpose expectedPurpose);
}
