package com.spotit.api.partner.service;

import com.spotit.api.partner.dto.PartnerProfileResponse;
import com.spotit.api.partner.dto.PartnerStatusResponse;

import java.util.UUID;

public interface PartnerReadService {
    PartnerStatusResponse getStatus(UUID userId);

    PartnerProfileResponse getOwnerProfile(UUID partnerId);
}
