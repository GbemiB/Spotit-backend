package com.spotit.api.partner.service;

import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;

import java.util.UUID;

public interface PartnerCheckInService {
    PartnerCheckInResponse getToday(UUID partnerId);

    PartnerCheckInResponse checkIn(UUID partnerId, PartnerCheckInRequest request);
}
