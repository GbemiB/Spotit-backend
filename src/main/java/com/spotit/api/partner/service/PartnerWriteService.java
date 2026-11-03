package com.spotit.api.partner.service;

import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.dto.PartnerStatusResponse;

import java.util.UUID;

public interface PartnerWriteService {
    PartnerStatusResponse invite(UUID ownerId, InviteRequest request);

    PartnerStatusResponse resendInvite(UUID ownerId);

    void cancelInvite(UUID ownerId);

    PartnerStatusResponse accept(UUID userId, AcceptInviteRequest request);

    PartnerStatusResponse updateSettings(UUID userId, PartnerSettingsRequest request);

    void unlink(UUID userId);

    void endAllFor(UUID userId);
}
