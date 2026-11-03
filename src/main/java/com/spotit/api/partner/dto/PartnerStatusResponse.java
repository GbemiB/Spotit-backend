package com.spotit.api.partner.dto;

import java.time.Instant;

public record PartnerStatusResponse(
        String role,
        String state,
        String inviteEmail,
        Instant inviteExpiresAt,
        String partnerFirstName,
        String ownerFirstName,
        String nickname,
        Boolean partnerReminder,
        Instant sharedSince
) {
    public static final String ROLE_OWNER = "owner";
    public static final String ROLE_PARTNER = "partner";

    public static final String STATE_NONE = "none";
    public static final String STATE_PENDING = "pending";
    public static final String STATE_EXPIRED = "expired";
    public static final String STATE_ACTIVE = "active";
    public static final String STATE_ENDED = "ended";
    public static final String STATE_INVITED = "invited";

    public static PartnerStatusResponse ownerNone() {
        return new PartnerStatusResponse(ROLE_OWNER, STATE_NONE, null, null, null, null, null, null, null);
    }

    public static PartnerStatusResponse ownerInvite(String state, String inviteEmail, Instant expiresAt) {
        return new PartnerStatusResponse(ROLE_OWNER, state, inviteEmail, expiresAt, null, null, null, null, null);
    }

    public static PartnerStatusResponse ownerActive(String partnerFirstName, Instant sharedSince) {
        return new PartnerStatusResponse(ROLE_OWNER, STATE_ACTIVE, null, null, partnerFirstName, null, null, null, sharedSince);
    }

    public static PartnerStatusResponse invited(String ownerFirstName) {
        return new PartnerStatusResponse(ROLE_OWNER, STATE_INVITED, null, null, null, ownerFirstName, null, null, null);
    }

    public static PartnerStatusResponse partnerActive(String ownerFirstName, String nickname, boolean partnerReminder, Instant sharedSince) {
        return new PartnerStatusResponse(ROLE_PARTNER, STATE_ACTIVE, null, null, null, ownerFirstName, nickname, partnerReminder, sharedSince);
    }

    public static PartnerStatusResponse partnerEnded() {
        return new PartnerStatusResponse(ROLE_PARTNER, STATE_ENDED, null, null, null, null, null, null, null);
    }
}
