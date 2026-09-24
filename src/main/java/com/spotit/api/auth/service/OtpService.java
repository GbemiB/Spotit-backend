package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.user.entity.User;

import java.util.UUID;

public interface OtpService {
    OtpCode issue(User user, OtpPurpose purpose);

    OtpCode verify(UUID otpId, String code, OtpPurpose expectedPurpose);

    OtpCode verifyLatest(UUID userId, String code, OtpPurpose expectedPurpose);

    void checkValid(UUID userId, String code, OtpPurpose expectedPurpose);

    OtpCode resend(UUID otpId);

    long ttlSeconds();
}
