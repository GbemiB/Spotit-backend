package com.spotit.api.partner.dto;

public record PartnerCodeResponse(
        String email,
        boolean sent,
        long expiresInSeconds,
        long resendInSeconds
) {
}
