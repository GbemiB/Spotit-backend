package com.spotit.api.auth.service;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import com.spotit.api.user.entity.User;

import java.util.UUID;

/** Issues and verifies one-time codes, emailing the code to the user on issue. */
public interface OtpService {

    OtpCode issue(User user, OtpPurpose purpose);

    OtpCode verify(UUID otpId, String code, OtpPurpose expectedPurpose);

    /** Verifies against the user's most recent unconsumed OTP for the purpose, rather than a client-supplied id — used where handing back an otpId would leak account existence (e.g. password reset). */
    OtpCode verifyLatest(UUID userId, String code, OtpPurpose expectedPurpose);

    /** Looks up the OTP's user/purpose and issues a fresh code in its place, invalidating the old one. */
    OtpCode resend(UUID otpId);

    long ttlSeconds();
}
